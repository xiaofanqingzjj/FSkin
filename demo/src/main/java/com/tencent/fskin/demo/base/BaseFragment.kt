package com.tencent.fskin.demo.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * TIFAFragment
 *
 *
 * Created by fortune
 */
open class BaseFragment : Fragment() {
    /**
     * 获取到Fragment 当前的ContentView
     *
     * @return
     */
    /**
     * 给 TIFAFragment 设置一个View
     *
     * @param view
     */
    /**
     * TIFAFragment 的ContentView
     */
    var contentView: View? = null
    /**
     * Fragment 完成创建
     *
     * @return
     */
    /**
     * 当前TIFAFragment 是否完成创建
     */
    var isCreated = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreated = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentView != null) {
            val vp = contentView!!.parent as ViewGroup
            vp?.removeView(contentView)
        }
        return contentView
    }

    /**
     * 根据 View 的 id 为TIFAFragment 设置一个 View
     *
     * @param id
     */
    fun setContentView(id: Int) {
        val view = View.inflate(activity, id, null)
        contentView = view
    }

    fun onShow() {}
    fun onLeave() {}
    /**
     * 查根据 id 找当前 ContentView 中的 View 对象
     *
     * @param resId
     * @return
     */
    fun findViewById(resId: Int): View? {
        return if (contentView != null) {
            contentView!!.findViewById(resId)
        } else null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }
}