package com.qxj.conclusion.CustomView.CustomDialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.StyleRes
import android.util.Log
import android.view.*
import com.zzdc.uwb.R

class CustomDialogFragment: DialogFragment(){

    private val TAG = CustomDialogFragment::class.java.simpleName

    private lateinit var mCancelListener: OnDialogCancelListener

    private fun isCancelListener(): Boolean= ::mCancelListener.isInitialized

    private var INSERT_ANI = R.style.Base_Animation

    private var type: String = "DIALOG"
    
    interface OnDialogCancelListener {
        fun onCancel()
    }

    interface OnCallDialog {
        fun getDialog(context: Context): Dialog
    }

    companion object {
        var mOnCallDialog: OnCallDialog? = null
        val DIALOG = "DIALOG"
        val DOWN_MENU = "DOWN_MENU"
        val UP_MENU = "UP_MENU"

        fun newInstance(type: String, resId: Int, call: OnCallDialog, cancelable: Boolean) : CustomDialogFragment {
            return newInstance(type, resId, call, cancelable, null)
        }

        fun newInstance(resId: Int?, call: OnCallDialog, cancelable: Boolean, cancelListener: OnDialogCancelListener?) : CustomDialogFragment {
            return newInstance(null, resId, call, cancelable, null)
        }

        fun newInstance(type: String?, @StyleRes resId: Int?, call: OnCallDialog, cancelable: Boolean, cancelListener: OnDialogCancelListener?) : CustomDialogFragment {
            val instance = CustomDialogFragment()
            if (null != type)instance.type = type
            if (null != resId) instance.INSERT_ANI = resId
            instance.isCancelable = cancelable
            if (null != cancelListener) instance.mCancelListener = cancelListener
            this.mOnCallDialog = call
            return instance
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return mOnCallDialog!!.getDialog(activity)
    }


    @SuppressLint("ObsoleteSdkInt", "NewApi")
    override fun onStart() {
        super.onStart()

        val dm = context.resources.displayMetrics
        val height = dm.heightPixels
        val width = dm.widthPixels

        val configuration: Configuration = context.resources.configuration
        val ori: Int = configuration.orientation //屏幕状态

        val window = dialog.window
        //消除白边
//        window.setBackgroundDrawable(BitmapDrawable())
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.setWindowAnimations(INSERT_ANI)
        val windowParams: WindowManager.LayoutParams = window.attributes

        when(type) {
            DIALOG -> {
                when(ori) {
                    Configuration.ORIENTATION_LANDSCAPE -> windowParams.width = (height * 0.9).toInt()
                    Configuration.ORIENTATION_PORTRAIT -> windowParams.width = (width * 0.9).toInt()
                }
                windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                windowParams.dimAmount = 0.5f //遮罩透明度
                windowParams.gravity = Gravity.CENTER
            }
            DOWN_MENU -> {
                when(ori) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        windowParams.width = (height * 0.9).toInt()
//                        windowParams.height = (width * 0.8).toInt()
                    }
                    Configuration.ORIENTATION_PORTRAIT -> {
                        windowParams.width = (width * 0.9).toInt()
//                        windowParams.height = (height * 0.8).toInt()
                    }
                }
                windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                windowParams.dimAmount = 0.5f //遮罩透明度
                windowParams.gravity = Gravity.CENTER
            }
            UP_MENU -> {
                when(ori) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        windowParams.width = height
                        windowParams.height = (width * 0.2).toInt()
                    }
                    Configuration.ORIENTATION_PORTRAIT -> {
                        windowParams.width = width
                        windowParams.height = (height * 0.2).toInt()
                    }
                }
                windowParams.dimAmount = 0f //遮罩透明度
                windowParams.gravity = Gravity.TOP
            }

        }
        Log.e(TAG, "type : $type  anim : $INSERT_ANI")
        window.attributes = windowParams
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        if (isCancelListener()) mCancelListener.onCancel()
    }
}