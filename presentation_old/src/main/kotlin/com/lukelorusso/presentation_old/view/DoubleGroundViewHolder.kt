package com.lukelorusso.presentation_old.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukelorusso.presentation_old.R

open class DoubleGroundViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val viewBackground: ViewGroup? = view.findViewById(R.id.viewBackground)
    val viewForeground: ViewGroup? = view.findViewById(R.id.viewForeground)
}
