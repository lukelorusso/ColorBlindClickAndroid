package com.lukelorusso.presentation.ui.preview

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import com.lukelorusso.presentation.extensions.build
import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.theme.AppTheme
import com.lukelorusso.presentation.ui.base.AppCardDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewDialogFragment : com.lukelorusso.presentation.ui.base.AppCardDialogFragment(
    isFullScreen = false
) {
    //region PROPERTIES
    private val viewModel: PreviewViewModel by viewModel()
    private val extraSerializedColor by lazy {
        requireArguments().getString(EXTRA_SERIALIZED_COLOR)
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
                Preview(
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData(extraSerializedColor)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        (activity as? MainActivity)?.applyImmersiveMode()
    }

    companion object {
        val TAG: String = this::class.java.simpleName
        private const val EXTRA_SERIALIZED_COLOR = "EXTRA_SERIALIZED_COLOR"

        fun newInstance(
            serializedColor: String
        ): PreviewDialogFragment = PreviewDialogFragment()
            .build { putString(EXTRA_SERIALIZED_COLOR, serializedColor) }
    }
}
