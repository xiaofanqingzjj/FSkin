package com.tencent.fskin

import android.content.res.Resources
import android.view.View
import java.util.*


/**
 * 表示界面上一个可以换肤的元素，一般是一个View
 *
 */
data class SkinElement(

        /**
         * 关联的View
         */
        var view: View,

        /**
         * 当前View上需要修改的属性
         */
        var attrs: ArrayList<SkinElementAttr> = ArrayList()) {



    internal fun initApply(skinResource: Resources) {
        if (!SkinManager.isDefaultSkin()) {
            apply(skinResource)
        }
    }

    /**
     * 通知皮肤修改了
     */
    fun apply(skinResource: Resources) {
        attrs.forEach {
            it.apply(view, skinResource)
        }
    }

    fun clean() {
        attrs.clear()
    }


}