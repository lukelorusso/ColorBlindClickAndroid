package com.lukelorusso.presentation.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lukelorusso.presentation.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun View.setAlphaWithAnimation(
    start: Float,
    end: Float,
    duration: Long? = null,
    animationEnd: (() -> Unit)? = null
) = (start to end).applyFloatAnimation(duration = duration, animationEnd = animationEnd) {
    alpha = it
}

fun ImageView?.load(url: String?, onError: (() -> Unit)? = null, onSuccess: (() -> Unit)? = null) {
    url?.also { imageUrl ->
        this?.also { imageView ->
            Picasso.get().load(imageUrl).into(imageView, object : Callback {
                override fun onSuccess() {
                    onSuccess?.invoke()
                }

                override fun onError(e: Exception?) {
                    onError?.invoke()
                }
            })
        }
    }
}

fun View.showKeyboard() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.also {
        it.showSoftInput(this, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.also {
        it.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun EditText.actionDone(callback: () -> Unit) {
    setOnKeyListener { _, keyCode, event ->
        // If the event is a key-down event on the "enter" button
        return@setOnKeyListener if (event.action == KeyEvent.ACTION_DOWN &&
            keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            // Perform action on key press
            callback()
            true
        } else false
    }
}

fun EditText.onTextChanged(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            // Nothing
        }

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            // Nothing
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            charSequence?.also { listener(it.toString()) }
        }
    })
}

fun EditText.focusOnLastLetter() {
    setSelection(text.length)
}

fun View.fadeInView(duration: Int? = null) {
    this.animation?.setAnimationListener(null)
    this.animation?.cancel()
    this.clearAnimation()
    val effectDuration = duration ?: resources.getInteger(R.integer.fading_effect_duration_default)
    this.visibility = View.INVISIBLE // setting precondition (just in case)
    this.setAlphaWithAnimation(0F, 1F, effectDuration.toLong()) // setting animation
    if (this is FloatingActionButton) this.show() // custom behaviour
    else this.visibility = View.VISIBLE // making this visible to show the animation to the user
}

fun View.fadeOutView(duration: Int? = null) {
    this.animation?.setAnimationListener(null)
    this.animation?.cancel()
    this.clearAnimation()
    val effectDuration = duration ?: resources.getInteger(R.integer.fading_effect_duration_default)
    this.visibility = View.VISIBLE // setting precondition (just in case)
    this.setAlphaWithAnimation(
        1F,
        0F,
        effectDuration.toLong()
    ) // setting animation with listener at the end (if reached)
    if (this is FloatingActionButton) this.hide() // custom behaviour
    else this.visibility = View.INVISIBLE // making this invisible
}

fun View.getBitmap(): Bitmap {
    val b = Bitmap.createBitmap(
        this.layoutParams.width,
        this.layoutParams.height,
        Bitmap.Config.ARGB_8888
    )
    val c = Canvas(b)
    this.layout(this.left, this.top, this.right, this.bottom)
    this.draw(c)
    return b
}
