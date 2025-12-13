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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showWelcomeDialog()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val btnTruth = findViewById<Button>(R.id.btnTruth)
        val btnDare = findViewById<Button>(R.id.btnDare)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
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
                showConfirmationDialog(name, selectedMode!!)
            } else {
                Toast.makeText(
                    this,
                    "Please enter name and choose Truth or Dare",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



        btnMultiPlayer.setOnClickListener {
            showMultiPlayerInfoDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showWelcomeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_welcome, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Let's Play!", null)
            .setCancelable(false)
            .create()

        dialog.show()
    }

    private fun showConfirmationDialog(name: String, mode: String) {
        val emoji = if (mode == "Truth") "🤐" else "💪"
        val title = if (mode == "Truth") "Ready for the Truth?" else "Ready for the Dare?"
        val message = if (mode == "Truth") {
            "$name, you chose TRUTH!\n\nTime to spill your secrets..."
        } else {
            "$name, you chose DARE!\n\nGet ready for a challenge..."
        }

        AlertDialog.Builder(this)
            .setTitle("$emoji $title")
            .setMessage(message)
            .setPositiveButton("I'm Ready!") { dialogInterface, _ ->
                val intent = Intent(this, SecondPage::class.java)
                intent.putExtra("playerName", name)
                intent.putExtra("mode", mode)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Go Back") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showMultiPlayerInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎮 Multi-Player Mode")
            .setMessage("Play with your friends!\n\n✓ Add 2-10 players\n✓ Swipe to switch turns\n✓ Everyone gets a challenge\n✓ All saves to history")
            .setPositiveButton("Start Setup") { dialogInterface, _ ->
                val intent = Intent(this, MultiPlayerSetupActivity::class.java)
                startActivity(intent)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }
}