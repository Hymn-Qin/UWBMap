package com.zzdc.uwb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.qxj.conclusion.CustomView.CustomDialog.DialogFragmentHelper
import com.qxj.conclusion.CustomView.CustomDialog.IDialogResultListener
import com.zzdc.uwb.AppConfig.UWBConfig
import com.zzdc.uwb.Utils.CleanDataUtil

open class BasicActivity : Activity() {

    private val TAG = BasicActivity::class.java.name

    open var LHG: Double = UWBConfig.user_lng.toDouble()//当前纬度
    open var LAT: Double = UWBConfig.user_lat.toDouble()//当前经度

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)
    }

    fun showPermissionDialog(message: String) {
        showAlertDialog("权限申请", message, "取消", "去开启", false, {
            CleanDataUtil.clearAppUserData(this)
        }
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

    }

    private fun showAlertDialog(title: String, message: String, leftStr: String?, rightStr: String?, cancelable: Boolean,
                                leftListener: ((Boolean) -> Unit)?,
                                rightListener: ((Boolean) -> Unit)?) {
        DialogFragmentHelper().showInsertDialog(fragmentManager, DialogFragmentHelper.INSERT_ANIMATION_01,
                title, message, leftStr, rightStr, object : IDialogResultListener<String> {
            override fun onDataResult(result: String) {
                Log.d(TAG, "onClick $result")
                when (result) {
                    leftStr -> {
                        leftListener?.let { it(false) }
                    }
                    rightStr -> {
                        rightListener?.let { it(true) }
                    }
                }
            }
        }, cancelable)
    }
}
