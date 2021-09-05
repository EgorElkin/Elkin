package com.example.developerslife.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.developerslife.ui.latest.LatestFragment
import com.example.developerslife.ui.random.RandomFragment
import com.example.developerslife.ui.top.TopFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    companion object{
        private const val ITEMS_COUNT = 3
        private const val LATEST_FRAGMENT_POSITION = 0
        private const val TOP_FRAGMENT_POSITION = 1
        private const val RANDOM_FRAGMENT_POSITION = 2
    }

    override fun getItemCount(): Int {
        return ITEMS_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            LATEST_FRAGMENT_POSITION -> LatestFragment()
            TOP_FRAGMENT_POSITION -> TopFragment()
            RANDOM_FRAGMENT_POSITION -> RandomFragment()
            else -> throw IllegalArgumentException("position is not one of 1, 2, 3")
        }
    }
}