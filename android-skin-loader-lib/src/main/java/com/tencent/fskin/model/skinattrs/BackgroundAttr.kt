package com.tencent.fskin.model.skinattrs

import android.view.View
import com.tencent.fskin.SkinManager
import com.tencent.fskin.model.SkinAttr


/**
 * android:background
 */
class BackgroundAttr : SkinAttr() {

    override fun apply(view: View?) {
        view?.run {
            when (attrValueTypeName) {
                RES_TYPE_NAME_COLOR -> setBackgroundColor(SkinManager.resources.getColor(attrValueRefId))
                RES_TYPE_NAME_DRAWABLE -> background = (SkinManager.resources.getDrawable(attrValueRefId))
            }
        }
    }
}