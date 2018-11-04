package com.qxj.conclusion.CustomView.CustomDialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import com.qxj.conclusion.CustomView.CustomDialog.CustomDialogFragment.Companion.newInstance
import com.zzdc.uwb.R
import kotlinx.android.synthetic.main.dialog_fragment_alert.view.*
import kotlinx.android.synthetic.main.dialog_alert_message.view.*
import kotlinx.android.synthetic.main.dialog_alert_select_o.view.*
import kotlinx.android.synthetic.main.dialog_alert_select_t.view.*
import kotlinx.android.synthetic.main.dialog_alert_title.view.*

class DialogFragmentHelper {
    private val TAG: String = DialogFragmentHelper::class.java.name

    private val PROGRESS_THEME = R.style.Base_AlertDialog

    private val PROGRESS_TAG = TAG + ":progress"

    companion object {
        var INSERT_ANIMATION_01 = R.style.Base_Animation
    }

    private val INSERT_THEME = R.style.Base_AlertDialog

    private val INSERT_TAG = TAG + ":insert"

    fun showInsertDialog(fragmentManager: FragmentManager, animation: Int?, title: String, msg: String, leftString: String?, rightString: String?, resultListener: IDialogResultListener<String>, cancelable: Boolean) {
        val dialogFragment: CustomDialogFragment = newInstance(animation, object : CustomDialogFragment.OnCallDialog {
            @SuppressLint("InflateParams")
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_alert, null)

                val dialog = builder.create()
                dialog.show()
                //标题
                view.tv_alert_title.layoutResource = R.layout.dialog_alert_title
                view.tv_alert_title.inflate()
                view.tv_dialog_title.text = title
                //信息
                view.tv_alert_message.layoutResource = R.layout.dialog_alert_message
                view.tv_alert_message.inflate()
                view.tv_dialog_message.text = msg
                //选项
                if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                    view.tv_alert_select.layoutResource = R.layout.dialog_alert_select_t
                    view.tv_alert_select.inflate()
                    view.tv_alert_negative.text = leftString
                    view.tv_alert_positive.text = rightString

                    view.tv_alert_negative.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(leftString!!)
                    }
                    view.tv_alert_positive.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(rightString!!)
                    }

                } else if (!TextUtils.isEmpty(rightString)) {
                    view.tv_alert_select.layoutResource = R.layout.dialog_alert_select_o
                    view.tv_alert_select.inflate()
                    view.tv_alert_positive_only.text = rightString

                    view.tv_alert_positive_only.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(rightString!!)
                    }
                }

                dialog.setContentView(view)

                return dialog

                /**原生**/
//                val builder: AlertDialog.Builder = AlertDialog.Builder(context, INSERT_THEME)
//                builder.setTitle(title)
//                builder.setMessage(msg)
//
//
//                builder.setPositiveButton(rightString) { _, _ ->
//                    resultListener.onDataResult(rightString)
//                }
//                builder.setNegativeButton(leftString) { _, _ ->
//                    resultListener.onDataResult(leftString)
//                }
//                return builder.create()
            }
        }, cancelable, null)
        dialogFragment.show(fragmentManager, INSERT_TAG)
    }
}