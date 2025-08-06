package com.lukelorusso.presentation.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.isActive
import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraXFragment : Fragment() {
    private val viewModel: CameraViewModel by viewModel()
    private val errorMessageFactory by inject<ErrorMessageFactory>()

    fun onBackPressHandled(): Boolean {
        return when {
            isActive() && isToolbarColorVisible() -> {
                hideToolbarColor()
                true
            }

            else ->
                false
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRouter(
            requireActivity() as AppCompatActivity,
            fragment = this
        ) // If there's a router, initialize it here
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(context = requireContext()).apply {
        setContent {
            AppTheme {
                CameraX(
                    viewModel = viewModel,
                    errorMessageFactory = errorMessageFactory
                )
            }
        }
        (activity as? MainActivity)?.hideSplashScreen()
    }

    private fun isToolbarColorVisible(): Boolean {
        return viewModel.uiState.value.run { contentState.isError || color != null }
    }

    private fun hideToolbarColor() {
        if (isToolbarColorVisible()) {
            viewModel.dismissErrorAndColor()
        }
    }

    fun reloadData() {
        viewModel.reloadData()
    }

    companion object {
        val TAG: String = this::class.java.simpleName

        fun newInstance(): CameraXFragment = CameraXFragment()
    }
}
