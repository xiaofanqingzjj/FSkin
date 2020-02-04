package com.tencent.fskin

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



    internal fun initApply() {
        if (!SkinManager.isDefaultSkin()) {
            apply()
        }
    }

    /**
     * 通知皮肤修改了
     */
    fun apply() {
        attrs.forEach {
            it.apply(view)
        }
    }

    fun clean() {
        attrs.clear()
    }


}