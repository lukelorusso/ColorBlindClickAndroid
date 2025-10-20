package com.lukelorusso.presentation.ui.imagepicker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.ui.preview.PreviewDialogFragment
import com.lukelorusso.presentation.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagePickerActivity : AppCompatActivity() {
    private val viewModel: ImagePickerViewModel by viewModel()
    private val errorMessageFactory by inject<ErrorMessageFactory>()
    private val uri: Uri?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(EXTRA_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_URI)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initRouter(
            this,
            fragment = null
        ) // If there's a router, initialize it here

        uri?.let {
            setContent {
                AppTheme {
                    ImagePicker(
                        it,
                        viewModel,
                        errorMessageFactory
                    )
                }
            }
        } ?: onBackPressedDispatcher.onBackPressed()
    }

    fun showColorPreviewDialog(serializedColor: String) =
        PreviewDialogFragment.newInstance(serializedColor)
            .show(supportFragmentManager, PreviewDialogFragment.TAG)

    companion object {
        const val EXTRA_URI = "EXTRA_URI"

        fun newIntent(context: Context, imageUri: Uri): Intent =
            Intent(context, ImagePickerActivity::class.java).apply {
                putExtra(EXTRA_URI, imageUri)
            }
    }
}
