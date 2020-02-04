package com.tencent.fskin.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tencent.fskin.SkinManager
import com.tencent.fskin.demo.base.BaseActivity
import com.tencent.fskin.demo.base.MenuFragment

class ChangeSkinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().add(R.id.fl_content, ChangeSkinFragment()).commitAllowingStateLoss()
    }


    class ChangeSkinFragment(): MenuFragment() {


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            addMenu("恢复默认") {
                SkinManager.restoreDefaultTheme()
            }

            addMenu("黑色主题") {
                SkinManager.applySkin("/sdcard/black.skin", object : SkinManager.ILoaderListener {
                    override fun onStart() {
                    }

                    override fun onSuccess() {
                        Toast.makeText(context, "皮肤切换成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailed(reason: String?) {
                        Toast.makeText(context, "皮肤切换失败$reason", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }
}