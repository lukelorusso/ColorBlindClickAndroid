package com.lukelorusso.presentation.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.view.View
import androidx.core.app.ActivityCompat
import java.util.*

@SuppressLint("ObsoleteSdkInt")
fun Activity.enableImmersiveMode(
    hideNavBar: Boolean,
    hideStatusBar: Boolean,
    resize: Boolean
) {
    if (!hideNavBar && !hideStatusBar) {
        disableImmersiveMode()
        return
    }

    var uiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (hideNavBar && resize) uiVisibility = (uiVisibility
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    if (hideStatusBar && resize) uiVisibility = (uiVisibility
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    if (hideNavBar) uiVisibility = (uiVisibility
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    if (hideStatusBar) uiVisibility = (uiVisibility
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) uiVisibility = (uiVisibility
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    this.window.decorView.systemUiVisibility = uiVisibility
}

fun Activity.disableImmersiveMode() {
    this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

fun Context.startActivityWithFlagNewTask(intent: Intent) {
    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

fun Context.dpToPixel(dp: Float): Float =
    dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Context.pixelToDp(px: Int): Float =
    px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

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

fun Context.getStatusBarHeight(): Int {
    return resources.getDimensionPixelSize(
        resources.getIdentifier("status_bar_height", "dimen", "android")
    )
}

fun Context.redirectToBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW)
    browserIntent.data = Uri.parse(url)
    startActivityWithFlagNewTask(browserIntent)
}

fun Context.shareImageAndText(content: Uri, text: String, popupLabel: String?) {
    var intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(content, this.contentResolver.getType(content))
    intent.putExtra(Intent.EXTRA_STREAM, content)
    if (text.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_TEXT, text)
    }
    popupLabel?.also { intent = Intent.createChooser(intent, it) }
    startActivityWithFlagNewTask(intent)
}

fun Context.shareText(text: String, popupLabel: String?) {
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
