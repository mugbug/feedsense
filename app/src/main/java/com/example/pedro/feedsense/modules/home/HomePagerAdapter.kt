package com.example.pedro.feedsense.modules.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomePagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {



    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> LineChartFragment.newInstance()
            else -> HomeReactionsFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}