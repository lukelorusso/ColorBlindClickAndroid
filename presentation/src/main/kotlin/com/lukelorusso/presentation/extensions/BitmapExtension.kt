package com.lukelorusso.presentation.extensions

import android.graphics.Bitmap
import android.graphics.Matrix

private var scaleMatrix: Matrix? = null

/**
 * Creates a new bitmap, scaled from an existing bitmap, when possible. If the
 * specified width and height are the same as the current width and height of
 * the source bitmap, the source bitmap is returned and no new bitmap is
 * created.
 *
 * @param dstWidth  The new bitmap's desired width.
 * @param dstHeight The new bitmap's desired height.
 * @param filter    true if the source should be filtered.
 * @return The new scaled bitmap or the source bitmap if no scaling is required.
 * @throws IllegalArgumentException if width is <= 0, or height is <= 0
 */
fun Bitmap.getScaled(
        dstWidth: Int,
        dstHeight: Int,
        filter: Boolean
): Bitmap {
    var m: Matrix?
    synchronized(Bitmap::class.java) {
        // small pool of just 1 matrix
        m = scaleMatrix
        scaleMatrix = null
    }

    if (m == null) {
        m = Matrix()
    }

    val width = this.width
    val height = this.height
    val sx = dstWidth / width.toFloat()
    val sy = dstHeight / height.toFloat()
    m?.setScale(sx, sy)
    val b = Bitmap.createBitmap(this, 0, 0, width, height, m, filter)

    synchronized(Bitmap::class.java) {
        // do we need to check for null? why not just assign every time?
        if (scaleMatrix == null) {
            scaleMatrix = m
        }
    }

    return b
}
