package com.lukelorusso.presentation.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukelorusso.presentation.databinding.DialogFragmentSettingsBinding
import com.lukelorusso.presentation.ui.base.ARenderBottomSheetDialogFragment
import com.lukelorusso.presentation.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsDialogFragment : ARenderBottomSheetDialogFragment<SettingsData>(
    isFullScreen = false
) {

    companion object {
        val TAG: String = SettingsDialogFragment::class.java.simpleName

        fun newInstance(): SettingsDialogFragment =
            SettingsDialogFragment()
    }

    // View
    private lateinit var binding: DialogFragmentSettingsBinding // This property is only valid between onCreateView and onDestroyView
    private val viewModel by viewModel<SettingsViewModel>()

    // Properties
    // TODO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DialogFragmentSettingsBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(
            viewLifecycleOwner,
            dataObserver = { data -> data?.let(::render) },
            eventObserver = { event -> event?.let(::renderEvent) }
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        (activity as? MainActivity)?.applyImmersiveMode()
    }

    // region RENDER
    override fun render(data: SettingsData) {

    }
    // endregion

    private fun initView() {
        subscribeIntents()
    }

    private fun subscribeIntents() {
        /*val getHomeUrl = Observable.just(Unit).flatMap { viewModel.intentGetHomeUrl(it) }

        viewModel.subscribe(getHomeUrl)*/
    }

}
