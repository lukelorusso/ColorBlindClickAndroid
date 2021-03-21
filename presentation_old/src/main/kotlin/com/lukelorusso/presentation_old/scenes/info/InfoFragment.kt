package com.lukelorusso.presentation_old.scenes.info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.presentation_old.BuildConfig
import com.lukelorusso.presentation_old.R
import com.lukelorusso.presentation_old.extensions.applyStatusBarMarginTopOnToolbar
import com.lukelorusso.presentation_old.extensions.dpToPixel
import com.lukelorusso.presentation_old.helper.TrackerHelper
import com.lukelorusso.presentation_old.scenes.base.view.ABaseFragment
import com.lukelorusso.presentation_old.view.VerticalSpaceItemDecoration
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.layout_info_toolbar.*
import javax.inject.Inject

class InfoFragment : ABaseFragment(R.layout.fragment_info), InfoView {

    companion object {
        val TAG: String = InfoFragment::class.java.simpleName

        fun newInstance(): InfoFragment = InfoFragment()
    }

    @Inject
    lateinit var presenter: InfoPresenter

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Intents
    private val intentOpenBrowser = PublishSubject.create<String>()
    private val intentGotoCamera = PublishSubject.create<Unit>()

    // Properties
    private val infoAdapter = InfoAdapter(withHeader = false, withFooter = false)

    fun backPressHandled(): Boolean {
        intentGotoCamera.onNext(Unit)
        return true
    }

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

    // region INTENTS
    override fun intentGotoHome(): Observable<Unit> = infoAdapter.intentItemClick
        .filter { position -> position == 0 }
        .doOnNext {
            trackerHelper.track(activity, TrackerHelper.Actions.GOTO_HOME_PAGE)
        }
        .map { Unit }

    override fun intentGotoHelp(): Observable<Unit> = infoAdapter.intentItemClick
        .filter { position -> position == 1 }
        .doOnNext {
            trackerHelper.track(activity, TrackerHelper.Actions.GOTO_HELP_PAGE)
        }
        .map { Unit }

    override fun intentGotoAboutMe(): Observable<Unit> = infoAdapter.intentItemClick
        .filter { position -> position == 2 }
        .doOnNext {
            trackerHelper.track(activity, TrackerHelper.Actions.GOTO_ABOUT_ME_PAGE)
        }
        .map { Unit }

    override fun intentOpenBrowser(): Observable<String> = intentOpenBrowser

    override fun intentGotoCamera(): Observable<Unit> = Observable.merge(
        fabInfoGotoCamera.clicks().map { Unit },
        intentGotoCamera
    )
    // endregion

    //region RENDER
    override fun render(viewModel: InfoViewModel) {
        TimberWrapper.d { "render: $viewModel" }

        activity?.runOnUiThread {
            renderUrl(viewModel.url)
            renderSnack(viewModel.snackMessage)
        }
    }

    private fun renderUrl(url: String?) = url?.also { intentOpenBrowser.onNext(it) }

    private fun renderSnack(snackMessage: String?) = snackMessage?.also {
        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
    }
    // endregion

    private fun initView() {
        activity?.also {
            (it as? AppCompatActivity)?.applyStatusBarMarginTopOnToolbar(toolbar, 25F)
        }

        tvToolbarAppVersion.apply {
            val version = "v.${BuildConfig.VERSION_NAME}"
            text = version
        }

        srlInfoList.isEnabled = false

        populateAdapter()
    }

    private fun populateAdapter() {
        activity?.also {
            infoAdapter.data = resources.getStringArray(R.array.info_adapter_items).asList()
            rvInfoList.adapter = infoAdapter
            rvInfoList.addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireContext().dpToPixel(2F)
                )
            )
            rvInfoList.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0 || dy < 0 && fabInfoGotoCamera.isShown)
                            fabInfoGotoCamera.hide()
                    }

                    override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE)
                            fabInfoGotoCamera.show()
                        super.onScrollStateChanged(rv, newState)
                    }
                }
            )
        }
    }

}
