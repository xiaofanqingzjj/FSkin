package com.tencent.fskin.demo.base.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


/**
 * 一个快捷的创建Adapter的类
 *
 * @author fortune
 */
open class QuickAdapter<T>

        /**
         *
         * @param context
         * @param data 数据列表
         * @param itemLayoutId ItemLayoutId
         * @param bindData 数据绑定到ItemView上
         */
        constructor(context: Context, data: List<T>, @LayoutRes private var itemLayoutId: Int, private var bindData: ((position: Int, data: T, itemView: View?)->Unit)? = null) : BaseViewTypeAdapter<T>(context, data) {

    init {
        setViewTypeViewHolderHook(object : BaseViewTypeAdapter.AbsViewTypeViewHolderHook<T>() {
            override fun onViewHoldPostCreate(holder: ViewTypeViewHolder<T>?) {
                holder?.setContentView(itemLayoutId)
            }

            override fun onViewHolderBindData(holder: ViewTypeViewHolder<T>?, data: T) {
                bindData(data, holder)
            }
        })
    }


    open fun bindData(data: T, viewHolder: ViewTypeViewHolder<T>?) {
        bindData?.invoke(viewHolder?.currentBindPosition ?: 0, data, viewHolder?.itemView)
    }



    fun getViewByPosition(recyclerView: RecyclerView?, position: Int, @IdRes viewId: Int): View? {
        return recyclerView?.findViewHolderForLayoutPosition(position)?.itemView?.findViewById(viewId)
    }
}


/**
 * 快速给RecyclerView设置一组数据
 */
fun <T> RecyclerView.quickAdapter(data: List<T>, @LayoutRes itemLayoutId: Int, bindData: ((pos: Int, data: T, itemView: View?)->Unit)? = null): RecyclerView.Adapter<*>? {

    this.adapter = QuickAdapter(
        context = context,
        data = data,
        itemLayoutId = itemLayoutId,
        bindData = bindData)

    layoutManager = object : LinearLayoutManager(context) {
        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    return this.adapter
}