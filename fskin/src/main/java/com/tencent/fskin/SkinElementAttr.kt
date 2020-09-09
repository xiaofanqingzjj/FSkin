package com.tencent.fskin

import android.content.res.Resources
import android.util.Log
import android.view.View
import com.tencent.tfkin.BuildConfig


/**
 * 一个皮肤属性
 *
 * 表示一个xml元素的属性，当皮肤变化时，如果View上有对应的属性并引用了资源，则需要动态修改
 *
 */
abstract class SkinElementAttr(

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




    internal fun initApply(view: View?, skinResource: Resources) {
        if (!SkinManager.isDefaultSkin()) {
            apply(view, skinResource)
        }
    }

    /**
     * 当皮肤改变的时候重新给View对应的属性设置皮肤包里的新值
     */
    open fun apply(view: View?, skinResource: Resources) {
        if (BuildConfig.DEBUG) {
            Log.d("SkinElementAttr", "apply attr:$this, view:$view")
        }
    }

    override fun toString(): String {
        return ("SkinAttr [attrName=" + attrName + ", "
                + "attrValueRefId=" + attrValueRefId + ","
                + "attrValueRefName=" + attrValueRefName + ", "
                + "attrValueTypeName=" + attrValueTypeName
                + "]")
    }

}