package com.lukelorusso.presentation.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.text.format.DateFormat
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.lukelorusso.presentation.BuildConfig
import java.io.*
import java.util.*


const val CACHE_DIR = "images"
const val SHARED_IMAGE_FILENAME = "shared_color.png"

fun Context.startActivityWithFlagNewTask(intent: Intent) {
    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

fun Context.isPermissionGranted(permission: String): Boolean {
    val rc = ActivityCompat.checkSelfPermission(this, permission)
    return rc == PackageManager.PERMISSION_GRANTED
}

fun Context?.getDeviceUdid(): String {
    return this?.run {
        Settings.System.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    } ?: ""

}

fun Context.gotoAppDetailsSettings() {
    val permissionDetailsIntent = Intent()
    permissionDetailsIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    permissionDetailsIntent.addCategory(Intent.CATEGORY_DEFAULT)
    permissionDetailsIntent.data = Uri.parse("package:" + this.packageName)
    permissionDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    permissionDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    permissionDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivityWithFlagNewTask(permissionDetailsIntent)
}

fun Context.redirectToBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW)
    browserIntent.data = Uri.parse(url)
    startActivityWithFlagNewTask(browserIntent)
}

fun Context.shareBitmap(
    bitmap: Bitmap,
    description: String,
    popupLabel: String? = null
) {
    try {
        val cachePath = File(
            this.externalCacheDir,
            CACHE_DIR
        )
        val mkdirs = cachePath.mkdirs()

        if (mkdirs || cachePath.exists()) {
            bitmap.compressToPNG(
                stream = FileOutputStream("$cachePath/${SHARED_IMAGE_FILENAME}")
            )
        }

        val file = File(
            cachePath,
            SHARED_IMAGE_FILENAME
        )
        val contentUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID,
            file
        )

        if (contentUri != null) {
            this.shareUri(
                contentUri,
                description,
                popupLabel
            )
        }

    } catch (ignored: IOException) {
    }
}

private fun Context.shareUri(content: Uri, description: String, popupLabel: String? = null) {
    var intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(content, this.contentResolver.getType(content))
    intent.putExtra(Intent.EXTRA_STREAM, content)
    if (description.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_TEXT, description)
    }
    popupLabel?.also { intent = Intent.createChooser(intent, it) }
    startActivityWithFlagNewTask(intent)
}

fun Context.shareText(text: String, popupLabel: String? = null) {
    var intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = "text/plain"
    popupLabel?.also { intent = Intent.createChooser(intent, it) }
    startActivityWithFlagNewTask(intent)
}

/**
 * Return the formatted date and time based on local system settings
 */
fun Context.getLocalizedDateTime(timeInMillis: Long): String {
    val date = Date(timeInMillis)
    val dateFormat = DateFormat.getMediumDateFormat(this)
    val timeFormat = DateFormat.getTimeFormat(this)
    return dateFormat.format(date) + " " + timeFormat.format(date)
}
