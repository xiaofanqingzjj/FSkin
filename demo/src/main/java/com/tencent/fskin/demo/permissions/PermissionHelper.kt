package com.tencent.fskin.demo.permissions

import android.support.v4.app.Fragment
import java.lang.ref.WeakReference


/**
 * 一个处理权限的辅助类
 * @author fortunexiao
 */
internal class PermissionHelper(var fragment: Fragment, var callback: Callback) {

    companion object {
        const val REQUEST_CODE_PERMISSION = 999
    }

    private var permissions: Array<String>? = null


    /**
     * 请求一组权限
     */
    fun requestPermissions(permissions: Array<String>) {
        if (fragment.activity == null) {
            return
        }

        this.permissions = permissions

        // 先看要申请的权限是否已经授权
        if (PermissionUtils.hasSelfPermissions(fragment.activity!!, *permissions)) {
            callback.onPermissionGranted() // 直接调用已经授权回调
        } else {

            // 如果没有授权的话再看是否要给用户弹权限说明
            if (PermissionUtils.shouldShowRequestPermissionRationale(fragment, *permissions)) { // 判断是否需要弹原理框

                // 弹原理框
                callback.onShowPermissionsRational(Holder(permissions, fragment, callback))
            } else {

                // 申请权限，等待onRequestPermissionsResult
                fragment.requestPermissions(permissions, REQUEST_CODE_PERMISSION)
            }
        }
    }

    /**
     * 申请权限回调
     */
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {

        if (permissions == null) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (PermissionUtils.verifyPermissions(*grantResults)) { // 是否正确授权
                    callback.onPermissionGranted()

                } else { // 如果没有正确授权，判断用户是否勾选了不再提示，判断的方法为Activity的shouldShowRequestPermissionRationale方法

                    if (!PermissionUtils.shouldShowRequestPermissionRationale(fragment, *permissions!!)) { // 用户勾选了不在提示
                        callback.onPermissionDeny(true) //onPermissionAndNeverAskAgain()
                    } else { // 用户拒绝了权限申请
                        callback.onPermissionDeny(false)
                    }
                }
            }
            else -> {
            }
        }
    }

    /**
     *
     */
    private class Holder(var permissions: Array<String>, fragment: Fragment, callback: Callback) : PermissionsRequestor.PermissionRequest {

        val fragmentRef: WeakReference<Fragment> = WeakReference(fragment)
        val callbackRef: WeakReference<Callback> = WeakReference(callback)

        override fun proceed() {
            val fragment = fragmentRef.get() ?: return
            fragment.requestPermissions(permissions, REQUEST_CODE_PERMISSION)
//            ActivityCompat.requestPermissions(fragment, permissions, REQUEST_CODE_PERMISSION)
        }

        override fun cancel() {
            callbackRef.get()?.onPermissionDeny(false)
        }
    }

    interface Callback {

        fun onShowPermissionsRational(request: PermissionsRequestor.PermissionRequest)

        fun onPermissionGranted()

        fun onPermissionDeny(withNeverAskAgain: Boolean)

//        fun onPermissionAndNeverAskAgain()
    }

}
