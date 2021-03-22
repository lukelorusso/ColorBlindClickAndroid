package com.lukelorusso.presentation.scenes.info

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.applyStatusBarMarginTopOnToolbar
import com.lukelorusso.presentation.extensions.dpToPixel
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseDataFragment
import com.lukelorusso.presentation.view.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.layout_info_toolbar.*
import javax.inject.Inject


class InfoFragment : ABaseDataFragment<InfoViewModel, InfoData>(
        R.layout.fragment_info,
        InfoViewModel::class.java
) {

    companion object {
        val TAG: String = InfoFragment::class.java.simpleName

        fun newInstance(): InfoFragment = InfoFragment()
    }

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Properties
    private val infoAdapter = InfoAdapter(withHeader = false, withFooter = false)

    fun backPressHandled(): Boolean {
        viewModel.gotoCamera()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(viewLifecycleOwner) {
            if (it != null) render(it)
        }
    }

    //region RENDER
    override fun render(data: InfoData) {
        renderUrl(data.url)
        renderSnack(data.snackMessage)
    }

    private fun renderUrl(url: String?) = url?.also { viewModel.gotoBrowser(it) }

    override fun renderSnack(messageError: String?) {
        messageError?.also { message ->
            activity?.also { activity ->
                Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    // endregion

    private fun initView() {
        subscribeIntents()

        activity?.also {
            (it as? AppCompatActivity)?.applyStatusBarMarginTopOnToolbar(toolbar, 25F)
        }

        tvToolbarAppVersion.apply {
            val version = "v.${BuildConfig.VERSION_NAME}"
            text = version
        }

        srlInfoList.isEnabled = false

        fabInfoGotoCamera.setOnClickListener { viewModel.gotoCamera() }

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

    private fun subscribeIntents() {
        val getHomeUrl = infoAdapter.intentItemClick
                .filter { position -> position == 0 }
                .doOnNext {
                    trackerHelper.track(activity, TrackerHelper.Actions.GOTO_HOME_PAGE)
                }
                .flatMap { viewModel.intentGetHelpUrl() }

        val getHelpUrl = infoAdapter.intentItemClick
                .filter { position -> position == 1 }
                .doOnNext {
                    trackerHelper.track(activity, TrackerHelper.Actions.GOTO_HELP_PAGE)
                }
                .flatMap { viewModel.intentGetHomeUrl() }

        val getAboutMeUrl = infoAdapter.intentItemClick
                .filter { position -> position == 2 }
                .doOnNext {
                    trackerHelper.track(activity, TrackerHelper.Actions.GOTO_ABOUT_ME_PAGE)
                }
                .flatMap { viewModel.intentGetAboutMeUrl() }

        viewModel.subscribe(getHomeUrl, getHelpUrl, getAboutMeUrl)
    }

}
