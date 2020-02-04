package com.tencent.fskin.model.skinattrs

import android.util.Log
import android.view.View
import android.widget.TextView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.model.SkinAttr


/**
 * android:textColor
 */
class TextColorAttr : SkinAttr() {

    companion object {
        const val TAG = "TextColorAttr"
    }

    override fun apply(view: View?) {
        Log.d(TAG, "apply view: $view, this: $this")

        (view as? TextView)?.run {


//            setTextColor(SkinManager.resources.getColor(attrValueRefId))
        }
    }
}