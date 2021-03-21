package com.lukelorusso.presentation_old.scenes.preview

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation_old.BuildConfig
import com.lukelorusso.presentation_old.R
import com.lukelorusso.presentation_old.extensions.*
import com.lukelorusso.presentation_old.helper.TrackerHelper
import com.lukelorusso.presentation_old.scenes.base.view.ABaseBottomSheetDialogFragment
import com.lukelorusso.presentation_old.scenes.main.MainActivity
import com.lukelorusso.presentation_old.task.SharePNGTask
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.dialog_fragment_preview.*
import kotlinx.android.synthetic.main.layout_color_toolbar.*
import javax.inject.Inject

class PreviewDialogFragment : ABaseBottomSheetDialogFragment(
    R.layout.dialog_fragment_preview,
    isFullScreen = false
), PreviewDialogView {

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
    lateinit var presenter: PreviewDialogPresenter

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Properties
    private val extraColor by lazy {
        requireArguments().getString(EXTRA_SERIALIZED_COLOR)!!.let { gson.fromJson<Color>(it) }
    }
    private lateinit var colorDescription: String
    private var task: SharePNGTask? = null

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        (activity as? MainActivity)?.applyImmersiveMode()
    }

    // region INTENTS
    override fun intentGetHomeUrl(): Observable<Unit> = Observable.just(Unit)
    // endregion

    // region RENDER
    override fun render(viewModel: PreviewDialogViewModel) {
        TimberWrapper.d { "render: $viewModel" }

        activity?.runOnUiThread {
            renderHomeUrl(viewModel.homeUrl)
            renderSnack(viewModel.snackMessage)
        }
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
        fabPreviewShare.setOnClickListener(fabClickListener)

        val duration = resources.getInteger(R.integer.fading_effect_duration_default)
        Handler().postDelayed({
            setColor(extraColor)
            vPreviewPanel?.fadeInView()
        }, duration.toLong())
    }

    private fun setColor(color: Color) =
        (vPreviewPanel?.background as? GradientDrawable)
            ?.setColor(color.colorHex.hashColorToPixel())

    private val fabClickListener = View.OnClickListener {
        if (activity != null && this@PreviewDialogFragment.isAdded) sharePNG()
    }

    private fun showColorToast() {
        toolbarColor.visibility = View.VISIBLE

        colorPreviewPanel.visibility = View.GONE

        colorTopLine.visibility = View.VISIBLE
        val topLineText = extraColor.colorHex
        colorTopLine.text = topLineText

        colorMainLine.visibility = View.VISIBLE
        colorMainLine.text = extraColor.colorName

        colorBottomLine.visibility = View.VISIBLE
        colorBottomLine.text = extraColor.toRGBString()

        toolbarColor.setOnClickListener {
            activity?.also {
                trackerHelper.track(activity, TrackerHelper.Actions.SHARED_TEXT)
                it.shareText(colorDescription, getString(R.string.choose_an_app))
            }
        }
    }

    private fun sharePNG() = activity?.also {
        if (task == null) {
            trackerHelper.track(activity, TrackerHelper.Actions.SHARED_PREVIEW)
            fabPreviewShare.fadeOutView()
            task = SharePNGTask(
                it,
                vPreviewPanel,
                BuildConfig.APPLICATION_ID,
                resources.getInteger(R.integer.color_picker_dimens_share_in_px),
                colorDescription,
                getString(R.string.choose_an_app),
                object : SharePNGTask.ActionListener {
                    override fun onTaskCompleted() {
                        fabPreviewShare.fadeInView()
                        task = null
                    }
                }
            )
            task?.execute()
        }
    }

}
