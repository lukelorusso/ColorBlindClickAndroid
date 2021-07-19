package com.lukelorusso.presentation.ui.history

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding4.view.clicks
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentHistoryBinding
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.ARenderFragment
import com.lukelorusso.presentation.ui.base.ContentState
import com.lukelorusso.presentation.view.SwipeToDeleteHelperCallback
import com.lukelorusso.presentation.view.VerticalSpaceItemDecoration
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : ARenderFragment<HistoryData>(R.layout.fragment_history) {

    companion object {
        val TAG: String = HistoryFragment::class.java.simpleName

        fun newInstance(): HistoryFragment = HistoryFragment()
    }

    // Intents
    private val intentLoadData: PublishSubject<Unit> = PublishSubject.create()
    private val intentDeleteItem = PublishSubject.create<Color>()
    private val intentDeleteAllItem = PublishSubject.create<Unit>()

    // View
    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel by viewModel<HistoryViewModel>()

    // Properties
    private val trackerHelper by inject<TrackerHelper>()
    private var isSearchingMode: Boolean
        get() = binding.inclToolbar.tvToolbarTitle.visibility == View.GONE
        set(searching) {
            if (searching) {
                binding.inclToolbar.tvToolbarTitle.visibility = View.GONE
                binding.inclToolbar.etToolbarSearch.visibility = View.VISIBLE
                binding.inclToolbar.etToolbarSearch.requestFocus()
                binding.inclToolbar.etToolbarSearch.showKeyboard()
            } else {
                binding.inclToolbar.tvToolbarTitle.visibility = View.VISIBLE
                binding.inclToolbar.etToolbarSearch.visibility = View.GONE
                binding.inclToolbar.etToolbarSearch.setText("")
                binding.inclToolbar.etToolbarSearch.hideKeyboard()
            }
        }
    private val adapter = HistoryAdapter(withHeader = false, withFooter = false) { colorSelected ->
        viewModel.gotoPreview(colorSelected)
    }

    fun backPressHandled(): Boolean {
        return when {
            isActive() && isSearchingMode -> {
                isSearchingMode = false
                true
            }
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

    // region RENDER
    override fun render(data: HistoryData) {
        showLoading(binding.inclProgress.root, data.contentState == ContentState.LOADING)
        showRefreshingLoading(binding.srlHistoryList, false)
        showRetryLoading(
            binding.inclError.btnErrorRetry,
            binding.inclError.inclProgress.root,
            data.contentState == ContentState.RETRY
        )
        showContent(binding.content, data.contentState == ContentState.CONTENT)
        showError(binding.inclError.viewError, data.contentState == ContentState.ERROR)

        renderData(data.list, data.deletedItem, data.deletedAllItems)
        //renderError(binding.inclError.textErrorDescription, data.errorMessage)
        renderPersistenceException(data.isPersistenceException)
    }

    override fun showContent(content: View, visible: Boolean) {
        content.isEnabled = visible
    }

    private fun renderData(
        list: List<Color>?,
        deletedItem: Color?,
        deletedAllItems: Boolean?
    ) {
        list?.also {
            adapter.originalData = it
            binding.rvHistoryList.scrollToPosition(0)
            renderNoItems()
            renderDeleteAll()
        }

        deletedItem?.also {
            adapter.notifyDataSetChanged() // to recalculate evens - odds colors
            renderNoItems()
            renderDeleteAll()
        }

        deletedAllItems?.also {
            if (it) {
                renderNoItems()
                renderDeleteAll()
            }
        }
    }

    private fun renderNoItems() {
        binding.tvHistoryNoItems.visibility =
            if (adapter.data.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun renderDeleteAll() {
        binding.inclToolbar.btnToolbarDeleteAll.visibility =
            if (adapter.data.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun renderPersistenceException(isPersistenceException: Boolean?) {
        if (isPersistenceException == true)
            trackerHelper.track(activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
    }
    // endregion

    private fun initView() {
        subscribeIntents()

        activity?.also {
            (it as? AppCompatActivity)?.applyStatusBarMarginTopOnToolbar(binding.inclToolbar.toolbar)
        }

        binding.inclToolbar.btnToolbarSearch.setOnClickListener { switchSearchingMode() }
        binding.inclToolbar.btnToolbarDeleteAll.setOnClickListener { askToRemoveAllItem() }
        binding.inclToolbar.tvToolbarTitle.setOnClickListener { switchSearchingMode() }

        binding.srlHistoryList.isEnabled = false

        binding.fabHistoryGotoCamera.setOnClickListener { viewModel.gotoCamera() }

        populateAdapter()
    }

    private fun populateAdapter() {
        //rvHistoryList.setHasFixedSize(true)
        binding.rvHistoryList.adapter = adapter
        binding.rvHistoryList.addItemDecoration(
            VerticalSpaceItemDecoration(
                requireContext().dpToPixel(2F)
            )
        )
        binding.rvHistoryList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && binding.fabHistoryGotoCamera.isShown)
                        switchFAB(false)
                }

                override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        switchFAB(true)
                    super.onScrollStateChanged(rv, newState)
                }
            }
        )
        val swipeHelper = object : SwipeToDeleteHelperCallback(
            hasHeader = false,
            hasFooter = false
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                askToRemoveItem(adapter.getItemAtPosition(position), position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.rvHistoryList)
        binding.inclToolbar.etToolbarSearch.onTextChanged { filter -> adapter.nameFilter = filter }
    }

    private fun subscribeIntents() {
        val loadData = Observable.merge(Observable.just(Unit), intentLoadData)
            .flatMap { viewModel.intentLoadData(it) }
        val refreshData =
            binding.srlHistoryList.refreshes().flatMap { viewModel.intentRefreshData(it) }
        val retryData =
            binding.inclError.btnErrorRetry.clicks().flatMap { viewModel.intentRetryData(it) }
        val deleteItem = intentDeleteItem.flatMap { viewModel.intentDeleteItem(it) }
        val deleteAllItem = intentDeleteAllItem.flatMap { viewModel.intentDeleteAllItems(it) }

        viewModel.subscribe(
            loadData,
            refreshData,
            retryData,
            deleteItem,
            deleteAllItem
        )
    }

    private fun switchSearchingMode() {
        isSearchingMode = !isSearchingMode
    }

    private fun switchFAB(showFAB: Boolean? = null) {
        val show = showFAB ?: !binding.fabHistoryGotoCamera.isShown
        if (show) {
            binding.fabHistoryGotoCamera.show()
        } else {
            binding.fabHistoryGotoCamera.hide()
        }
    }

    private fun askToRemoveItem(color: Color, adapterPosition: Int) {
        val originalDataSize = adapter.data.size
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_generic_confirmation)
        }
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationMessage).text =
            getString(R.string.color_delete_confirmation_message).toHtml(requireContext())
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationPositive).text =
            getString(R.string.yes).toHtml(requireContext())
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationNegative).text =
            getString(R.string.no).toHtml(requireContext())
        dialog.findViewById<View>(R.id.textDialogGenericConfirmationNegative)
            .setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.textDialogGenericConfirmationPositive).setOnClickListener {
            trackerHelper.track(activity, TrackerHelper.Actions.DELETED_ITEM)
            (adapter.originalData as? MutableList)?.remove(color)
            adapter.removeItem(color) // I assume, if you are removing it, it's in the adapter's data
            dialog.dismiss()
            intentDeleteItem.onNext(color)
        }
        dialog.setOnDismissListener {
            if (originalDataSize != adapter.data.size)
                adapter.notifyItemRemoved(adapterPosition)
            else
                adapter.notifyItemChanged(adapterPosition)
        }
        dialog.show()
    }

    private fun askToRemoveAllItem() {
        isSearchingMode = false
        val originalDataSize = adapter.data.size
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_generic_confirmation)
        }
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationMessage).text =
            getString(R.string.color_delete_all_confirmation_message).toHtml(requireContext())
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationPositive).text =
            getString(R.string.yes).toHtml(requireContext())
        dialog.findViewById<TextView>(R.id.textDialogGenericConfirmationNegative).text =
            getString(R.string.no).toHtml(requireContext())
        dialog.findViewById<View>(R.id.textDialogGenericConfirmationNegative)
            .setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.textDialogGenericConfirmationPositive).setOnClickListener {
            trackerHelper.track(activity, TrackerHelper.Actions.DELETED_ALL_ITEMS)
            (adapter.originalData as? MutableList)?.clear()
            adapter.removeAllItems()
            dialog.dismiss()
            intentDeleteAllItem.onNext(Unit)
        }
        dialog.setOnDismissListener {
            if (originalDataSize != adapter.data.size)
                adapter.notifyItemRangeRemoved(0, originalDataSize)
        }
        dialog.show()
    }

    fun reloadData() = intentLoadData.onNext(Unit)

}
