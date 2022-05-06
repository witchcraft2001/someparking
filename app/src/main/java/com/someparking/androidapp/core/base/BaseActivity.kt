package com.someparking.androidapp.core.base

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.someparking.androidapp.R
import com.someparking.androidapp.core.interfaces.RequestMessage
import com.someparking.androidapp.core.interfaces.RequestProgress
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.ui.ProgressLayout
import dagger.android.support.DaggerAppCompatActivity

open class BaseActivity: DaggerAppCompatActivity(), RequestMessage, RequestProgress {

    private var progressLayout: ProgressLayout? = null

    override fun showMessage(message: Message) {
        when (message) {
            is Message.Dialog -> showMessageDialog(message.title, message.message)
            is Message.Toast -> showToast(message.message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }

    fun showMessageDialog(title: String?, message: String) {
        val dialog = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.btn_ok)) {
                    dialog, _ -> dialog.dismiss()
            }
        if (title.isNullOrEmpty().not()) {
            dialog.setTitle(title)
        }
        dialog.show()
    }

    override fun setProgressVisibility(isVisible: Boolean) {
        if (isVisible) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    @Synchronized
    override fun showProgress() {
        runOnUiThread {
            if (progressLayout == null) {
                progressLayout = ProgressLayout(this)
            }
            progressLayout?.let {
                if (!it.isVisible() && !isFinishing) {
                    it.showProcessLayout()
                }
            }
        }
    }

    @Synchronized
    override fun hideProgress() {
        runOnUiThread{
            progressLayout?.hideProcessLayout()
        }
    }
}