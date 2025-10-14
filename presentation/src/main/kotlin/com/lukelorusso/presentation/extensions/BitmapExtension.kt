package com.lukelorusso.presentation.extensions

import android.graphics.*
import androidx.core.graphics.get
import java.io.FileOutputStream

/**
 * @param averageNeighbourhood is the number of pixel next to the central one;
 * the goal is to create a grid in the center of the bitmap used to determine the average color:
 * - if 0 only the central pixel will determine the color
 * - if 1 the color will be determined by the 3x3 pixel matrix = average of 9 pixel colors
 * - if 2 the color will be determined by the 5x5 pixel matrix = average of 25 pixel colors
 * ... and so on
 */
fun Bitmap.getCentralPixelHash(averageNeighbourhood: Int = 0): String {
    var redBucket: Long = 0
    var greenBucket: Long = 0
    var blueBucket: Long = 0
    var pixelCount: Long = 0
    val centralWidth = width / 2
    val centralHeight = height / 2

    for (width in (centralWidth - averageNeighbourhood)..(centralWidth + averageNeighbourhood)) {
        for (height in (centralHeight - averageNeighbourhood)..(centralHeight + averageNeighbourhood)) {
            pixelCount++
            val pixel = this[width, height]
            redBucket += Color.red(pixel)
            greenBucket += Color.green(pixel)
            blueBucket += Color.blue(pixel)
        }
    }

    val twoDigits: (String) -> String = { if (it.length == 1) "0$it" else it }
    val redHex = Integer.toHexString((redBucket / pixelCount).toInt())
    val greenHex = Integer.toHexString((greenBucket / pixelCount).toInt())
    val blueHex = Integer.toHexString((blueBucket / pixelCount).toInt())
    return Triple(
        twoDigits(redHex).uppercase(),
        twoDigits(greenHex).uppercase(),
        twoDigits(blueHex).uppercase()
    ).let { hexTriple -> "#${hexTriple.first}${hexTriple.second}${hexTriple.third}" }
}

/**
 * A one color image.
 * @param width
 * @param height
 * @param color
 * @return A one color image with the given width and height.
 */
fun createBitmap(width: Int, height: Int, color: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.color = color
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    return bitmap
}

fun Bitmap.compressToPNG(stream: FileOutputStream) {
    this.compress(Bitmap.CompressFormat.PNG, 0, stream)
    stream.close()
}
