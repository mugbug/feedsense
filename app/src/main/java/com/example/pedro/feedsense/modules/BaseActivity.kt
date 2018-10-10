package com.example.pedro.feedsense.modules

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.models.Alert
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity : FragmentActivity() {

    private var simpleDialog: AlertDialog? = null
    val disposeBag = CompositeDisposable()

    override fun onPause() {
        super.onPause()
        disposeBag.clear()
    }

    fun showSimpleDialog(alert: Alert) {
        val dummyListener = DialogInterface.OnClickListener { _, _ -> }
        showSimpleDialog(alert.title, alert.message, alert.buttonText, alert.isCancelable, dummyListener)
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