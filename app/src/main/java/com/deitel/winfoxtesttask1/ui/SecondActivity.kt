package com.deitel.winfoxtesttask1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.deitel.winfoxtesttask1.adapters.ViewPagerAdapter
import com.deitel.winfoxtesttask1.databinding.ActivitySecondBinding
import com.deitel.winfoxtesttask1.di.DaggerAppComponent
import com.deitel.winfoxtesttask1.viewmodels.PlacesViewModel
import com.deitel.winfoxtesttask1.viewmodels.PlacesViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator


class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var viewModel: PlacesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = DaggerAppComponent.create().repository()
        val viewModelFactory = PlacesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlacesViewModel::class.java)

        val tabLayout = binding.myTabLayout
        var viewPager2 = binding.myViewPager2
        viewPager2.isUserInputEnabled = false

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when(position) {
                0-> {
                    tab.text = "First"
                }
                1 -> {
                    tab.text = "Second"

                }
            }
        }.attach()



    }



}