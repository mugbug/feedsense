package com.example.pedro.feedsense.modules

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.pedro.feedsense.R
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity : AppCompatActivity() {

    private var simpleDialog: AlertDialog? = null
    val disposeBag = CompositeDisposable()

    override fun onPause() {
        super.onPause()
        disposeBag.clear()
    }

    open fun showLoading() {}

    open fun hideLoading() {}

    fun showSimpleDialog(title: String, message: String, buttonText: String, isCancelable: Boolean = true) {
        val dummyListener = DialogInterface.OnClickListener { _, _ -> }
        showSimpleDialog(title, message, buttonText, isCancelable, dummyListener)
    }

    protected fun showSimpleDialog(title: String, message: String, buttonText: String,
                                   isCancelable: Boolean = true, clickListener: DialogInterface.OnClickListener) {
        simpleDialog?.dismiss()
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(buttonText, clickListener)
        builder.setCancelable(isCancelable)
        simpleDialog = builder.create()
        simpleDialog?.show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}