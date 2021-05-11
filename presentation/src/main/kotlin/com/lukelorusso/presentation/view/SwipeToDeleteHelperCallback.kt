package com.lukelorusso.presentation.view

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteHelperCallback(
        private val hasHeader: Boolean,
        private val hasFooter: Boolean
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (hasHeader && viewHolder.bindingAdapterPosition == 0) return 0
        if (hasFooter && viewHolder.bindingAdapterPosition == recyclerView.childCount - 1) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                viewHolder.getForegroundView(),
                dX,
                dY,
                actionState,
                isCurrentlyActive
        )
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(viewHolder.getForegroundView())
    }

    // Let's draw our delete view
    override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        val isCancelled = dX == 0f && !isCurrentlyActive
        viewHolder.getBackgroundView().visibility = if (isCancelled) View.GONE else View.VISIBLE
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
                canvas,
                recyclerView,
                viewHolder.getForegroundView(),
                dX,
                dY,
                actionState,
                isCurrentlyActive
        )
    }

    /**
     * On some old Android versions view.findViewById(...) may result in a NullPointerException.
     * Not a big deal, since we can refer to views by their position in the FrameLayout;
     * btw this means that YOU NEED TO KEEP THE RIGHT POSITION:
     *     0 for viewBackground
     * and
     *     1 for viewForeground
     *
     * If you need to change layout's views position, remember to update this code!
     */
    private fun RecyclerView.ViewHolder.getBackgroundView() =
            (this as? DoubleGroundViewHolder)?.viewBackground
                    ?: ((this.itemView as ViewGroup).getChildAt(0) as ViewGroup)

    /**
     * Same as before
     */
    private fun RecyclerView.ViewHolder.getForegroundView() =
            (this as? DoubleGroundViewHolder)?.viewForeground
                    ?: ((this.itemView as ViewGroup).getChildAt(1) as ViewGroup)
}
