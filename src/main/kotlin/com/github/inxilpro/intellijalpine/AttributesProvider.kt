package com.github.inxilpro.intellijalpine

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.html.dtd.HtmlElementDescriptorImpl
import com.intellij.psi.impl.source.html.dtd.HtmlNSDescriptorImpl
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ArrayUtil
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider
import com.intellij.xml.impl.BasicXmlAttributeDescriptor

class AttributesProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(xmlTag: XmlTag): Array<XmlAttributeDescriptor> {
        val descriptors = mutableListOf<AttributeDescriptor>()

        // FIXME
//        for (directive in Alpine.allDirectives(xmlTag.name)) {
//            descriptors.add(AttributeDescriptor(directive))
//        }

        for (descriptor in getDefaultHtmlAttributes(xmlTag)) {
            if (descriptor.name.startsWith("on")) {
                val event = descriptor.name.substring(2)
                for (prefix in Alpine.EVENT_PREFIXES) {
                    descriptors.add(AttributeDescriptor(prefix + event))
                }
            } else {
                for (prefix in Alpine.BIND_PREFIXES) {
                    descriptors.add(AttributeDescriptor(prefix + descriptor.name))
                }
            }
        }

        return descriptors.toTypedArray()
    }

    @Suppress("ReturnCount")
    override fun getAttributeDescriptor(name: String, xmlTag: XmlTag): XmlAttributeDescriptor? {
        for (prefix in Alpine.EVENT_PREFIXES) {
            if (name.startsWith(prefix)) {
                val descriptor = xmlTag.descriptor
                if (descriptor != null) {
                    val attrName = name.substring(prefix.length)
                    val attributeDescriptor = descriptor.getAttributeDescriptor(attrName, xmlTag)
                    return attributeDescriptor ?: descriptor.getAttributeDescriptor("on$attrName", xmlTag)
                }
            }
        }

        for (prefix in Alpine.BIND_PREFIXES) {
            if (name.startsWith(prefix)) {
                val descriptor = xmlTag.descriptor
                if (descriptor != null) {
                    val attrName = name.substring(prefix.length)
                    val attributeDescriptor = descriptor.getAttributeDescriptor(attrName, xmlTag)
                    return attributeDescriptor ?: descriptor.getAttributeDescriptor(attrName, xmlTag)
                }
            }
        }

        return null
    }

    private fun getDefaultHtmlAttributes(xmlTag: XmlTag): Array<out XmlAttributeDescriptor> {
        return (xmlTag.descriptor as? HtmlElementDescriptorImpl
            ?: HtmlNSDescriptorImpl.guessTagForCommonAttributes(xmlTag) as? HtmlElementDescriptorImpl)
            ?.getDefaultAttributeDescriptors(xmlTag) ?: emptyArray()
    }

    // Dropping PsiPresentableMetaData for now
    private class AttributeDescriptor(private val name: String) :
        BasicXmlAttributeDescriptor() {
        // override fun getIcon() = Alpine.ICON

        // override fun getTypeName(): String? = "Alpine.js"

        override fun init(psiElement: PsiElement) {
        }

        override fun isRequired(): Boolean = false
        override fun hasIdType(): Boolean = false
        override fun hasIdRefType(): Boolean = false
        override fun isEnumerated(): Boolean = false
        override fun getDeclaration(): PsiElement? = null
        override fun getName(): String = name
        override fun getDependencies(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY
        override fun isFixed(): Boolean = false
        override fun getDefaultValue(): String? = null
        override fun getEnumeratedValues(): Array<String>? = ArrayUtil.EMPTY_STRING_ARRAY
    }
}
