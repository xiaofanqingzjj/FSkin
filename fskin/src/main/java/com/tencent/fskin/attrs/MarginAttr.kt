package com.tencent.fskin.attrs

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import com.tencent.fskin.SkinElementAttr
import com.tencent.fskin.SkinResourcesProxy


/**
 *
 * View
 *
 * android:marginX
 */
class MarginAttr : SkinElementAttr() {

    companion object {
        const val TAG = "MarginAttr"
    }

    override fun apply(view: View?, skinResourcesProxy: Resources) {
        super.apply(view, skinResourcesProxy)
        view?.run {
            val padding = skinResourcesProxy.getDimension(attrValueRefId).toInt()

            val lp = (layoutParams as? ViewGroup.MarginLayoutParams) ?: return
            when (attrName) {
                "layoutMargin" -> {
                    lp.run {
                        leftMargin = padding
                        topMargin = padding
                        rightMargin = padding
                        bottomMargin = padding
                    }
                }
                "layoutMarginLeft" -> {
                    lp.run {
                        leftMargin = padding
                    }
                }
                "layoutMarginTop" -> {
                    lp.run {
                        topMargin = padding
                    }
                }
                "layoutMarginRight" -> {
                    lp.run {
                        rightMargin = padding
                    }
                }
                "layoutMarginBottom" -> {
                    lp.run {
                        bottomMargin = padding
                    }
                }
                else -> {}
            }
        }
    }
}