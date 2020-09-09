package com.tencent.fskin.attrs

import android.content.res.Resources
import android.view.View
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr


/**
 * android:paddingXXXX
 */
class PaddingAttr : SkinElementAttr() {

    companion object {
        const val TAG = "PaddingAttr"
    }

    override fun apply(view: View?, resources: Resources) {
        super.apply(view, resources)
        view?.run {
            val padding = resources.getDimension(attrValueRefId).toInt()

            when (attrName) {
                "padding" -> {
                    setPadding(padding, padding, padding, padding)
                }
                "paddingLeft" -> {
                    setPadding(padding, paddingTop, paddingRight, paddingBottom)
                }
                "paddingTop" -> {
                    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
                }
                "paddingRight" -> {
                    setPadding(paddingLeft, paddingTop, padding, paddingBottom)
                }
                "paddingBottom" -> {
                    setPadding(paddingLeft, paddingTop, paddingRight, padding)
                }
            }
        }
    }
}