package com.tencent.fskin.demo.base

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import com.tencent.fskin.demo.R
import kotlinx.android.synthetic.main.activity_base.*

open class BaseActivity: FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)

        btn_back.setOnClickListener {
            finishAfterTransition()
        }
    }

    fun hideBackBtn() {
        btn_back.visibility = View.GONE
    }

    fun hideActionBar() {
        fl_action_bar.visibility = View.GONE
    }

    fun setTitle(title: String? ) {
        tv_title.text = title
    }

    override fun setContentView(layoutResID: Int) {
        LayoutInflater.from(this).inflate(layoutResID, fl_content)
    }

}