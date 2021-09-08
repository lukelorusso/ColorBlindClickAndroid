@file:Suppress("CanBeParameter")

package com.lukelorusso.presentation.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.view.DoubleGroundViewHolder
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Based on https://github.com/lopspower/HFRecyclerView
 * Copyright 2018 Mikhael LOPEZ - http://mikhaellopez.com/
 */
class InfoAdapter(private val withHeader: Boolean, private val withFooter: Boolean) :
    HFRecyclerView<Pair<Int, String>>(withHeader, withFooter) { // Pair of DrawableRes Int (icon) and String (label)

    val intentItemClick: PublishSubject<Int> = PublishSubject.create()

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
            fun bind(item: Pair<Int, String>, position: Int, intentItemClick: PublishSubject<Int>) =
                with(itemView) {
                    val even = position % 2 == 0
                    val colorRes = if (even)
                        R.color.item_background_evens
                    else
                        R.color.item_background_odds
                    val color = ContextCompat.getColor(context, colorRes)
                    setBackgroundColor(color)

                    val tvItem = findViewById<TextView>(R.id.tvItemInfo)
                    tvItem.setCompoundDrawablesWithIntrinsicBounds(
                        item.first, // left icon, custom
                        0,
                        R.drawable.keyboard_arrow_right, // right icon, always the same
                        0
                    )
                    tvItem.text = item.second

                    setOnClickListener { intentItemClick.onNext(position) }
                }
        }
    }
    //endregion

}
