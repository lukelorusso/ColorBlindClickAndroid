package com.lukelorusso.presentation.extensions

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.view.ChoiceListAdapter

fun <T> List<T>.showListDialog(
    context: Context,
    title: CharSequence,
    currentSelectedPosition: Int = -1,
    callback: (T, Int) -> Unit
) {
    // Inflate View
    val view = View.inflate(context, R.layout.layout_dialog_list_picker, null)

    // Create Dialog
    val dialog = AlertDialog.Builder(context).setView(view).create()
    dialog.show()

    // Init Dialog View
    view.findViewById<TextView>(R.id.text_dialog_title).text = title
    view.findViewById<View>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }

    // Init RecyclerView
    val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_search)
    val searchTextAdapter =
        ChoiceListAdapter<T>(currentSelectedPosition) { `object`, selectedIndex ->
            callback(`object`, selectedIndex)
            dialog.dismiss()
        }
    searchTextAdapter.data = this
    recyclerView.adapter = searchTextAdapter
}
