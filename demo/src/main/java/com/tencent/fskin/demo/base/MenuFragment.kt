package com.tencent.fskin.demo.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tencent.fskin.demo.R
import com.tencent.fskin.demo.base.adapter.quickAdapter
import kotlinx.android.synthetic.main.fragment_menu.*


/**
 * A simple menu Activity
 *
 * @author fortune
 */
open class MenuFragment: Fragment() {

    private var menus = mutableListOf<Menu>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_menu, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.quickAdapter(
                data = menus,
                itemLayoutId = R.layout.rv_item_fragment_menu,
                bindData = {_, data, itemView ->
                    itemView?.run {
                        (data as? Menu)?.run {
                            val textView = findViewById<TextView>(R.id.text)
                            textView.text = name
                            setOnClickListener {
                                click?.invoke()
                            }
                        }
                    }
                }
        )
    }



    /**
     * add menu
     *
     * @param name menu title
     * @param click menu click listener
     */
    open fun addMenu(name: String?, click:(()->Unit)? = null) {
        menus.add(Menu(name, click))
        recycler_view?.adapter?.notifyDataSetChanged()
    }

    /**
     * Add menu
     *
     * @param name menu title
     * @param targetClazz menu jump activity class
     */
    fun addMenu(name: String?, targetClazz: Class<out Activity>) {
        menus.add(Menu(name) {
          startActivity(Intent(activity, targetClazz))
        })

        recycler_view?.adapter?.notifyDataSetChanged()
    }

    /**
     * Add menu by fragment
     *
     * @param name menu title
     * @param targetClazz menu jump fragment class
     */
    fun addMenuByFragment(name: String?, targetClazz: Class<out Fragment>) {
//        menus.add(Menu(name) {
//            FragmentContainerActivity.show(activity!!, name, targetClazz)
//        })
//
//        recycler_view?.adapter?.notifyDataSetChanged()
    }

    /**
     * Menu
     */
    data class Menu(var name: String? = null, var click: (()->Unit)? = null)

}