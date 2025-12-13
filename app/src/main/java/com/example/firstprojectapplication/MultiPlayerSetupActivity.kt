package com.example.firstprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MultiPlayerSetupActivity : AppCompatActivity() {

    private val playerNames = mutableListOf<String>()
    private lateinit var adapter: PlayerSetupAdapter
    private lateinit var nameInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_setup)

        // Show setup instructions
        showSetupInstructionsDialog()

        nameInput = findViewById(R.id.playerNameInput)
        val btnAddPlayer = findViewById<Button>(R.id.btnAddPlayer)
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        val btnBack = findViewById<Button>(R.id.btnBackFromSetup)
        val recyclerView = findViewById<RecyclerView>(R.id.playerListRecyclerView)

        // Setup RecyclerView for player list
        adapter = PlayerSetupAdapter(playerNames) { position ->
            // Show confirmation before removing player
            showRemovePlayerDialog(position)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add player button
        btnAddPlayer.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isNotEmpty()) {
                if (playerNames.size < 10) {
                    playerNames.add(name)
                    adapter.notifyItemInserted(playerNames.size - 1)
                    nameInput.text.clear()
                    Toast.makeText(this, "✅ $name added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Maximum 10 players allowed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        // Start game button
        btnStartGame.setOnClickListener {
            if (playerNames.size >= 2) {
                showStartGameDialog()
            } else {
                Toast.makeText(this, "Add at least 2 players to start", Toast.LENGTH_SHORT).show()
            }
        }

        // Back button
        btnBack.setOnClickListener {
            if (playerNames.isNotEmpty()) {
                showExitConfirmationDialog()
            } else {
                finish()
            }
        }
    }

    private fun showSetupInstructionsDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎮 Multi-Player Setup")
            .setMessage("Add 2-10 players to start!\n\n• Enter each player's name\n• Tap 'Add' to add them\n• Tap on a name to remove\n• Start when ready!")
            .setPositiveButton("Got It!") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showRemovePlayerDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Remove Player?")
            .setMessage("Remove ${playerNames[position]} from the game?")
            .setPositiveButton("Remove") { _, _ ->
                val removedName = playerNames[position]
                playerNames.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "$removedName removed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showStartGameDialog() {
        val playerList = playerNames.joinToString("\n• ")

        AlertDialog.Builder(this)
            .setTitle("🎉 Start Game?")
            .setMessage("Ready to play with ${playerNames.size} players?\n\n• $playerList\n\nLet the games begin!")
            .setPositiveButton("Let's Go!") { _, _ ->
                val intent = Intent(this, GamePagerActivity::class.java)
                intent.putStringArrayListExtra("playerNames", ArrayList(playerNames))
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Wait") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Exit Setup?")
            .setMessage("You have ${playerNames.size} player(s) added. Exit without starting the game?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("No, Stay") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onBackPressed() {
        if (playerNames.isNotEmpty()) {
            showExitConfirmationDialog()
        } else {
            super.onBackPressed()
        }
    }
}