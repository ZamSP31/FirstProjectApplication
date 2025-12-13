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
        "Who was your first kiss, and how was it?",
        "What's the biggest lie you've ever told?",
        "What's your biggest fear?",
        "Have you ever cheated on a test or exam?",
        "What's the weirdest dream you've ever had?",
        "Who in this room would you most like to make out with?",

        "Have you ever had a crush on a teacher?",
        "What's the longest you've gone without showering?",
        "Have you ever been in love?",
        "What's your biggest regret in a relationship?",
        "Have you ever ghosted someone? Why?",
        "What's the most childish thing you still do?",
        "Have you ever lied to your best friend?",
        "What's something you're glad your parents don't know about you?",
        "What's the most trouble you've been in?",
        "Have you ever sent a risky text to the wrong person?",
        "What's the most embarrassing thing in your search history?",
        "Have you ever had a wardrobe malfunction in public?",
        "What's the weirdest place you've ever cried?",
        "Have you ever pretended to like a gift you actually hated?",
        "What's something you've done that you'd never want your family to know?",
        "Have you ever stalked someone on social media?",
        "If you could be invisible for a day, what would you do?",
        "What's the worst date you've ever been on?",
        "Have you ever peed in a pool?",
        "What's the grossest thing you've ever eaten?",
        "Have you ever farted in public and blamed someone else?",
        "What's your most embarrassing childhood memory?",
        "Have you ever picked your nose in public?",
        "What's the dumbest thing you've ever done for attention?"

    )

    private val dareList = listOf(
        // Physical Challenges
        "Do 10 jumping jacks!",
        "Do 20 push-ups right now!",
        "Hold a plank position for 1 minute!",
        "Do your best TikTok dance!",
        "Spin around 10 times and try to walk in a straight line!",
        "Do the worm dance move!",
        "Attempt to do a cartwheel!",
        "Act like a chicken for 30 seconds!",

        // Social Media & Texting
        "Text someone 'I love pineapples 🍍'.",
        "Post an embarrassing selfie on your story!",
        "Let someone else post anything on your social media!",
        "Send a voice memo to your crush saying hi!",
        "Text your ex 'Hey, what's up?'",
        "Change your profile picture to an embarrassing childhood photo!",
        "Call a random contact and sing 'Happy Birthday' to them!",

        // Performance
        "Sing a song chosen by the group!",
        "Do your best celebrity impression!",
        "Speak in an accent for the next 3 rounds!",
        "Freestyle rap about everyone in the room!",
        "Talk without closing your mouth for 2 minutes!",
        "Yell out the first word that comes to your mind!",
        "Imitate a baby being born!",

        // Creative & Funny
        "Wear your clothes backwards for the rest of the game!",
        "Let someone draw on your face with a marker!",
        "Eat a spoonful of hot sauce or mustard!",
        "Smell everyone's feet and rank them from best to worst!",
        "Let the group give you a new hairstyle!",
        "Do a dramatic reading of the last text you sent!",

        // Public Challenges
        "Run around the Main Building fountain three times yelling your favorite song!",
        "Walk through España Blvd. waving at every person passing by!",
        "Go outside and dance with no music for 1 minute!",
        "Knock on a random door and ask if they have seen your pet unicorn!",

        // Group Interaction
        "Let someone tickle you for 30 seconds!",
        "Switch clothes with someone in the room!",
        "Give everyone in the room a compliment!",
        "Share your most embarrassing photo on your phone!",
        "Let the group read your last 5 text messages out loud!",
        "Do whatever the person to your left tells you to do (within reason)!"
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

        AlertDialog.Builder(this, R.style.CustomAlertDialog)
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

        AlertDialog.Builder(this, R.style.CustomAlertDialog)
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
        AlertDialog.Builder(this, R.style.CustomAlertDialog)
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