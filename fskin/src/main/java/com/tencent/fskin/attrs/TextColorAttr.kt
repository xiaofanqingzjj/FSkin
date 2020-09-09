package com.tencent.fskin.attrs

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr
import com.tencent.fskin.SkinResourcesProxy


/**
 * TextView
 *
 * android:textColor
 */
class TextColorAttr : SkinElementAttr() {

    companion object {
        const val TAG = "TextColorAttr"
    }

    override fun apply(view: View?, skinResourcesProxy: Resources) {
        super.apply(view, skinResourcesProxy)
//        Log.d(TAG, "applyView:$view, this: $this")

        (view as? TextView)?.run {
            setTextColor(skinResourcesProxy.getColorStateList(attrValueRefId))
        }
    }
}