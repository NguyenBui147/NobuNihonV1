package com.example.doanchuyende.Activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.Adapters.KanaPagerAdapter
import com.example.doanchuyende.databinding.ActivityKanaAlphabetBinding
import com.google.android.material.tabs.TabLayoutMediator

class KanaAlphabetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKanaAlphabetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanaAlphabetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = KanaPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            tab.text = when(position) {
                0 -> "Hiragana"
                1 -> "Katakana"
                else -> ""
            }
        }.attach()



    }
} 