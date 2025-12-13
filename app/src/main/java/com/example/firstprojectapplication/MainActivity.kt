package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var selectedMode: String? = null
    private lateinit var firebaseManager: FirebaseHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseManager = FirebaseHistoryManager()

        // Show welcome dialog when app starts
        showWelcomeDialog()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val btnTruth = findViewById<Button>(R.id.btnTruth)
        val btnDare = findViewById<Button>(R.id.btnDare)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnViewHistory = findViewById<Button>(R.id.btnViewHistory)
        val btnMultiPlayer = findViewById<Button>(R.id.btnMultiPlayer)

        btnTruth.setOnClickListener {
            selectedMode = "Truth"
            btnTruth.alpha = 1.0f
            btnDare.alpha = 0.5f
        }

        btnDare.setOnClickListener {
            selectedMode = "Dare"
            btnDare.alpha = 1.0f
            btnTruth.alpha = 0.5f
        }

        btnSubmit.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isNotEmpty() && selectedMode != null) {
                // Show confirmation dialog before proceeding
                showConfirmationDialog(name, selectedMode!!)
            } else {
                Toast.makeText(
                    this,
                    "Please enter name and choose Truth or Dare",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, PlayerHistoryActivity::class.java)
            startActivity(intent)
        }

        btnMultiPlayer.setOnClickListener {
            // Show info dialog about multi-player mode
            showMultiPlayerInfoDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showWelcomeDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎲 Welcome to Truth or Dare!")
            .setMessage("Ready to reveal secrets or face challenges?\n\nChoose wisely... there's no turning back!")
            .setPositiveButton("Let's Go!") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showConfirmationDialog(name: String, mode: String) {
        val message = if (mode == "Truth") {
            "$name, are you ready to reveal your deepest secrets?"
        } else {
            "$name, are you brave enough to face the dare?"
        }

        AlertDialog.Builder(this)
            .setTitle("⚠️ Are You Sure?")
            .setMessage(message)
            .setPositiveButton("Yes, I'm Ready!") { dialog, _ ->
                // Proceed to SecondPage
                val intent = Intent(this, SecondPage::class.java)
                intent.putExtra("playerName", name)
                intent.putExtra("mode", mode)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                dialog.dismiss()
            }
            .setNegativeButton("Wait, Let Me Think...") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showMultiPlayerInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎮 Multi-Player Mode")
            .setMessage("Play with 2-10 friends!\n\n• Add player names\n• Swipe between players\n• Each gets their own turn\n• All saved to history")
            .setPositiveButton("Start Setup") { dialog, _ ->
                val intent = Intent(this, MultiPlayerSetupActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}