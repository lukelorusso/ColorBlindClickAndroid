package com.lukelorusso.presentation_old.scenes.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lukelorusso.presentation_old.scenes.camera.CameraFragment
import com.lukelorusso.presentation_old.scenes.history.HistoryFragment
import com.lukelorusso.presentation_old.scenes.info.InfoFragment
import java.lang.ref.WeakReference

/**
 * @author LukeLorusso on 07-01-2019.
 */
class MainPagerAdapter(
    fm: FragmentManager,
    private val tabTitleList: List<String>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // It's important to keep WeakReference to Fragments.
    // Otherwise the risk is to lose the fragment's instance (empty context)
    private val weakFragmentList: ArrayList<WeakReference<Fragment>?> =
        ArrayList(tabTitleList.map { null })

    override fun getCount(): Int {
        return tabTitleList.size
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> instanceOf(position, InfoFragment.newInstance())
        1 -> instanceOf(position, CameraFragment.newInstance())
        2 -> instanceOf(position, HistoryFragment.newInstance())
        else -> Fragment()
    }

    private fun instanceOf(position: Int, newInstance: Fragment): Fragment =
        weakFragmentList[position]?.get() ?: newInstance.also {
            weakFragmentList[position] = WeakReference(it)
        }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitleList.getOrNull(position) ?: ""
    }
}
