package com.tencent.fskin.demo

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tencent.fskin.SkinManager
import com.tencent.fskin.demo.base.BaseActivity
import com.tencent.fskin.demo.base.FragmentTabHost
import com.tencent.permissionsrequestor.PermissionsRequestor
import kotlinx.android.synthetic.main.view_home_tab_indicator.view.*

class MainActivity : BaseActivity() {

    private lateinit var tabHost: FragmentTabHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        hideBackBtn()

        setTitle("换肤Demo")

        tabHost = findViewById(android.R.id.tabhost)

        tabHost.setup(this, supportFragmentManager, android.R.id.tabcontent)

        setupTabs()

        //		PermissionsRequestor(this).request(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INSTALL_PACKAGES))
        PermissionsRequestor(this).request(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun setupTabs() {

        addTab("1", "首页", R.drawable.selector_tabbar_book, HomeFragment::class.java)
//        addTab("2", "世界", R.drawable.selector_tabbar_world, SimpleEmptyFragment::class.java)
        addTab("3", "个人中心", R.drawable.selector_tabbar_my, MyFragment::class.java)

    }

    private fun addTab(tag: String, text: String?, icon: Int, clazz: Class<out Fragment>) {

        val view = layoutInflater.inflate(R.layout.view_home_tab_indicator, null)

        view.iv_icon.setImageResource(icon)

//        view.iv_icon.setImageResource(R.color.SC1)

        SkinManager.addSkinAttr(this, view.iv_icon, "src", icon)

        view.tv_text.text = text

        tabHost.addTab(tabHost.newTabSpec(tag).setIndicator(view), clazz, null)
    }

}