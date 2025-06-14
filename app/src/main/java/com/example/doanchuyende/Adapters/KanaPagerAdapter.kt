package com.example.doanchuyende.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.doanchuyende.Fragments.HiraganaFragment
import com.example.doanchuyende.Fragments.KatakanaFragment

class KanaPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HiraganaFragment()
            1 -> KatakanaFragment()
            else -> throw IllegalStateException("Invalid adapter position $position")
        }
    }
} 