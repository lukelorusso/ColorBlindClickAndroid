@file:Suppress("CanBeParameter")

package com.lukelorusso.presentation.scenes.history

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.containsWords
import com.lukelorusso.presentation.extensions.getLocalizedDateTime
import com.lukelorusso.presentation.extensions.hashColorToPixel
import com.lukelorusso.presentation.view.DoubleGroundViewHolder
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import kotlinx.android.synthetic.main.item_color.view.*

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

                        val states = arrayOf(
                                intArrayOf(android.R.attr.state_enabled),  // enabled
                                intArrayOf(-android.R.attr.state_enabled), // disabled
                                intArrayOf(-android.R.attr.state_checked), // unchecked
                                intArrayOf(android.R.attr.state_pressed)   // pressed
                        )

                        val colorRes = if (even) R.color.background_evens else R.color.background_odds
                        val color = ContextCompat.getColor(context, colorRes)
                        val colors = arrayOf(color, color, color, color).toIntArray()

                        ViewCompat.setBackgroundTintList(content, ColorStateList(states, colors))

                        (itemPreviewPanel.background as? GradientDrawable)?.setColor(item.colorHex.hashColorToPixel())

                        itemName.text = item.colorName

                        itemPicker.text = item.colorHex

                        itemDescription.text = context.getLocalizedDateTime(item.timestamp)

                        setOnClickListener { onItemClicked(item) }
                    }
        }
    }
    //endregion
}
