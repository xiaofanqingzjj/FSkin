package com.tencent.fskin.demo.util

import android.content.Context

object DensityUtil {

    fun getHeightInPx(context: Context): Float {
        return context.resources.displayMetrics.heightPixels.toFloat()
    }

    fun getWidthInPx(context: Context): Float {
        return context.resources.displayMetrics.widthPixels.toFloat()
    }

    fun getHeightInDp(context: Context): Int {
        val height = context.resources.displayMetrics.heightPixels.toFloat()
        return px2dip(context, height)
    }

    fun getWidthInDp(context: Context): Int {
        val height = context.resources.displayMetrics.heightPixels.toFloat()
        return px2dip(context, height)
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (spValue * scale + 0.5f).toInt()
    }

}


/**
 * dip转px的扩展方法
 */
fun Float.toPx(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}


fun Int.toPx(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}