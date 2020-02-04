package com.tencent.fskin.attrs

import android.view.View
import android.widget.TextView
import com.tencent.fskin.SkinManager
import com.tencent.fskin.SkinElementAttr


/**
 * android:textColor
 */
class TextViewDrawableAttr : SkinElementAttr() {

    companion object {
        const val TAG = "TextSizeAttr"
    }

    override fun apply(view: View?) {
        super.apply(view)

        (view as? TextView)?.run {

            val drawable = SkinManager.skinResourcesProxy.getDrawable(attrValueRefId)
            val cds = compoundDrawables //左上右下

            when (attrName) {
                "drawableLeft"-> {
                    setCompoundDrawablesWithIntrinsicBounds(drawable, cds[1], cds[2], cds[3])
                }
                "drawableTop"-> {
                    setCompoundDrawablesWithIntrinsicBounds(cds[0], drawable, cds[2], cds[3])
                }
                "drawableRight"-> {
                    setCompoundDrawablesWithIntrinsicBounds(cds[0], cds[1], drawable, cds[3])
                }
                "drawableBottom"-> {
                    setCompoundDrawablesWithIntrinsicBounds(cds[0], cds[1], cds[2], drawable)
                }
                "drawableStart"-> {
                    setCompoundDrawablesWithIntrinsicBounds(drawable, cds[1], cds[2], cds[3])
                }
                "drawableEnd"-> {
                    setCompoundDrawablesWithIntrinsicBounds(cds[0], cds[1], drawable, cds[3])
                }
            }
        }
    }
}