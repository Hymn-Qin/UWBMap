package com.zzdc.uwb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fengmap.android.FMMapSDK
import com.zzdc.uwb.AMap.AMapStartActivity
import com.zzdc.uwb.Utils.PermissionUtil

class ConductStartActivity : BasicActivity() {

    val TAG = ConductStartActivity::class.java.simpleName

    var permissionUtil = PermissionUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_main)
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "fragment size = " + fragmentManager.fragments.size)
        if (fragmentManager.fragments.size == 0) {
            permissionUtil.checkEssentialPermission(Runnable {
                FMMapSDK.init(this)
                Log.d(TAG, "storage permission success")
                val intent = Intent()
                intent.setClass(this, AMapStartActivity::class.java)
                startActivity(intent)
                finish()
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
