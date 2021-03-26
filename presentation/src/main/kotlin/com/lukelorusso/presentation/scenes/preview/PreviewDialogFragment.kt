package com.lukelorusso.presentation.scenes.preview

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.DialogFragmentPreviewBinding
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseBottomSheetDialogFragment
import com.lukelorusso.presentation.scenes.main.MainActivity
import com.lukelorusso.presentation.task.SharePNGTask
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PreviewDialogFragment : ABaseBottomSheetDialogFragment<PreviewDialogViewModel, PreviewDialogData>(
        PreviewDialogViewModel::class.java,
        isFullScreen = false
) {

    companion object {
        private const val EXTRA_SERIALIZED_COLOR = "EXTRA_SERIALIZED_COLOR"
        private const val EXTRA_STATUS_BAR_TOP_MARGIN = "EXTRA_STATUS_BAR_TOP_MARGIN"

        val TAG: String = PreviewDialogFragment::class.java.simpleName

        fun newInstance(
                serializedColor: String,
                statusBarTopMargin: Boolean
        ): PreviewDialogFragment =
                PreviewDialogFragment().build {
                    putString(EXTRA_SERIALIZED_COLOR, serializedColor)
                    putBoolean(EXTRA_STATUS_BAR_TOP_MARGIN, statusBarTopMargin)
                }
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // View
    private lateinit var binding: DialogFragmentPreviewBinding // This property is only valid between onCreateView and onDestroyView

    // Properties
    private val extraColor by lazy {
        requireArguments().getString(EXTRA_SERIALIZED_COLOR)!!.let { gson.fromJson<Color>(it) }
    }
    private lateinit var colorDescription: String
    private var task: SharePNGTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        DialogFragmentPreviewBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(viewLifecycleOwner) {
            if (it != null) render(it)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        (activity as? MainActivity)?.applyImmersiveMode()
    }

    // region RENDER
    override fun render(data: PreviewDialogData) {
        renderHomeUrl(data.homeUrl)
        renderSnack(data.snackMessage)
    }

    private fun renderHomeUrl(homeUrl: String?) = homeUrl?.also {
        val credits = resources.getString(R.string.credits) + " " + it
        colorDescription = extraColor.sharableDescription(credits)
        showColorToast()
    }

    private fun renderSnack(snackMessage: String?) = snackMessage?.also {
        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
    }
    // endregion

    private fun initView() {
        subscribeIntents()

        binding.fabPreviewShare.setOnClickListener(fabClickListener)

        val duration = resources.getInteger(R.integer.fading_effect_duration_default)
        Handler(Looper.getMainLooper()).postDelayed({
            setColor(extraColor)
            binding.vPreviewPanel.fadeInView()
        }, duration.toLong())
    }

    private fun subscribeIntents() {
        val getHomeUrl = Observable.just(Unit).flatMap { viewModel.intentGetHomeUrl(it) }

        viewModel.subscribe(getHomeUrl)
    }

    private fun setColor(color: Color) =
            (binding.vPreviewPanel.background as? GradientDrawable)
                    ?.setColor(color.similarColor.hashColorToPixel())

    private val fabClickListener = View.OnClickListener {
        if (activity != null && this@PreviewDialogFragment.isAdded) sharePNG()
    }

    private fun showColorToast() {
        binding.inclToolbarColor.root.visibility = View.VISIBLE

        binding.inclToolbarColor.colorPreviewPanel.visibility = View.GONE

        binding.inclToolbarColor.colorTopLine.visibility = View.VISIBLE
        val topLineText = extraColor.similarColor
        binding.inclToolbarColor.colorTopLine.text = topLineText

        binding.inclToolbarColor.colorMainLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorMainLine.text = extraColor.colorName

        binding.inclToolbarColor.colorBottomLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorBottomLine.text = extraColor.toRGBString()

        binding.inclToolbarColor.root.setOnClickListener {
            activity?.also {
                trackerHelper.track(activity, TrackerHelper.Actions.SHARED_TEXT)
                it.shareText(colorDescription, getString(R.string.choose_an_app))
            }
        }
    }

    private fun sharePNG() = activity?.also {
        if (task == null) {
            trackerHelper.track(activity, TrackerHelper.Actions.SHARED_PREVIEW)
            binding.fabPreviewShare.fadeOutView()
            task = SharePNGTask(
                    it,
                    binding.vPreviewPanel,
                    BuildConfig.APPLICATION_ID,
                    resources.getInteger(R.integer.color_picker_dimens_share_in_px),
                    colorDescription,
                    getString(R.string.choose_an_app),
                    object : SharePNGTask.ActionListener {
                        override fun onTaskCompleted() {
                            binding.fabPreviewShare.fadeInView()
                            task = null
                        }

                        override fun onTaskFailed() {
                            onTaskCompleted()
                            renderSnack(getString(R.string.error_generic))
                        }
                    }
            )
            task?.execute()
        }
    }

}
