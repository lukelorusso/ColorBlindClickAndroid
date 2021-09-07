package com.lukelorusso.presentation.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation.R

class ChoiceListAdapter<T>(
    private val currentSelectedPosition: Int,
    private val selectedListener: (T, Int) -> Unit
) : RecyclerView.Adapter<ChoiceListAdapter.ViewHolder<T>>() {

    var data: List<T> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_simple_text,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        data[position].also { choice ->
            holder.bind(choice, position, currentSelectedPosition) {
                val currentPosition = holder.absoluteAdapterPosition
                if (currentPosition < itemCount) {
                    selectedListener(choice, currentPosition)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: T, position: Int, currentSelectedPosition: Int, clickCallBack: () -> Unit) =
            with(itemView) {
                val textContent = findViewById<TextView>(R.id.textContent)
                textContent.text = item.toString()
                textContent.setTypeface(
                    textContent.typeface,
                    if (position == currentSelectedPosition)
                        Typeface.BOLD_ITALIC
                    else
                        Typeface.NORMAL
                )
                setOnClickListener { clickCallBack() }
            }
    }
}
