package com.lukelorusso.colorblindclick.presentation.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.lukelorusso.colorblindclick.presentation.error.ErrorMessageFactory
import com.lukelorusso.colorblindclick.presentation.extensions.isActive
import com.lukelorusso.colorblindclick.presentation.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : Fragment() {
    private val viewModel: InfoViewModel by viewModel()
    private val errorMessageFactory by inject<ErrorMessageFactory>()

    fun onBackPressHandled(): Boolean {
        return when {
            isActive() -> {
                viewModel.gotoCamera()
                true
            }

            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRouter(
            activity = requireActivity(),
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
                Info(
                    viewModel = viewModel,
                    errorMessageFactory = errorMessageFactory
                )
            }
        }
    }

    override fun onDestroy() {
        viewModel.clearRouter()
        super.onDestroy()
    }

    companion object {
        val TAG: String = this::class.java.simpleName

        fun newInstance(): InfoFragment = InfoFragment()
    }
}
