package com.lukelorusso.presentation.ui.v3.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.theme.AppTheme
import com.lukelorusso.presentation.ui.v3.base.AppCardDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsDialogFragment : AppCardDialogFragment(
    isFullScreen = true,
    isLocked = false
) {
    //region PROPERTIES
    private val viewModel: SettingsViewModel by viewModel()

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
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        (activity as? MainActivity)?.applyImmersiveMode()
    }

    companion object {
        val TAG: String = this::class.java.simpleName

        fun newInstance(): SettingsDialogFragment = SettingsDialogFragment()
    }
}
