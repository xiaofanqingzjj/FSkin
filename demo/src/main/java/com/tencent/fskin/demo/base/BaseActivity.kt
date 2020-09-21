package com.tencent.fskin.demo.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.tencent.fskin.demo.R
import kotlinx.android.synthetic.main.activity_base.*


/**
 *
 */
open class BaseActivity: FragmentActivity() {

    companion object {
        const val TAG = "BaseActivity@@@"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)

        btn_back.setOnClickListener {
            finishAfterTransition()
        }

        val layoutInflaterFromFrom = LayoutInflater.from(this)
        val layoutInflaterFromGetService = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val fromActivity = layoutInflater

        Log.d(TAG, "context:$this, \nfrom:$layoutInflaterFromFrom\n" +
                "getService:$layoutInflaterFromGetService\n" +
                "fromActivity:$fromActivity")
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