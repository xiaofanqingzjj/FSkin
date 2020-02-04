package com.tencent.fskin.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tencent.fskin.demo.base.MenuFragment

class MyFragment : MenuFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addMenu("设置皮肤") {
            startActivity(Intent(activity, ChangeSkinActivity::class.java))
        }

    }

}