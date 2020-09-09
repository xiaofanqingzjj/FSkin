package com.tencent.fskin.util

import android.util.Log
import com.tencent.tfkin.BuildConfig


object DLog  {

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }
}