package com.tencent.fskin.attrs

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr
import com.tencent.fskin.SkinResourcesProxy


/**
 *
 * ImageView
 *
 * android:src
 */
class ImageSrcAttr : SkinElementAttr() {

    companion object {
        const val TAG = "ImageSrcAttr"
    }

    override fun apply(view: View?, skinResourcesProxy: Resources) {
        super.apply(view, skinResourcesProxy)
        Log.d(TAG, "applyView:$view, this: $this")

        (view as? ImageView)?.run {
            setImageDrawable(skinResourcesProxy.getDrawable(attrValueRefId))
        }
    }
}