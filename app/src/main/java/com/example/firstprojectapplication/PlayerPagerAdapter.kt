package com.example.firstprojectapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PlayerPagerAdapter(
    activity: FragmentActivity,
    private val playerNames: List<String>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = playerNames.size

    override fun createFragment(position: Int): Fragment {
        return PlayerFragment.newInstance(playerNames[position])
    }
}