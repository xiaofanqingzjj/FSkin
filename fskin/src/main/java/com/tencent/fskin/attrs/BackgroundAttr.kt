package com.tencent.fskin.attrs

import android.content.res.Resources
import android.view.View
import com.tencent.fskin.SkinElementAttr


/**
 *
 * View
 *
 * android:background
 */
class BackgroundAttr : SkinElementAttr() {

    override fun apply(view: View?, skinResourcesProxy: Resources) {
        super.apply(view, skinResourcesProxy)

//        view?.context?.obtainStyledAttributes()

        view?.run {
            when (attrValueTypeName) {
                "color" -> setBackgroundColor(skinResourcesProxy.getColor(attrValueRefId))
                "drawable" -> background = (skinResourcesProxy.getDrawable(attrValueRefId))
            }
        }
    }
}