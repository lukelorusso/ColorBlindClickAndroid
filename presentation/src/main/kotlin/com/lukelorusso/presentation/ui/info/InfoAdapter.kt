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
    HFRecyclerView<String>(withHeader, withFooter) {

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
            fun bind(item: String, position: Int, intentItemClick: PublishSubject<Int>) =
                with(itemView) {
                    val even = position % 2 == 0
                    val colorRes = if (even)
                        R.color.item_background_evens
                    else
                        R.color.item_background_odds
                    val color = ContextCompat.getColor(context, colorRes)
                    setBackgroundColor(color)

                    val itemName = findViewById<TextView>(R.id.itemName)
                    itemName.text = item

                    setOnClickListener { intentItemClick.onNext(position) }
                }
        }
    }
    //endregion

}
