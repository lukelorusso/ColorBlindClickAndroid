package com.lukelorusso.presentation.ui.settings

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AppCardDialogFragment
import com.lukelorusso.presentation.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsDialogFragment : AppCardDialogFragment(
    isFullScreen = true,
    isLocked = false
) {
    //region PROPERTIES
    private val viewModel: SettingsViewModel by viewModel()
    private val errorMessageFactory by inject<ErrorMessageFactory>()

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
                Settings(
                    viewModel = viewModel,
                    errorMessageFactory = errorMessageFactory
                )
            }
        }
    }

    companion object {
        val TAG: String = this::class.java.simpleName

        fun newInstance(): SettingsDialogFragment = SettingsDialogFragment()
    }
}
