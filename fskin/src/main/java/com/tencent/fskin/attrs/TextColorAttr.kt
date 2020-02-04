package com.tencent.fskin.attrs

import android.view.View
import android.widget.TextView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr


/**
 * TextView
 *
 * android:textColor
 */
class TextColorAttr : SkinElementAttr() {

    companion object {
        const val TAG = "TextColorAttr"
    }

    override fun apply(view: View?) {
        super.apply(view)
//        Log.d(TAG, "applyView:$view, this: $this")

        (view as? TextView)?.run {
            setTextColor(SkinManager.skinResourcesProxy.getColorStateList(attrValueRefId))
        }
    }
}