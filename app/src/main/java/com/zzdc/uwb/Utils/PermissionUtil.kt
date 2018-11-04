package com.zzdc.uwb.Utils

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.zzdc.uwb.BasicActivity

class PermissionUtil(private val context: BasicActivity) {
    private var mHasPermissionRunnable: Runnable? = null
    private var mNoPermissionRunnable: Runnable? = null
    private var REQUEST_CODE_PERMISSION = 1000

    private val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun checkEssentialPermission(hasPermissionDo: Runnable) {
        val permission = arrayOf(READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, Runnable {
            context.showPermissionDialog("在设置-应用-UWB-权限中开启存储空间和定位权限，以正常使用定位功能")
        })
    }

    private fun checkPermission(permissions: Array<out String>, hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        mHasPermissionRunnable = null
        mNoPermissionRunnable = null
        when {
            isPermissionsGranted(permissions) -> hasPermissionDo.run()
            ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0]) -> noPermissionDo.run()
            else -> {
                mHasPermissionRunnable = hasPermissionDo
                mNoPermissionRunnable = noPermissionDo
                ActivityCompat.requestPermissions(context, permissions, REQUEST_CODE_PERMISSION)
            }
        }
    }


    private fun isPermissionsGranted(permissions: Array<out String>): Boolean {
        for (it in permissions) {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun isAllGranted(grantResults: IntArray): Boolean {
        for (it in grantResults) {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (isAllGranted(grantResults)) mHasPermissionRunnable?.run()
            else mNoPermissionRunnable?.run()
        }
    }


}