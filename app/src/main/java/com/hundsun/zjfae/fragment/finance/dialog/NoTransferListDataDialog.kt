package com.hundsun.zjfae.fragment.finance.dialog

import android.view.View
import android.widget.Button
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.view.dialog.BaseDialogFragment

class NoTransferListDataDialog : BaseDialogFragment() {

    override fun initView() {
        findViewById<View>(R.id.tv_re).setOnClickListener {
            dismissDialog()
        }

    }

    override fun getLayoutId(): Int {

        return R.layout.dialog_no_transfer_data_layout
    }

    /**
     * 设置是否可以cancel
     * @return
     */
    override fun isCancel(): Boolean {

        return false
    }
}