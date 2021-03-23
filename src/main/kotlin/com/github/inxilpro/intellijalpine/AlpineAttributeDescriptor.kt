package com.github.inxilpro.intellijalpine

import com.intellij.psi.PsiElement
import com.intellij.psi.meta.PsiPresentableMetaData
import com.intellij.util.ArrayUtil
import com.intellij.xml.impl.BasicXmlAttributeDescriptor

class AlpineAttributeDescriptor(
    private val name: String
) :
    BasicXmlAttributeDescriptor(),
    PsiPresentableMetaData {
    override fun getIcon() = Alpine.ICON
    override fun getTypeName() = "Alpine.js"
    override fun init(psiElement: PsiElement) {}
    override fun isRequired(): Boolean = false
    override fun hasIdType(): Boolean {
        return name == "id"
    }

    override fun hasIdRefType(): Boolean = false
    override fun isEnumerated(): Boolean = false
    override fun getDeclaration(): PsiElement? = null
    override fun getName(): String = name
    override fun getDependencies(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY
    override fun isFixed(): Boolean = false
    override fun getDefaultValue(): String? = null
    override fun getEnumeratedValues(): Array<String>? = ArrayUtil.EMPTY_STRING_ARRAY
}