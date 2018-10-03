package com.example.pedro.feedsense.modules.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.ActivityLoginBinding
import com.example.pedro.feedsense.modules.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.architecture.ext.viewModel
import android.opengl.ETC1.getHeight
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.squareup.picasso.Picasso
import android.view.WindowManager



enum class SlideDirection { LEFT, RIGHT, UP, DOWN }

class LoginActivity: BaseActivity() {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
                this,
                R.layout.activity_login
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        updateBackgroundImage()
    }

    private fun updateBackgroundImage() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        Picasso.get()
                .load("https://source.unsplash.com/featured/?nature,water")
                .fit()
                .centerCrop()
                .into(background_image)
    }

    private fun slide(direction: SlideDirection, view: View, completion: (() -> Unit)? = null) {
        val translX = when(direction) {
            SlideDirection.RIGHT -> view.width.toFloat()
            SlideDirection.LEFT -> -view.width.toFloat()
            else -> 0.0f
        }

        val translY = when(direction) {
            SlideDirection.UP -> view.height.toFloat()
            SlideDirection.DOWN -> -view.height.toFloat()
            else -> 0.0f
        }

        view.animate()
                .translationX(translX)
                .translationY(translY)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        completion?.invoke()
                    }
                })
    }

    fun fade(alpha: Float, view: View, completion: (() -> Unit)? = null) {
        view.animate()
                .alpha(alpha)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        completion?.invoke()
                    }
                })

    }

    fun didTapLogin(view: View) {
        // switch login button to loading
    }

    fun didTapRegister(view: View) {
        slide(SlideDirection.LEFT, login_button) {
            login_button.visibility = View.GONE
            login_button.clearAnimation()
            password_check_field.visibility = View.VISIBLE
            register_buttons_layout.visibility = View.VISIBLE
        }
        fade(0.0f, start_register_button) {
            start_register_button.visibility = View.GONE
            start_register_button
        }
    }

    fun didTapBackToLogin(view: View) {
        fade(1.0f, start_register_button) {
            start_register_button.visibility = View.VISIBLE
        }
        slide(SlideDirection.RIGHT, password_check_field)
        slide(SlideDirection.RIGHT, register_buttons_layout) {
            login_button.visibility = View.VISIBLE

            password_check_field.visibility = View.GONE
            password_check_field.clearAnimation()
            register_buttons_layout.visibility = View.GONE
            register_buttons_layout.clearAnimation()
        }
    }
}