package com.tencent.fskin.demo.permissions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log


/**
 * 一个用来请求权限不可见Fragment，对用户来说是透明的
 *
 * @author fortunexiao
 *
 */
class PermissionProxyFragment : Fragment() {



    /**
     * 给shouldShowRequestPermissionRationale做标识，表示当前处于的情况
     *
     * 请参看：shouldShowRequestPermissionRationale方法
     */
    private var isInOnRequestPermissionsResulting = false

    private var permissionHelper: PermissionHelper? = null

    var permissions: Array<String>? = null
    var isShowRationale: Boolean = false


    // 外部回调
    var onShowRationale: ((request: PermissionsRequestor.PermissionRequest)->Unit)? = null
    var onGranted: (()->Unit)? = null
    var onDeny: ((withNeverAskAgain: Boolean)->Unit)? = null
//    var onDenyAndNeverAskAgain: (()->Unit)? = null


    private val callback = object : PermissionHelper.Callback {
        override fun onShowPermissionsRational(request: PermissionsRequestor.PermissionRequest) {
            onShowRationale?.invoke(request)
        }

        override fun onPermissionGranted() {
            onGranted?.invoke()

            // 如果正常授权，从Activity中移除当前授权
            detach()
        }

        override fun onPermissionDeny(withNeverAskAgain: Boolean) {
            onDeny?.invoke(withNeverAskAgain)
        }

//        override fun onPermissionAndNeverAskAgain() {
//            onDenyAndNeverAskAgain?.invoke()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (pendingRequestPermissions) {
            requestInner()
            pendingRequestPermissions = false
        }
    }

    private fun detach() {
        try {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitNowAllowingStateLoss()
        } catch (e: Exception) {
            // ignore
        }
    }

    /**
     * 直接请求权限
     * 某些场景要用
     */
    fun requestPermissionsDirect(permissions: Array<String>) {
        requestPermissions(permissions, PermissionHelper.REQUEST_CODE_PERMISSION)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(permissions: Array<String>, isShowRationale: Boolean) {
        this.isShowRationale = isShowRationale
        this.permissions = permissions

        // 如果请求权限的时候Fragment还没被添加到Activity，则设置一个Pending标志，等被添加到了Activity之后才启动游戏
        if (isAdded) {
            requestInner()
        } else {
            pendingRequestPermissions = true
        }
    }

    private var pendingRequestPermissions: Boolean = false

    private fun requestInner() {
        if (permissions == null) {
            return
        }

        if (permissionHelper == null) {
            permissionHelper = PermissionHelper(this, callback)
        }
        permissionHelper?.requestPermissions(permissions!!)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        isInOnRequestPermissionsResulting = true

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(requestCode, grantResults)

        isInOnRequestPermissionsResulting = false
    }



    /**
     * 这个方法有2个作用：
     *
     * 1.在请求系统权限之前是否显示权限说明弹框。如果返回true则在弹系统的权限框之前弹一个自己的权限说明框
     * 2.在权限请求之后，如果权限被拒绝，如果该方法返回false表示用户勾选了不再提示勾选框
     *
     * 所以我们现在遇到了一个恶心的问题：
     *
     * 我们希望所有的权限在申请之前都弹出一个权限说明框，所以我们重写这个方法，强制返回true，这样就使得所有的权限都会弹权限说明。
     *
     * 但是这个方法还有一个功能是表示用户是否勾选了"不再提示"勾选，所以如果强制返回true之后那么PermissionDispatcher框架就没办法监听到是否勾选了"不再提示"框。
     *
     * 解决办法：
     *
     * 因为权限说明是在请求权限的时候使用的.
     * 而"不在提示"是在onRequestPermissionsResult里判断的,所以我们在onRequestPermissionResult增加一个标志，用来告诉shouldShowRequestPermissionRationale方法，当前这次调用的目的。
     *
     * @param permission
     * @return
     */
    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        var result = super.shouldShowRequestPermissionRationale(permission)
        Log.d("PermissionDispatcher", "shouldShowRequestPermissionRationale super:$result")

        if (!isInOnRequestPermissionsResulting) { // 为true的时候当前调用该方法是用来判断用户是否勾选了"不再提示"，false的时候表示询问当前是否弹权限说明
            result = isShowRationale
        }

        Log.d("PermissionDispatcher", "shouldShowRequestPermissionRationale return :$result")
        return result
    }

}