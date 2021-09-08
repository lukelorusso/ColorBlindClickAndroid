package com.lukelorusso.presentation.ui.info

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentInfoBinding
import com.lukelorusso.presentation.extensions.applyStatusBarMarginTopOnToolbar
import com.lukelorusso.presentation.extensions.dpToPixel
import com.lukelorusso.presentation.extensions.isActive
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.ARenderFragment
import com.lukelorusso.presentation.view.VerticalSpaceItemDecoration
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : ARenderFragment<InfoData>(R.layout.fragment_info) {

    companion object {
        val TAG: String = InfoFragment::class.java.simpleName

        fun newInstance(): InfoFragment = InfoFragment()
    }

    // View
    private val binding by viewBinding(FragmentInfoBinding::bind)
    private val viewModel by viewModel<InfoViewModel>()

    // Properties
    private val trackerHelper by inject<TrackerHelper>()
    private val infoAdapter = InfoAdapter(withHeader = false, withFooter = false)

    fun backPressHandled(): Boolean {
        return when {
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
            requireActivity() as AppCompatActivity,
            fragment = this
        ) // If there's a router, initialize it here
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

    //region RENDER
    override fun render(data: InfoData) {
        renderUrl(data.url)
    }

    private fun renderUrl(url: String?) = url?.also { viewModel.gotoBrowser(it) }
    // endregion

    private fun initView() {
        subscribeIntents()

        activity?.also {
            (it as? AppCompatActivity)?.applyStatusBarMarginTopOnToolbar(
                binding.inclToolbar.toolbar,
                25F
            )
        }

        binding.inclToolbar.tvToolbarAppVersion.apply {
            val version = "v.${BuildConfig.VERSION_NAME}"
            text = version
        }

        binding.srlInfoList.isEnabled = false

        binding.fabInfoGotoCamera.setOnClickListener { viewModel.gotoCamera() }

        populateAdapter()
    }

    private fun populateAdapter() {
        activity?.also {
            infoAdapter.data = labelStringResList.mapIndexed { position, labelStringRes ->
                Pair(iconDrawableResList[position], getString(labelStringRes))
            }
            binding.rvInfoList.adapter = infoAdapter
            binding.rvInfoList.addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireContext().dpToPixel(2F)
                )
            )
            binding.rvInfoList.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0 || dy < 0 && binding.fabInfoGotoCamera.isShown)
                            binding.fabInfoGotoCamera.hide()
                    }

                    override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE)
                            binding.fabInfoGotoCamera.show()
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

        val gotoSettings = infoAdapter.intentItemClick
            .filter { position -> position == 3 }
            .doOnNext {
                trackerHelper.track(activity, TrackerHelper.Actions.GOTO_SETTINGS)
            }
            .flatMap { viewModel.gotoSettings() }

        viewModel.subscribe(getHomeUrl, getHelpUrl, getAboutMeUrl, gotoSettings)
    }

}
