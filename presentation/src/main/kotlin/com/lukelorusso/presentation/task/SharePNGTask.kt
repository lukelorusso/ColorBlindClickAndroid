package com.lukelorusso.presentation.task

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.FileProvider
import com.lukelorusso.presentation.extensions.getBitmap
import com.lukelorusso.presentation.extensions.getScaled
import com.lukelorusso.presentation.extensions.shareImageAndText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class SharePNGTask(
    private val context: Context,
    private val colorPreview: View,
    private val applicationId: String,
    private val dimen: Int,
    private val description: String,
    private val popupLabel: String,
    private val listener: ActionListener?
) {

    interface ActionListener {
        fun onTaskCompleted() {}
        fun onTaskFailed() {}
    }

    companion object {
        const val CACHE_DIR = "images"
        const val SHARED_COLOR_FILENAME = "shared_color.png"
    }

    fun execute() {
        Executors.newSingleThreadExecutor().execute {
            val success: Boolean = doInBackground()

            Handler(Looper.getMainLooper()).post {
                if (success)
                    listener?.onTaskCompleted()
                else
                    listener?.onTaskFailed()
            }
        }
    }

    private fun doInBackground(): Boolean {
        try {
            val cachePath = File(
                context.externalCacheDir,
                CACHE_DIR
            )
            val mkdirs = cachePath.mkdirs()
            val sharableBitmap: Bitmap?
            if (mkdirs || cachePath.exists()) {
                val stream = FileOutputStream(
                    "$cachePath/${SHARED_COLOR_FILENAME}"
                )
                sharableBitmap = generateSharablePNG(
                    stream,
                    colorPreview,
                    dimen
                )
                stream.close()
            } else
                return false

            val contentUri: Uri?
            if (sharableBitmap != null) {
                val file = File(
                    cachePath,
                    SHARED_COLOR_FILENAME
                )
                contentUri = FileProvider.getUriForFile(
                    context,
                    applicationId,
                    file
                )
            } else
                return false

            if (contentUri != null) {
                context.shareImageAndText(
                    contentUri,
                    description,
                    popupLabel
                )
            } else
                return false

        } catch (ignored: IOException) {
            return false
        }

        return true
    }

    private fun generateSharablePNG(
        stream: FileOutputStream,
        colorPreview: View,
        dimen: Int
    ): Bitmap? {
        var bitmap = colorPreview.getBitmap()
        if (bitmap.height > dimen) bitmap = bitmap.getScaled(dimen, dimen, false)
        val isCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return if (isCompressed) bitmap else null
    }

}
