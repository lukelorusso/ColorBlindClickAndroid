package com.lukelorusso.presentation.ui.main

import androidx.fragment.app.*
import com.lukelorusso.presentation.ui.camera.CameraFragment
import java.lang.ref.WeakReference
import com.lukelorusso.presentation.ui.v3.history.HistoryFragment as HistoryFragmentV3
import com.lukelorusso.presentation.ui.v3.info.InfoFragment as InfoFragmentV3

/**
 * @author LukeLorusso on 07-01-2019.
 */
class MainPagerAdapter(private var fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitleList = listOf(
        InfoFragmentV3.TAG,
        CameraFragment.TAG,
        HistoryFragmentV3.TAG
    )

    // It's important to keep WeakReference to Fragments,
    // otherwise the risk is to lose the fragment's instance
    private val weakFragmentList: ArrayList<WeakReference<Fragment>?> =
        ArrayList(tabTitleList.map { null })

    override fun getCount(): Int {
        return tabTitleList.size
    }

    override fun getItem(position: Int): Fragment = instanceOf(position)

    private fun instanceOf(position: Int): Fragment = weakFragmentList[position]?.get()
        ?: fragmentManager.fragments.getOrNull(position)
            ?.also { weakFragmentList[position] = WeakReference(it) }
        ?: newInstanceAt(position)
            .also { weakFragmentList[position] = WeakReference(it) }

    private fun newInstanceAt(position: Int): Fragment = when (position) {
        0 -> InfoFragmentV3.newInstance()
        1 -> CameraFragment.newInstance()
        2 -> HistoryFragmentV3.newInstance()
        else -> Fragment()
    }

    fun updateFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitleList.getOrNull(position) ?: ""
    }
}
