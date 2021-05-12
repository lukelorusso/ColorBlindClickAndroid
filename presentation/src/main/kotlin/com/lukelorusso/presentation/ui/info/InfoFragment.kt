package com.lukelorusso.presentation.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentInfoBinding
import com.lukelorusso.presentation.extensions.applyStatusBarMarginTopOnToolbar
import com.lukelorusso.presentation.extensions.dpToPixel
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.ARenderFragment
import com.lukelorusso.presentation.view.VerticalSpaceItemDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class InfoFragment : ARenderFragment<InfoData>() {

    companion object {
        val TAG: String = InfoFragment::class.java.simpleName

        fun newInstance(): InfoFragment = InfoFragment()
    }

    // View
    private lateinit var binding: FragmentInfoBinding // This property is only valid between onCreateView and onDestroyView
    private val viewModel: InfoViewModel by viewModel()

    // Properties
    private val trackerHelper by inject<TrackerHelper>()
    private val infoAdapter = InfoAdapter(withHeader = false, withFooter = false)

    fun backPressHandled(): Boolean {
        viewModel.gotoCamera()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRouter(
            requireActivity() as AppCompatActivity,
            fragment = this
        ) // If there's a router, initialize it here
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentInfoBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(viewLifecycleOwner) { data -> data?.also { render(it) } }
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
            infoAdapter.data = resources.getStringArray(R.array.info_adapter_items).asList()
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

        viewModel.subscribe(getHomeUrl, getHelpUrl, getAboutMeUrl)
    }

}