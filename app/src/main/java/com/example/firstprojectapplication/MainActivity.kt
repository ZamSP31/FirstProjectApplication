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

    private var selectedMode: String? = null // "Truth" or "Dare"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        Values

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val btnTruth = findViewById<Button>(R.id.btnTruth)
        val btnDare = findViewById<Button>(R.id.btnDare)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)



//        choices for fate
        btnTruth.setOnClickListener {
            selectedMode = "Truth"
        }

        btnDare.setOnClickListener {
            selectedMode = "Dare"
        }

//        condition for validation the user cannot submit if not picked a button
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


