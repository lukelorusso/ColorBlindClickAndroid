package com.lukelorusso.colorblindclick.presentation.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import com.lukelorusso.colorblindclick.presentation.ui.capture.CaptureFragment
import com.lukelorusso.colorblindclick.presentation.ui.history.HistoryFragment
import com.lukelorusso.colorblindclick.presentation.ui.info.InfoFragment
import java.lang.ref.WeakReference

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class MainPagerAdapter(private var fragmentManager: FragmentManager) : PagerAdapter() {

    private val tabTitleList = listOf(
        InfoFragment.TAG,
        CaptureFragment.TAG,
        HistoryFragment.TAG
    )

    // It's important to keep WeakReference to Fragments,
    // otherwise the risk is to lose the fragment's instance
    private val weakFragmentList: ArrayList<WeakReference<Fragment>?> =
        ArrayList(tabTitleList.map { null })

    private var currentTransaction: FragmentTransaction? = null
    private var primaryItem: Fragment? = null

    override fun getCount(): Int = tabTitleList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = instanceOf(position)

        if (!fragment.isAdded) {
            (currentTransaction ?: fragmentManager.beginTransaction().also { currentTransaction = it })
                .add(container.id, fragment, fragmentTagAt(position))
                .setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        }

        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val fragment = item as Fragment
        (currentTransaction ?: fragmentManager.beginTransaction().also { currentTransaction = it })
            .remove(fragment)
        if (fragment == primaryItem) primaryItem = null
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, item: Any) {
        val fragment = item as Fragment
        if (fragment != primaryItem) {
            (currentTransaction ?: fragmentManager.beginTransaction().also { currentTransaction = it }).apply {
                primaryItem?.let { setMaxLifecycle(it, Lifecycle.State.STARTED) }
                setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            }
            primaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        currentTransaction?.commitNowAllowingStateLoss()
        currentTransaction = null
    }

    override fun isViewFromObject(view: View, item: Any): Boolean = (item as Fragment).view == view

    fun getItem(position: Int): Fragment = instanceOf(position)

    private fun instanceOf(position: Int): Fragment = weakFragmentList[position]?.get()
        ?: fragmentManager.findFragmentByTag(fragmentTagAt(position))
            ?.also { weakFragmentList[position] = WeakReference(it) }
        ?: fragmentManager.fragments.getOrNull(position)
            ?.also { weakFragmentList[position] = WeakReference(it) }
        ?: newInstanceAt(position)
            .also { weakFragmentList[position] = WeakReference(it) }

    private fun newInstanceAt(position: Int): Fragment = when (position) {
        0 -> InfoFragment.newInstance()
        1 -> CaptureFragment.newInstance()
        2 -> HistoryFragment.newInstance()
        else -> Fragment()
    }

    private fun fragmentTagAt(position: Int): String = "MainPagerAdapter:$position"

    fun updateFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitleList.getOrNull(position) ?: ""
    }
}
