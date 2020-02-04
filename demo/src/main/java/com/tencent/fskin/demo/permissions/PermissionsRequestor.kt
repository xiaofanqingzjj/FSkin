package com.tencent.fskin.demo.permissions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log


/**
 * 一个动态申请Android敏感权限的类
 *
 * @author fortunexiao
 */
class PermissionsRequestor {

    companion object {
        const val TAG = "PermissionsRequestor"
    }

    private var fragmentManager: FragmentManager

    constructor(activity: FragmentActivity) {
        fragmentManager = activity.supportFragmentManager
    }

    constructor(fragment: Fragment) {
        fragmentManager = fragment.childFragmentManager
    }




    /**
     * 执行一段需要权限的代码
     */
    fun doWithPermissions(permissions: Array<String>, action: ()->Unit) {
        if (permissions.isNullOrEmpty()) {
            action.invoke()
            return
        }

        request(permissions = permissions, onGranted = {
            action.invoke()
        })
    }

    /**
     * 这个方法主要是用在当用户调用来request请求权限，但用户选择了拒绝权限，如果这是一个必要权限，你可能需要弹出一个提示框让用户继续开启权限
     *
     * 回调函数继续复用原来request里的回调参数
     */
    fun requestDirect(permissions: Array<String>) {
        val fragment = findFragment()
        fragment?.requestPermissionsDirect(permissions)
    }

    /**
     * 请求某一组敏感权限
     *
     * @param permissions 权限列表
     * @param isShowRationale 申请权限之前是否显示权限使用说明
     * @param onShowRationale 弹权限说明回调
     * @param onGranted 正确授权
     * @param onDeny 用户选择了拒绝
     *
     */
    @JvmOverloads fun request(permissions: Array<String>,
                              isShowRationale: Boolean = false,
                              onShowRationale: ((request: PermissionRequest)->Unit)? = null,
                              onGranted:(()->Unit)? = null,
                              onDeny: ((withNeverAskAgain: Boolean)->Unit)? = null) {

        if (permissions.isNullOrEmpty()) { // 如果是空列表直接授权
            onGranted?.invoke()
            return
        }

        val fragment = getPermissionsFragment()

        fragment.onShowRationale = onShowRationale
        fragment.onGranted = onGranted
        fragment.onDeny = onDeny
//        fragment.onDenyAndNeverAskAgain = onDenyAndNeverAskAgain

        fragment.requestPermissions(permissions, isShowRationale)
    }

    private fun getPermissionsFragment(): PermissionProxyFragment {
        var permissionsFragment= findFragment()
        Log.d(TAG, "getPermissionsFragment$permissionsFragment")
        if ( permissionsFragment == null) {
            permissionsFragment = PermissionProxyFragment()

            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
        }
        return permissionsFragment
    }

    private fun findFragment(): PermissionProxyFragment? {
//        val fragmentManager: FragmentManager = activity.supportFragmentManager
        return fragmentManager.findFragmentByTag(FRAGMENT_TAG) as PermissionProxyFragment?
    }

    /**
     * 释放
     *
     *
     */
    fun release() {
        try {
            findFragment()?.run {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitNow()
            }

        } catch (e: Exception) {
            // ignore
        }
    }


    private  val FRAGMENT_TAG = "PermissionsFragment"

    interface PermissionRequest {
        fun proceed()
        fun cancel()
    }
}
