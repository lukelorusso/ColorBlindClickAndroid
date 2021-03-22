package com.lukelorusso.presentation.scenes.base.view

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress.*
import javax.inject.Inject

abstract class ABaseDataFragment<ViewModel : AViewModel<Data>, Data : Any>(
        @LayoutRes contentLayoutId: Int,
        private val viewModelType: Class<ViewModel>
) : ABaseFragment(contentLayoutId), ADataView<Data> {

    class ABaseDataFragmentViewBindingInjector {
        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory
    }

    private val injector = ABaseDataFragmentViewBindingInjector()
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(injector)
        viewModel = ViewModelProvider(this, injector.viewModelFactory).get(viewModelType)
        viewModel.initRouter(requireActivity() as AppCompatActivity, this)
        super.onCreate(savedInstanceState)
    }

    //region RENDER
    protected fun showLoading(visible: Boolean) {
        progress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showRefreshingLoading(swipeRefreshLayout: SwipeRefreshLayout, visible: Boolean) {
        swipeRefreshLayout.isRefreshing = visible
    }

    protected fun showRetryLoading(visible: Boolean) {
        btnErrorRetry.isClickable = !visible
        errorProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    protected fun showContent(content: View, visible: Boolean) {
        content.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showError(visible: Boolean) {
        viewError.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun renderError(messageError: String?) {
        messageError?.also { textErrorDescription.text = it }
    }

    protected open fun renderSnack(messageError: String?) {
        messageError?.also {
            activity?.also { activity ->
                Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        it, Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    //endregion

}
