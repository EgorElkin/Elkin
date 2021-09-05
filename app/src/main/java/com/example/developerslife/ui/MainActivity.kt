package com.example.developerslife.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.developerslife.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    private val adapter = ViewPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setUpAdapter()
    }

    private fun initUI() {
        viewPager = findViewById(R.id.viewPagerMain)
        tabLayout = findViewById(R.id.tabLayoutMain)
    }

    private fun setUpAdapter(){
        viewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, viewPager!!){ tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.tab_latest_label)
                1 -> getString(R.string.tab_top_label)
                2 -> getString(R.string.tab_random_label)
                else -> throw IllegalArgumentException("TabLayout position is less then 0 or more then 2")
            }
        }.attach()
    }
}