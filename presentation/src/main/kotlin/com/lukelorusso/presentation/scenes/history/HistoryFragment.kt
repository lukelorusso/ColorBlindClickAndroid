package com.lukelorusso.presentation.scenes.history

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
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseDataFragment
import com.lukelorusso.presentation.scenes.base.view.ContentState
import com.lukelorusso.presentation.scenes.base.view.LoadingState
import com.lukelorusso.presentation.view.SwipeToDeleteHelperCallback
import com.lukelorusso.presentation.view.VerticalSpaceItemDecoration
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_history_toolbar.*
import javax.inject.Inject

class HistoryFragment : ABaseDataFragment<HistoryViewModel, HistoryData>(
        R.layout.fragment_history,
        HistoryViewModel::class.java
) {

    companion object {
        val TAG: String = HistoryFragment::class.java.simpleName

        fun newInstance(): HistoryFragment = HistoryFragment()
    }

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Intents
    val intentLoadData: PublishSubject<Unit> = PublishSubject.create()
    private val intentDeleteItem = PublishSubject.create<Color>()
    private val intentDeleteAllItem = PublishSubject.create<Unit>()

    // Properties
    private var isSearchingMode: Boolean
        get() = tvToolbarTitle.visibility == View.GONE
        set(searching) {
            if (searching) {
                tvToolbarTitle.visibility = View.GONE
                etToolbarSearch.visibility = View.VISIBLE
                etToolbarSearch.requestFocus()
                etToolbarSearch.showKeyboard()
            } else {
                tvToolbarTitle.visibility = View.VISIBLE
                etToolbarSearch.visibility = View.GONE
                etToolbarSearch.setText("")
                etToolbarSearch.hideKeyboard()
            }
        }
    private val adapter = HistoryAdapter(withHeader = false, withFooter = false) { colorSelected ->
        viewModel.gotoPreview(colorSelected)
    }

    fun backPressHandled(): Boolean {
        return when {
            isSearchingMode -> {
                isSearchingMode = false
                true
            }
            else -> {
                viewModel.gotoCamera()
                true
            }
        }
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

    // region RENDER
    override fun render(data: HistoryData) {
        showLoading(data.loadingState == LoadingState.LOADING)
        showRefreshingLoading(srlHistoryList, false)
        showRetryLoading(data.loadingState == LoadingState.RETRY)
        showContent(content, data.contentState == ContentState.CONTENT)
        showError(data.contentState == ContentState.ERROR)

        renderData(data.list, data.deletedItem, data.deletedAllItems)
        renderError(data.errorMessage)
        renderSnack(data.snackMessage)
        renderPersistenceException(data.isPersistenceException)
    }

    private fun renderData(
            list: List<Color>?,
            deletedItem: Color?,
            deletedAllItems: Boolean?
    ) {
        list?.also {
            adapter.originalData = it
            rvHistoryList.scrollToPosition(0)
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
        tvHistoryNoItems.visibility =
                if (adapter.data.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun renderDeleteAll() {
        btnToolbarDeleteAll.visibility =
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
            (it as? AppCompatActivity)?.applyStatusBarMarginTopOnToolbar(toolbar)
        }

        btnToolbarSearch.setOnClickListener { switchSearchingMode() }
        btnToolbarDeleteAll.setOnClickListener { askToRemoveAllItem() }
        tvToolbarTitle.setOnClickListener { switchSearchingMode() }

        srlHistoryList.isEnabled = false

        fabHistoryGotoCamera.setOnClickListener { viewModel.gotoCamera() }

        populateAdapter()
    }

    private fun populateAdapter() {
        //rvHistoryList.setHasFixedSize(true)
        rvHistoryList.adapter = adapter
        rvHistoryList.addItemDecoration(
                VerticalSpaceItemDecoration(
                        requireContext().dpToPixel(2F)
                )
        )
        rvHistoryList.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0 || dy < 0 && fabHistoryGotoCamera.isShown)
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
                val position = viewHolder.adapterPosition
                askToRemoveItem(adapter.getItemAtPosition(position), position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(rvHistoryList)
        etToolbarSearch.onTextChanged { filter -> adapter.nameFilter = filter }
    }

    private fun subscribeIntents() {
        val loadData = Observable.merge(Observable.just(Unit), intentLoadData)
                .flatMap { viewModel.intentLoadData(it) }
        val refreshData = srlHistoryList.refreshes().flatMap { viewModel.intentRefreshData(it) }
        val retryData = btnErrorRetry.clicks().flatMap { viewModel.intentRetryData(it) }
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
        val show = showFAB ?: !fabHistoryGotoCamera.isShown
        if (show) {
            fabHistoryGotoCamera.show()
        } else {
            fabHistoryGotoCamera.hide()
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

}
