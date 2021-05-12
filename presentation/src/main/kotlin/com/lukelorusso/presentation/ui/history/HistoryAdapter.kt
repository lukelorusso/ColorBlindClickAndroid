@file:Suppress("CanBeParameter")

package com.lukelorusso.presentation.ui.history

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.containsWords
import com.lukelorusso.presentation.extensions.getLocalizedDateTime
import com.lukelorusso.presentation.extensions.hashColorToPixel
import com.lukelorusso.presentation.view.DoubleGroundViewHolder
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView

/**
 * Based on https://github.com/lopspower/HFRecyclerView
 * Copyright 2018 Mikhael LOPEZ - http://mikhaellopez.com/
 */
class HistoryAdapter(
    private val withHeader: Boolean,
    private val withFooter: Boolean,
    private val onItemClicked: (Color) -> Unit
) : HFRecyclerView<Color>(withHeader, withFooter) {

    internal var nameFilter = ""
        set(value) {
            field = value
            applySorting()
        }
    internal var originalData: List<Color> = emptyList()
        set(value) {
            field = value
            applySorting()
        }

    private fun applySorting() {
        val filteredByName = mutableListOf<Color>()

        if (nameFilter.isBlank()) filteredByName.addAll(originalData)
        else for (item in originalData)
            if (item.toString().containsWords(nameFilter)) filteredByName.add(item)

        data = filteredByName
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemViewHolder -> holder.bind(
                getItem(position),
                position,
                onItemClicked
            )
        }
    }

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder.ItemViewHolder(inflater.inflate(R.layout.item_color, parent, false))

    override fun getHeaderView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? = null

    override fun getFooterView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? = null

    fun removeItem(item: Color) = (data as? MutableList)?.remove(item)

    fun removeAllItems() = (data as? MutableList)?.clear()

    fun getItemAtPosition(position: Int): Color {
        val header = if (withHeader) 1 else 0
        return getItem(position - header) //(position - 1) in case of header
    }

    //region ViewHolder Header
    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class ItemViewHolder(view: View) : DoubleGroundViewHolder(view) {
            fun bind(item: Color, position: Int, onItemClicked: (Color) -> Unit) =
                with(itemView) {
                    val even = position % 2 == 0
                    val colorRes = if (even)
                        R.color.item_background_evens
                    else
                        R.color.item_background_odds
                    val color = ContextCompat.getColor(context, colorRes)
                    setBackgroundColor(color)

                    val itemPreviewPanel = findViewById<View>(R.id.itemPreviewPanel)
                    (itemPreviewPanel.background as? GradientDrawable)
                        ?.setColor(item.similarColor.hashColorToPixel())

                    val itemName = findViewById<TextView>(R.id.itemName)
                    itemName.text = item.colorName

                    val itemPicker = findViewById<TextView>(R.id.itemPicker)
                    itemPicker.text = item.similarColor

                    val itemDescription = findViewById<TextView>(R.id.itemDescription)
                    itemDescription.text = context.getLocalizedDateTime(item.timestamp)

                    setOnClickListener { onItemClicked(item) }
                }
        }
    }
    //endregion
}
