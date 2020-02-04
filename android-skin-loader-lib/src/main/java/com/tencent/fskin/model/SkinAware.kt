package com.tencent.fskin.model

import android.view.View
import java.util.*


/**
 * 表示界面上一个可以换肤的元素，一般是一个View
 *
 */
data class SkinAware(

        /**
         * 关联的View
         */
        var view: View? = null,

        /**
         * 当前View上需要修改的属性
         */
        var attrs: ArrayList<SkinAttr> = ArrayList()) {


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