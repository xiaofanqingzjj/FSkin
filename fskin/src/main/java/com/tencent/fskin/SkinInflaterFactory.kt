package com.tencent.fskin

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.tencent.tfkin.BuildConfig


/**
 * 一个LayoutInflater Factory
 *
 * 拦截所有XML中的View创建，检查View是否有支持换肤的属性，如果有的话，
 *
 * @author fortunexiao
 */
class SkinInflaterFactory : LayoutInflater.Factory {

    companion object {
        private const val DEBUG = true

        const val TAG = "SkinInflaterFactory@@"

        private val sClassPrefixList = arrayOf(
                "android.view.",
                "android.widget.",
                "android.webkit.",
                "android.app."
        )
    }

    /**
     * Store the view item that need skin changing in the activity
     */
    internal val mSkinItems: MutableMap<View, SkinElement> = mutableMapOf()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? { // if this is NOT enable to be skined , simplly skip it

        // 先收集属性，看是否有换肤支持的属性，如果没有，则不拦截
        val skinAttrs = parseSkinAttr(context, attrs)

        if (skinAttrs.isEmpty()) {
            return null
        }

        // 拦截View的创建
        val view = createViewByOrgLayoutInflate(context, name, attrs)

        if (view != null && skinAttrs.isNotEmpty()) {

            val element = SkinElement(view, skinAttrs)

            // 初始换肤
            element.initApply(SkinManager.skinResourcesProxy)

            mSkinItems[view] = element

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onCreateView context:$context, name:$name, skinItem:${mSkinItems[view]}")
            }

        }
        return view
    }

    private fun createViewByOrgLayoutInflate(context: Context, name: String, attrs: AttributeSet): View? {

        val layoutInflater = LayoutInflater.from(context)
        var view: View? = null
        if (-1 == name.indexOf('.')) { // 不是全路径，表示是系统自带的widget
            for (prefix in sClassPrefixList) {
                try {
                    view = layoutInflater.createView(name, prefix, attrs)
                    if (view != null) {
                        break
                    }
                } catch (e: ClassNotFoundException) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }
        } else {
            view = layoutInflater.createView(name, null, attrs)
        }
        return view
    }

    private fun parseSkinAttr(context: Context, attrs: AttributeSet) :  ArrayList<SkinElementAttr>{
        val viewAttrs: ArrayList<SkinElementAttr> = ArrayList()

        // 循环所有的xml属性
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)

            // 看属性是否是支持换肤的属性
            if (!SkinElementAttrFactory.isSupportedAttr(attrName)) {
                continue
            }

            if (attrValue.startsWith("@")) { // 只有@开头的才表示用了引用资源
                try {
                    val id = attrValue.substring(1).toInt()

                    val entryName = context.resources.getResourceEntryName(id)
                    val typeName = context.resources.getResourceTypeName(id)

                    val mSkinAttr = SkinElementAttrFactory.createSkinAttr(attrName, id, entryName, typeName)

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
            it.value.apply(SkinManager.skinResourcesProxy)
        }
    }

    fun clean() {
        mSkinItems?.forEach {
            it.value.clean()
        }
        mSkinItems.clear()
    }
}