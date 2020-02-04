package com.tencent.fskin.model

import android.view.View


/**
 * 一个皮肤属性
 *
 * 表示一个xml元素的属性，当皮肤变化时，如果View上有对应的属性并引用了资源，则需要动态修改
 *
 */
abstract class SkinAttr(

        /**
         * xml属性名，比如：background、src、textColor等等
         */
        var attrName: String? = null,

        /**
         * 属性的资源引用id
         */
        var attrValueRefId: Int = 0,


        /**
         * 属性值资源名
         */
        var attrValueRefName: String? = null,

        /**
         * 属性值资源类型名
         */
        var attrValueTypeName: String? = null) {


    /**
     * 当皮肤改变的时候重新给View对应的属性设置皮肤包里的新值
     */
    open fun apply(view: View?) {




    }

    override fun toString(): String {
        return ("SkinAttr \n[\nattrName=" + attrName + ", \n"
                + "attrValueRefId=" + attrValueRefId + ", \n"
                + "attrValueRefName=" + attrValueRefName + ", \n"
                + "attrValueTypeName=" + attrValueTypeName
                + "\n]")
    }

    companion object {
        const val RES_TYPE_NAME_COLOR = "color"
        const val RES_TYPE_NAME_DRAWABLE = "drawable"
    }
}