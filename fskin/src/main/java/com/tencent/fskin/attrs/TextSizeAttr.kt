package com.tencent.fskin.attrs

import android.content.res.Resources
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

    override fun apply(view: View?, resources: Resources) {
        super.apply(view, resources)
//        Log.d(TAG, "applyView:$view, this: $this")

        (view as? TextView)?.run {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(attrValueRefId))
//            setTextColor(SkinManager.skinResourcesProxy.getColorStateList(attrValueRefId))
        }
    }
}