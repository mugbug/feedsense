package com.example.pedro.feedsense.modules.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class HomePagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    var pages = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }
}