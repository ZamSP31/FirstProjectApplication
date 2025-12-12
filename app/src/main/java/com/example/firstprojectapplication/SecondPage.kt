package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondPage : AppCompatActivity() {

    private val truthList = listOf(
        "What's the most embarrassing thing you've done while drunk?",
        "Have you ever had a crush on someone in this room?",
        "What's a secret you've never told anyone?",
        "Who was your first crush?",
        "Who was your first kiss, and how was it?"
    )

    private val dareList = listOf(
        "Do 10 jumping jacks!",
        "Run around the Main Building fountain three times yelling your favorite song.",
        "Act like a chicken for 10 seconds.",
        "Text someone 'I love pineapples 🍍'.",
        "Walk through España Blvd. waving at every person passing by."
    )

    private lateinit var firebaseManager: FirebaseHistoryManager
    private lateinit var currentChallenge: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        // Initialize Firebase manager
        firebaseManager = FirebaseHistoryManager()

        val name = intent.getStringExtra("playerName") ?: "Player"
        val mode = intent.getStringExtra("mode") ?: "Truth"

        val nameTview = findViewById<TextView>(R.id.nameTview)
        val passageTview = findViewById<TextView>(R.id.passageTview)
        val againButton = findViewById<Button>(R.id.againButton)
        val backButton = findViewById<Button>(R.id.homeButton)

        nameTview.text = "Hello, $name!"

        fun showRandomPassage() {
            val passage = if (mode == "Truth") truthList.random() else dareList.random()
            currentChallenge = passage
            passageTview.text = passage

            // Save to Firebase
            val playerData = PlayerData(
                playerName = name,
                mode = mode,
                challenge = passage
            )

            firebaseManager.savePlayerData(playerData) { success ->
                if (!success) {
                    Toast.makeText(
                        this,
                        "Failed to save to history",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        showRandomPassage()

        againButton.setOnClickListener {
            showRandomPassage()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}