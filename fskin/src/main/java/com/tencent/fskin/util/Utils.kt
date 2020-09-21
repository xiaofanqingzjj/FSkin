package com.tencent.fskin.util

import android.content.Context
import android.graphics.drawable.Drawable


/**
 * 从当前的主题中加载指定属性的Drawable
 *
 * @param attrId 属性
 *
 */
fun Context.getThemeDrawableAttribute(attrId: Int): Drawable? {
    val attrs = intArrayOf(attrId)
    val a = obtainStyledAttributes(attrs)
    val value = a.getDrawable(0)
    a.recycle()
    return value
}
