package com.tencent.fskin.attrs

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

    override fun apply(view: View?) {
        super.apply(view)
        view?.run {
            val padding = SkinManager.skinResourcesProxy.getDimension(attrValueRefId).toInt()

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