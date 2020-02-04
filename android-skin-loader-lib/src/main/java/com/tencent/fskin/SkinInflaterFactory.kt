package com.tencent.fskin

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cn.feng.skin.manager.util.L
import com.example.android_skin_loader_lib.BuildConfig
import com.tencent.fskin.model.SkinAttr
import com.tencent.fskin.model.SkinAttrFactory
import com.tencent.fskin.model.SkinAware
import kotlin.collections.ArrayList


/**
 *
 *
 */
class SkinInflaterFactory : LayoutInflater.Factory {

    companion object {
        private const val DEBUG = true
        const val TAG = "SkinInflaterFactory@@"
    }

    /**
     * Store the view item that need skin changing in the activity
     */
    private val mSkinItems: MutableList<SkinAware> = ArrayList()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? { // if this is NOT enable to be skined , simplly skip it
        // 先收集属性，看是否有换肤支持的属性，如果没有，则不拦截
        val skinAttrs = parseSkinAttr(context, attrs)
        if (skinAttrs.isEmpty()) {
            return null
        }

        val view = createView(context, name, attrs) ?: return null

        mSkinItems.add(SkinAware(view, skinAttrs).apply {


//            apply()
        })

        return view
    }

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {

        var view: View? = null
        try {
            if (-1 == name.indexOf('.')) { // 系统自带的widget

                if ("View" == name) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs)
                }

                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs)
                }

                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs)
                }

            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs)
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onCreateView context:$context, name:$name")
            }

        } catch (e: Exception) {
            L.e("error while create 【" + name + "】 : " + e.message)
            view = null
        }
        return view
    }

    private fun parseSkinAttr(context: Context, attrs: AttributeSet) :  ArrayList<SkinAttr>{
        val viewAttrs: ArrayList<SkinAttr> = ArrayList()

        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)
            if (!SkinAttrFactory.isSupportedAttr(attrName)) { // 看属性是否是支持换肤的属性
                continue
            }

            if (attrValue.startsWith("@")) { // 只有@开头的才表示用了引用资源
                try {
                    val id = attrValue.substring(1).toInt()

                    val entryName = context.resources.getResourceEntryName(id)
                    val typeName = context.resources.getResourceTypeName(id)

                    val mSkinAttr = SkinAttrFactory.createSkinAttr(attrName, id, entryName, typeName)

                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        return viewAttrs
    }

    fun applySkin() {
        mSkinItems.forEach {
            if (it.view != null) {
                it.apply()
            }
        }
    }

    fun clean() {
        mSkinItems?.forEach {
            it.clean()
        }
        mSkinItems.clear()
    }
}