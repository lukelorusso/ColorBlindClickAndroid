package com.lukelorusso.presentation_old.task

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.view.View
import androidx.core.content.FileProvider
import com.lukelorusso.presentation_old.extensions.getBitmap
import com.lukelorusso.presentation_old.extensions.getScaled
import com.lukelorusso.presentation_old.extensions.shareImageAndText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by LukeLorusso on 09-Jan-18.
 */
class SharePNGTask(
    context: Context,
    colorPreview: View,
    private val applicationId: String,
    private val dimen: Int,
    private val description: String,
    private val popupLabel: String,
    private val listener: ActionListener?
) : AsyncTask<Void, Void, Boolean>() {

    interface ActionListener {
        fun onTaskCompleted() {}
    }

    companion object {
        const val CACHE_DIR = "images"
        const val SHARED_COLOR_FILENAME = "shared_color.png"
    }

    private val mWeakContext: WeakReference<Context> = WeakReference(context)
    private val mWeakColorPreview: WeakReference<View> = WeakReference(colorPreview)

    override fun doInBackground(vararg params: Void): Boolean {
        val context = mWeakContext.get()
        val colorPreview = mWeakColorPreview.get()
        if (context != null && colorPreview != null)
            try {
                val cachePath = File(
                    context.externalCacheDir,
                    CACHE_DIR
                )
                val mkdirs = cachePath.mkdirs()
                val sharableBitmap: Bitmap?
                if (mkdirs || cachePath.exists()) {
                    val stream = FileOutputStream(
                        "$cachePath/$SHARED_COLOR_FILENAME"
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
                } else return false

                if (contentUri != null) {
                    context.shareImageAndText(
                        contentUri,
                        description,
                        popupLabel
                    )
                } else return false

            } catch (ignored: IOException) {
            }

        return true
    }

    override fun onPostExecute(success: Boolean) {
        listener?.onTaskCompleted()
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
