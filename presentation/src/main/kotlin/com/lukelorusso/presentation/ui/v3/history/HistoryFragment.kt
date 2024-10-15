package com.lukelorusso.presentation.ui.v3.history

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.lukelorusso.presentation.extensions.isActive
import com.lukelorusso.presentation.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModel()

    fun backPressHandled(): Boolean {
        return when {
            isActive() && viewModel.uiState.value.isSearchingMode -> {
                viewModel.toggleSearchingMode(false)
                true
            }

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
                History(
                    viewModel = viewModel
                )
            }
        }
    }

    fun reloadData() =
        viewModel.loadData()

    companion object {
        val TAG: String = HistoryFragment::class.java.simpleName

        fun newInstance(): HistoryFragment = HistoryFragment()
    }
}
