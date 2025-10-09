package com.lukelorusso.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.ActivityMainBinding
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.base.SwipeViewPager
import com.lukelorusso.presentation.ui.capture.CaptureFragment
import com.lukelorusso.presentation.ui.history.HistoryFragment
import com.lukelorusso.presentation.ui.info.InfoFragment
import com.lukelorusso.presentation.ui.preview.PreviewDialogFragment
import com.lukelorusso.presentation.ui.settings.SettingsDialogFragment
import androidx.core.view.isVisible
import androidx.core.view.isGone


class MainActivity : AppCompatActivity() {

    // View
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val pagerAdapter by lazy { MainPagerAdapter(supportFragmentManager) }

    // Properties
    private var lastPage = -1

    companion object {
        private const val PERMISSION_CAMERA_REQUEST_CODE = 10107
        private const val OFFSCREEN_PAGE_LIMIT = 3
    }

    private val handleOnBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (val f =
                pagerAdapter.getItem(binding.viewPager.currentItem)) { // pick the current fragment
                is InfoFragment -> if (!f.onBackPressHandled()) finish()
                is CaptureFragment -> if (!f.onBackPressHandled()) finish()
                is HistoryFragment -> if (!f.onBackPressHandled()) finish()
                else -> finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isBrokenViewVisible()) checkPermission()
    }

    @SuppressLint("MissingSuperCall") // No call for super(): bug on API Level > 11
    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, handleOnBackPressed)
        val duration = resources.getInteger(R.integer.splash_screen_duration)
        binding.splash.logo.setAlphaWithAnimation(0F, 1F, duration.toLong()) {
            initializeActivity(savedInstanceState)
        }
    }

    private fun initializeActivity(savedInstanceState: Bundle?) {
        savedInstanceState?.also {
            pagerAdapter.updateFragmentManager(supportFragmentManager)
        }

        checkPermission()
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) { // permission NOT granted
            if (ActivityCompat.shouldShowRequestPermissionRationale( // the permission was already denied before
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                runOnUiThread { showBrokenView() }

            } else { // we can request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_CAMERA_REQUEST_CODE
                )
            }

        } else { // permission granted
            runOnUiThread { hideBrokenView() }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        resultArray: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, resultArray)
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (resultArray.isNotEmpty() // if the request is cancelled, resultArray is empty
                    && resultArray[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    runOnUiThread { hideBrokenView() }
                } else {
                    runOnUiThread { showBrokenView() }
                }
            }
        }
    }

    private fun isBrokenViewVisible(): Boolean =
        binding.brokenScreen.root.isVisible && binding.viewPager.isGone

    private fun showBrokenView() {
        binding.splash.root.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
        binding.brokenScreen.apply {
            description.text = resources.getString(R.string.permissions_label).toHtml()
            root.visibility = View.VISIBLE
            root.setOnClickListener { view ->
                if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                ) { // permission is granted
                    hideBrokenView()
                } else {
                    gotoAppDetailsSettings()
                }
            }
        }
    }

    private fun hideBrokenView() {
        binding.brokenScreen.root.visibility = View.GONE
        binding.viewPager.visibility = View.VISIBLE
        initializeViewPager()
    }

    private fun initializeViewPager() {
        binding.viewPager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = OFFSCREEN_PAGE_LIMIT
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) { // if scroll is finished
                        val newPage = currentItem
                        if (newPage != lastPage) {
                            val f = pagerAdapter.getItem(newPage)
                            (f as? HistoryFragment)?.reloadData()
                            (f as? CaptureFragment)?.reloadData()
                        }
                        direction = when (newPage) {
                            0 ->
                                SwipeViewPager.SwipeDirection.RIGHT // info page cannot scroll left... this will hide the swiping feedback when you can't scroll anymore
                            pagerAdapter.count - 1 ->
                                SwipeViewPager.SwipeDirection.LEFT // history page cannot scroll right, because swipe right gesture is set to delete an item from history
                            else ->
                                SwipeViewPager.SwipeDirection.ALL
                        }
                        lastPage = newPage
                    }
                }

                override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) {}

                override fun onPageSelected(p: Int) {}
            })
        }
        gotoCamera()
    }

    fun hideSplashScreen() {
        binding.splash.root.visibility = View.GONE
    }

    fun gotoInfo() {
        binding.viewPager.currentItem = 0
    }

    fun gotoCamera() {
        binding.viewPager.currentItem = 1
    }

    fun gotoHistory() {
        binding.viewPager.currentItem = 2
    }

    fun showColorPreviewDialog(serializedColor: String) =
        PreviewDialogFragment.newInstance(serializedColor)
            .show(supportFragmentManager, PreviewDialogFragment.TAG)

    fun showSettingsDialog() =
        SettingsDialogFragment.newInstance()
            .show(supportFragmentManager, SettingsDialogFragment.TAG)
}
