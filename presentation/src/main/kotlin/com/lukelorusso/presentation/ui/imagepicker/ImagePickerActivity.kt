package com.lukelorusso.presentation.ui.imagepicker

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lukelorusso.presentation.ui.theme.AppTheme

class ImagePickerActivity : ComponentActivity() {
    val uri: Uri?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(EXTRA_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_URI)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri?.let {
            setContent {
                AppTheme {
                    ImagePicker(it)
                }
            }
        } ?: onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        const val EXTRA_URI = "EXTRA_URI"
    }
}
