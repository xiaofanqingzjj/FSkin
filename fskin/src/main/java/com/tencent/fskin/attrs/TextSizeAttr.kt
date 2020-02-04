package com.tencent.fskin.attrs

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr


/**
 * android:textColor
 */
class TextSizeAttr : SkinElementAttr() {

    companion object {
        const val TAG = "TextSizeAttr"
    }

    override fun apply(view: View?) {
        super.apply(view)
//        Log.d(TAG, "applyView:$view, this: $this")

        (view as? TextView)?.run {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, SkinManager.skinResourcesProxy.getDimension(attrValueRefId))
//            setTextColor(SkinManager.skinResourcesProxy.getColorStateList(attrValueRefId))
        }
    }
}