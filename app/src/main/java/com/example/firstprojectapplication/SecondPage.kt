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
            "How honest were you with your truth?"
        } else {
            "How well did you complete the dare?"
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("✅ Challenge Complete!")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val rating = ratingBar.rating
                showThankYouDialog(rating)
            }
            .setNegativeButton("Skip") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showThankYouDialog(rating: Float) {
        val message = when {
            rating >= 4.0f -> "Awesome! You're a true champion! 🌟"
            rating >= 2.5f -> "Good effort! Keep playing! 👍"
            else -> "Nice try! Maybe next time! 😊"
        }

        AlertDialog.Builder(this)
            .setTitle("Thanks for Rating!")
            .setMessage(message)
            .setPositiveButton("Play Again") { _, _ ->
                // Stay on current page for another round
            }
            .setNeutralButton("Go Home") { _, _ ->
                navigateHome()
            }
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Leave Game?")
            .setMessage("Are you sure you want to go back to the home screen?")
            .setPositiveButton("Yes") { _, _ ->
                navigateHome()
            }
            .setNegativeButton("No") { dialog, _ ->
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