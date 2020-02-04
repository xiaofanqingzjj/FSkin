package com.tencent.fskin.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import cn.feng.skin.manager.entity.DynamicAttr
import cn.feng.skin.manager.listener.IDynamicNewView

open class BaseFragment : Fragment() {
//    private var mIDynamicNewView: IDynamicNewView? = null
//    private val mLayoutInflater: LayoutInflater? = null
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mIDynamicNewView = try {
//            context as IDynamicNewView
//        } catch (e: ClassCastException) {
//            null
//        }
//    }
//
//    override fun dynamicAddView(view: View, pDAttrs: List<DynamicAttr>) {
//        if (mIDynamicNewView == null) {
//            throw RuntimeException("IDynamicNewView should be implements !")
//        } else {
//            mIDynamicNewView!!.dynamicAddView(view, pDAttrs)
//        }
//    }

//    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
//
//        return activity?.layoutInflater ?: super.onGetLayoutInflater(savedInstanceState)
//    }
}