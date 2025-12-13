package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GamePagerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var playerNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_pager)

        playerNames = intent.getStringArrayListExtra("playerNames") ?: emptyList()

        if (playerNames.isEmpty()) {
            finish()
            return
        }

        viewPager = findViewById(R.id.playerViewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val btnHome = findViewById<Button>(R.id.btnHomeFromGame)
        val currentPlayerText = findViewById<TextView>(R.id.currentPlayerIndicator)

        // Setup ViewPager
        val adapter = PlayerPagerAdapter(this, playerNames)
        viewPager.adapter = adapter

        // Link TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = playerNames[position]
        }.attach()

        // Update current player indicator
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPlayerText.text = "Current: ${playerNames[position]}"
            }
        })

        currentPlayerText.text = "Current: ${playerNames[0]}"

        // Home button
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}