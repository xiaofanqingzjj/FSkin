package com.tencent.fskin.attrs

import android.view.View
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr


/**
 *
 * View
 *
 * android:background
 */
class BackgroundAttr : SkinElementAttr() {

    override fun apply(view: View?) {
        super.apply(view)


        view?.run {
            when (attrValueTypeName) {
                "color" -> setBackgroundColor(SkinManager.skinResourcesProxy.getColor(attrValueRefId))
                "drawable" -> background = (SkinManager.skinResourcesProxy.getDrawable(attrValueRefId))
            }
        }
    }
}