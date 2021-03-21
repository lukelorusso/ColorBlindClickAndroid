@file:Suppress("CanBeParameter")

package com.lukelorusso.presentation_old.scenes.info

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation_old.R
import com.lukelorusso.presentation_old.view.DoubleGroundViewHolder
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_info.view.*

/**
 * Based on https://github.com/lopspower/HFRecyclerView
 * Copyright 2018 Mikhael LOPEZ - http://mikhaellopez.com/
 */
class InfoAdapter(private val withHeader: Boolean, private val withFooter: Boolean) :
    HFRecyclerView<String>(withHeader, withFooter) {

    val intentItemClick: PublishSubject<Int> = PublishSubject.create<Int>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemViewHolder -> holder.bind(
                getItem(position),
                position,
                intentItemClick
            )
        }
    }

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder.ItemViewHolder(inflater.inflate(R.layout.item_info, parent, false))

    override fun getHeaderView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? = null

    override fun getFooterView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? = null

    //region ViewHolder Header
    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class ItemViewHolder(view: View) : DoubleGroundViewHolder(view) {
            fun bind(item: String, position: Int, intentItemClick: PublishSubject<Int>) =
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

                    itemName.text = item

                    setOnClickListener { intentItemClick.onNext(position) }
                }
        }
    }
    //endregion
}
