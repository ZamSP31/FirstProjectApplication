package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondPage : AppCompatActivity() {

    private val truthList = listOf(
        "What's the most embarrassing thing you've done?",
        "Have you ever had a crush on someone in this room?",
        "What's a secret you've never told anyone?",
        "Who was your first crush?",
        "What's the biggest lie you've ever told?",
        "What's your biggest fear?",
        "Have you ever cheated on a test?",
        "What's the weirdest dream you've had?",
        "Have you ever had a crush on a teacher?",
        "Have you ever been in love?"

    )

    private val dareList = listOf(
        "Do 10 jumping jacks!",
        "Do 20 push-ups right now!",
        "Act like a chicken for 30 seconds!",
        "Sing a song chosen by the group!",
        "Do your best TikTok dance!",
        "Speak in an accent for the next 3 rounds!",
        "Do your best celebrity impression!",
        "Give everyone in the room a compliment!",
        "Do a cartwheel!",
        "Dance with no music for 1 minute!"
    )

    private lateinit var currentChallenge: String
    private lateinit var currentName: String
    private lateinit var currentMode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        currentName = intent.getStringExtra("playerName") ?: "Player"
        currentMode = intent.getStringExtra("mode") ?: "Truth"

        val nameTview = findViewById<TextView>(R.id.nameTview)
        val passageTview = findViewById<TextView>(R.id.passageTview)
        val againButton = findViewById<Button>(R.id.againButton)
        val backButton = findViewById<Button>(R.id.homeButton)
        val completeButton = findViewById<Button>(R.id.completeButton)

        nameTview.text = "Hello, $currentName!"

        // Handle back button press with new API
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

        fun showRandomPassage() {
            val passage = if (currentMode == "Truth") truthList.random() else dareList.random()
            currentChallenge = passage
            passageTview.text = passage
        }

        showRandomPassage()

        againButton.setOnClickListener {
            showRandomPassage()
        }

        completeButton.setOnClickListener {
            showCompletionDialog()
        }

        backButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showCompletionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_completion, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val messageText = dialogView.findViewById<TextView>(R.id.completionMessage)

        messageText.text = if (currentMode == "Truth") {
            "How honest were you?"
        } else {
            "How epic was your dare?"
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val rating = ratingBar.rating
                showThankYouDialog(rating)
            }
            .setNegativeButton("Skip") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showThankYouDialog(rating: Float) {
        val (emoji, message) = when {
            rating >= 4.5f -> "🏆" to "LEGENDARY! You crushed it!"
            rating >= 3.5f -> "🌟" to "Awesome! Great job!"
            rating >= 2.5f -> "👍" to "Not bad! Keep it up!"
            rating >= 1.5f -> "😅" to "Nice try! Better luck next time!"
            else -> "🐔" to "Chicken! Do better next round!"
        }

        AlertDialog.Builder(this)
            .setTitle("$emoji Rating Recorded!")
            .setMessage(message)
            .setPositiveButton("Play Again") { _, _ ->
                // Stay
            }
            .setNeutralButton("Go Home") { _, _ ->
                navigateHome()
            }
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Leave Game?")
            .setMessage("Are you sure you want to quit?\n\nYour current challenge will be lost!")
            .setPositiveButton("Yes, Leave") { _, _ ->
                navigateHome()
            }
            .setNegativeButton("Stay") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}