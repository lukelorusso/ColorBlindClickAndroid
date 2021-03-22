package com.lukelorusso.presentation.scenes.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseActivity
import com.lukelorusso.presentation.scenes.camera.CameraFragment
import com.lukelorusso.presentation.scenes.history.HistoryFragment
import com.lukelorusso.presentation.scenes.info.InfoFragment
import com.lukelorusso.presentation.scenes.preview.PreviewDialogFragment
import com.lukelorusso.presentation.view.MaybeScrollableViewPager
import com.mikhaellopez.ratebottomsheet.AskRateBottomSheet
import com.mikhaellopez.ratebottomsheet.RateBottomSheet
import com.mikhaellopez.ratebottomsheet.RateBottomSheetManager
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : ABaseActivity(R.layout.activity_main), AskRateBottomSheet.ActionListener {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // View
    private val adapter by lazy {
        MainPagerAdapter(
                supportFragmentManager,
                listOf(
                        InfoFragment.TAG,
                        CameraFragment.TAG,
                        HistoryFragment.TAG
                ) // this is usually a list of tab labels
        )
    }

    // Properties
    private var immersiveMode: Boolean = false
        set(value) {
            field = value
            if (value) enableImmersiveMode(hideNavBar = false, hideStatusBar = true, resize = false)
            else disableImmersiveMode()
        }
    private var lastPage = -1

    companion object {
        private const val PERMISSIONS_REQUEST_CAMERA = 10107
    }

    override fun onBackPressed() {
        when (val f = adapter.getItem(activityViewpager.currentItem)) { // pick the current fragment
            is InfoFragment -> if (!f.backPressHandled()) finish()
            is CameraFragment -> if (!f.backPressHandled()) finish()
            is HistoryFragment -> if (!f.backPressHandled()) finish()
            else -> finish() // just quit
        }
    }

    override fun onResume() {
        super.onResume()
        if (isBrokenViewVisible()) checkPermission()
        applyImmersiveMode()
    }

    fun applyImmersiveMode() {
        immersiveMode = isPageVisible(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        val duration = resources.getInteger(R.integer.splash_screen_duration)
        splashScreenLogo.setAlphaWithAnimation(0F, 1F, duration.toLong()) {
            initializationActivity(
                    savedInstanceState
            )
        }
    }

    private fun initializationActivity(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            checkPermission()
        }

        RateBottomSheetManager(this)
                .setInstallDays(2)
                .setLaunchTimes(4)
                .setRemindInterval(1)
                .setShowAskBottomSheet(false)
                .setShowLaterButton(true)
                .setShowCloseButtonIcon(false)
                .monitor()
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) { // permission NOT granted
            if (ActivityCompat.shouldShowRequestPermissionRationale( // the permission was already denied before
                            this,
                            Manifest.permission.CAMERA
                    )
            ) {
                showBrokenView()

            } else { // we can request the permission
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSIONS_REQUEST_CAMERA
                )
            }

        } else { // permission granted
            hideBrokenView()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            resultArray: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                if (resultArray.isNotEmpty() // if the request is cancelled, resultArray is empty
                        && resultArray[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    hideBrokenView()
                } else {
                    showBrokenView()
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun isPageVisible(position: Int): Boolean =
            !isBrokenViewVisible() && activityViewpager.currentItem == position

    private fun isBrokenViewVisible(): Boolean =
            activityBrokenView.visibility == View.VISIBLE
                    && activityViewpager.visibility == View.GONE

    private fun showBrokenView() {
        immersiveMode = false
        splashScreen.visibility = View.GONE
        activityViewpager.visibility = View.GONE
        activityBrokenView.visibility = View.VISIBLE
        activityPermissionsLabel.text = resources.getString(R.string.permissions_label).toHtml()
        activityBrokenView.setOnClickListener { v ->
            if (ContextCompat.checkSelfPermission(v.context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
            ) { // permission is granted
                hideBrokenView()
            } else {
                gotoAppDetailsSettings()
            }
        }
    }

    private fun hideBrokenView() {
        immersiveMode = true
        activityBrokenView.visibility = View.GONE
        activityViewpager.visibility = View.VISIBLE
        initViewPager()
    }

    private fun initViewPager() {
        activityViewpager.adapter = adapter
        activityViewpager.offscreenPageLimit = 3
        activityViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) { // if scroll is finished
                    val newPage = activityViewpager.currentItem
                    immersiveMode = newPage == 1 // only camera page is immersive
                    if (newPage == 2 && newPage != lastPage) {
                        val f = adapter.getItem(newPage)
                        (f as? HistoryFragment)?.intentLoadData?.onNext(Unit)
                    }
                    when (newPage) {
                        0 -> activityViewpager.direction =
                                MaybeScrollableViewPager.SwipeDirection.RIGHT // info page cannot scroll left... this will hide the swiping feedback when you can't scroll anymore
                        adapter.count - 1 -> activityViewpager.direction =
                                MaybeScrollableViewPager.SwipeDirection.LEFT // history page cannot scroll right, because swipe right gesture is set to delete an item from history
                        else -> activityViewpager.direction =
                                MaybeScrollableViewPager.SwipeDirection.ALL
                    }
                    lastPage = newPage
                }
            }

            override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) {}

            override fun onPageSelected(p: Int) {}
        })
        gotoCamera()

        // Show bottom sheet if meets conditions
        RateBottomSheet.showRateBottomSheetIfMeetsConditions(
                this,
                this
        )
    }

    fun hideSplashScreen() {
        splashScreen.visibility = View.GONE
    }

    fun gotoInfo() {
        activityViewpager.currentItem = 0
    }

    fun gotoCamera() {
        activityViewpager.currentItem = 1
    }

    fun gotoHistory() {
        activityViewpager.currentItem = 2
    }

    fun showColorPreviewDialog(color: Color) =
            PreviewDialogFragment.newInstance(gson.toJson(color), !isPageVisible(1))
                    .show(supportFragmentManager, PreviewDialogFragment.TAG)

    //region RateBottomSheet.ActionListener
    override fun onRateClickListener() =
            trackerHelper.track(this, TrackerHelper.Actions.RATING_YES_CLICKED)

    override fun onNoClickListener() =
            trackerHelper.track(this, TrackerHelper.Actions.RATING_NO_CLICKED)
    //endregion

}
