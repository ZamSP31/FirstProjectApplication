package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var selectedMode: String? = null
    private lateinit var firebaseManager: FirebaseHistoryManager // Changed from historyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase manager
        firebaseManager = FirebaseHistoryManager()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val btnTruth = findViewById<Button>(R.id.btnTruth)
        val btnDare = findViewById<Button>(R.id.btnDare)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnViewHistory = findViewById<Button>(R.id.btnViewHistory)

        btnTruth.setOnClickListener {
            selectedMode = "Truth"
        }

        btnDare.setOnClickListener {
            selectedMode = "Dare"
        }

        btnSubmit.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isNotEmpty() && selectedMode != null) {
                val intent = Intent(this, SecondPage::class.java)
                intent.putExtra("playerName", name)
                intent.putExtra("mode", selectedMode)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}