package com.lukelorusso.presentation.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.DialogFragmentSettingsBinding
import com.lukelorusso.presentation.extensions.showListDialog
import com.lukelorusso.presentation.extensions.toBoolean
import com.lukelorusso.presentation.extensions.toInt
import com.lukelorusso.presentation.ui.base.ARenderBottomSheetDialogFragment
import com.lukelorusso.presentation.ui.main.MainActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsDialogFragment : ARenderBottomSheetDialogFragment<SettingsData>(
    isFullScreen = true
) {

    companion object {
        val TAG: String = SettingsDialogFragment::class.java.simpleName

        fun newInstance(): SettingsDialogFragment =
            SettingsDialogFragment()
    }

    // Intents
    private val intentSetPixelNeighbourhood = PublishSubject.create<Int>()
    private val intentSetSaveCameraOption = PublishSubject.create<Boolean>()

    // View
    private lateinit var binding: DialogFragmentSettingsBinding // This property is only valid between onCreateView and onDestroyView
    private val viewModel by viewModel<SettingsViewModel>()

    // Properties
    private lateinit var settingsViewfinderPixelsValueList: List<String>
    private lateinit var settingsSaveCameraOptionsList: List<String>

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
        renderSettings(
            data.pixelNeighbourhood,
            data.saveCameraOption
        )
    }

    private fun renderSettings(
        pixelNeighbourhood: Int?,
        saveCameraOption: Boolean?
    ) {
        pixelNeighbourhood?.also {
            binding.tvSettingsViewfinderPixels.text =
                settingsViewfinderPixelsValueList[it]
        }

        saveCameraOption?.also {
            binding.tvSettingsSaveCameraOptions.text =
                settingsSaveCameraOptionsList[it.toInt()]
        }
    }
    // endregion

    private fun initView() {
        subscribeIntents()

        settingsViewfinderPixelsValueList =
            viewfinderPixelsValueStringResList.map { labelStringRes -> getString(labelStringRes) }

        binding.itemSettingsViewfinderPixels.setOnClickListener {
            val currentPosition = settingsViewfinderPixelsValueList
                .indexOfFirst { label -> label == binding.tvSettingsViewfinderPixels.text }

            settingsViewfinderPixelsValueList.showListDialog(
                context = requireContext(),
                title = getString(R.string.settings_viewfinder_pixels),
                currentSelectedPosition = currentPosition
            ) { newLabel, value ->
                binding.tvSettingsViewfinderPixels.text = newLabel
                intentSetPixelNeighbourhood.onNext(value)
            }
        }

        settingsSaveCameraOptionsList =
            saveCameraOptionsStringResList.map { labelStringRes -> getString(labelStringRes) }

        binding.itemSettingsSaveCameraOptions.setOnClickListener {
            val currentPosition = settingsSaveCameraOptionsList
                .indexOfFirst { label -> label == binding.tvSettingsSaveCameraOptions.text }

            settingsSaveCameraOptionsList.showListDialog(
                context = requireContext(),
                title = getString(R.string.settings_save_camera_options),
                currentSelectedPosition = currentPosition
            ) { newLabel, value ->
                binding.tvSettingsSaveCameraOptions.text = newLabel
                intentSetSaveCameraOption.onNext(value.toBoolean())
            }
        }
    }

    private fun subscribeIntents() {
        val loadData = Observable.just(Unit)
            .flatMap { viewModel.intentLoadData(it) }
        val setPixelNeighbourhood = intentSetPixelNeighbourhood
            .flatMap { viewModel.intentSetPixelNeighbourhood(it) }
        val setSaveCameraOption = intentSetSaveCameraOption
            .flatMap { viewModel.intentSetSaveCameraOption(it) }

        viewModel.subscribe(
            loadData,
            setPixelNeighbourhood,
            setSaveCameraOption
        )
    }

}
