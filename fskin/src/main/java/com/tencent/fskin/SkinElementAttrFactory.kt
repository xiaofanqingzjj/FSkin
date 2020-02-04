package com.tencent.fskin

import com.tencent.fskin.attrs.*


/**
 * 在这里可以添加动态换肤的属性
 *
 */
object SkinElementAttrFactory {

    private val supportSkinAttrs: MutableMap<String, Class<out SkinElementAttr>> = mutableMapOf()


    /**
     * 添加一个换肤
     *
     * @param attrName
     * @param attrElementClazz
     *
     */
    fun registerSkinAttr(attrName: String, attrElementClazz: Class<out SkinElementAttr>) {
        supportSkinAttrs[attrName] = attrElementClazz
    }


    init {
        registerSkinAttr("background", BackgroundAttr::class.java)
        registerSkinAttr("textColor", TextColorAttr::class.java)
        registerSkinAttr("src", ImageSrcAttr::class.java)
        registerSkinAttr("textSize", TextSizeAttr::class.java)

        registerSkinAttr("drawableLeft", TextViewDrawableAttr::class.java)
        registerSkinAttr("drawableTop", TextViewDrawableAttr::class.java)
        registerSkinAttr("drawableRight", TextViewDrawableAttr::class.java)
        registerSkinAttr("drawableBottom", TextViewDrawableAttr::class.java)

        // padding
        registerSkinAttr("padding", PaddingAttr::class.java)
        registerSkinAttr("paddingLeft", PaddingAttr::class.java)
        registerSkinAttr("paddingTop", PaddingAttr::class.java)
        registerSkinAttr("paddingRight", PaddingAttr::class.java)
        registerSkinAttr("paddingBottom", PaddingAttr::class.java)


    }


    /**
     *
     *
     *
     *
     */
    fun createSkinAttr(attrName: String, attrValueRefId: Int, attrValueRefName: String?, typeName: String?): SkinElementAttr? {
        val skinAttr = supportSkinAttrs[attrName]?.newInstance()
        skinAttr?.run {
            this.attrName = attrName
            this.attrValueRefId = attrValueRefId
            this.attrValueRefName = attrValueRefName
            this.attrValueTypeName = typeName
        }
        return skinAttr
    }

    fun isSupportedAttr(attrName: String): Boolean {
        return supportSkinAttrs.keys.contains(attrName)
    }
}