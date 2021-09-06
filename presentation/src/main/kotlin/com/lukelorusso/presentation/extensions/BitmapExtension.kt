package com.lukelorusso.presentation.extensions

import android.graphics.Bitmap
import android.graphics.Color
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

/**
 * @param averageNeighbourhood is the number of pixel next to the central one;
 * the goal is to create a grid in the center of the bitmap used to determine the average color:
 * - if 0 only the central pixel will determine the color
 * - if 1 the color will be determined by the 3x3 pixel matrix = average of 9 pixel colors
 * - if 2 the color will be determined by the 5x5 pixel matrix = average of 25 pixel colors
 * ... and so on
 */
fun Bitmap.getAveragePixel(averageNeighbourhood: Int = 0): Int {
    var redBucket: Long = 0
    var greenBucket: Long = 0
    var blueBucket: Long = 0
    var pixelCount: Long = 0
    val centralWidth = width / 2
    val centralHeight = height / 2

    for (width in (centralWidth - averageNeighbourhood)..(centralWidth + averageNeighbourhood)) {
        for (height in (centralHeight - averageNeighbourhood)..(centralHeight + averageNeighbourhood)) {
            pixelCount++
            val pixel = getPixel(width, height)
            redBucket += Color.red(pixel)
            greenBucket += Color.green(pixel)
            blueBucket += Color.blue(pixel)
        }
    }

    return Color.rgb(
        (redBucket / pixelCount).toInt(),
        (greenBucket / pixelCount).toInt(),
        (blueBucket / pixelCount).toInt()
    )
}
