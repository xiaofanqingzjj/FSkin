package com.tencent.fskin.demo.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.support.v4.app.ActivityCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v4.util.SimpleArrayMap

internal object PermissionUtils {
    // Map of dangerous permissions introduced in later framework versions.
    // Used to conditionally bypass permission-hold checks on older devices.
    private val MIN_SDK_PERMISSIONS: SimpleArrayMap<String, Int> = SimpleArrayMap(8)

    init {
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23)
    }

    fun verifyPermissions(vararg grantResults: Int): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun permissionExists(permission: String): Boolean {
        val minVersion = MIN_SDK_PERMISSIONS.get(permission)
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion
    }

    fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false
            }
        }
        return true
    }

    private fun hasSelfPermission(context: Context, permission: String): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && "Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
//            return hasSelfPermissionForXiaomi(context, permission)
//        }

        return try {
            checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (t: RuntimeException) {
            false
        }

    }

    private fun hasSelfPermissionForXiaomi(context: Context, permission: String): Boolean {
        val permissionToOp = AppOpsManagerCompat.permissionToOp(permission)
                ?: // in case of normal permissions(e.g. INTERNET)
                return true
        val noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.packageName)
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    fun shouldShowRequestPermissionRationale(fragment: Fragment, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }
}
