package com.tencent.fskin.demo.base

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * 一个用测试的占位Fragment
 * Created by fortunexiao on 2018/7/23.
 */
open class SimpleEmptyFragment : BaseFragment() {


    companion object {
        const val PARAM_TEXT = "text"

        fun instance(text:String) : SimpleEmptyFragment {
            return SimpleEmptyFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_TEXT, text)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return NestedScrollView(activity!!).apply { FrameLayout(activity!!).apply {
            addView(TextView(activity).apply {
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER
                }
                text = arguments?.getString(PARAM_TEXT) ?: "这是一个占位Fragment"
            })
        } }
    }
}