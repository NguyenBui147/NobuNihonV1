package com.example.doanchuyende.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.doanchuyende.Fragments.VocabularyFragment

class VocabularyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5 // N5 to N1

    override fun createFragment(position: Int): Fragment {
        return VocabularyFragment.newInstance()
    }
} 