package com.example.firstprojectapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayerHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerHistoryAdapter
    private lateinit var firebaseManager: FirebaseHistoryManager
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_history)

        firebaseManager = FirebaseHistoryManager()

        recyclerView = findViewById(R.id.recyclerViewHistory)
        emptyView = findViewById(R.id.emptyView)
        val btnBack = findViewById<Button>(R.id.btnBackToMain)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlayerHistoryAdapter(emptyList()) { playerData ->
            // Handle long press to delete
            showDeleteDialog(playerData)
        }
        recyclerView.adapter = adapter

        // Load data from Firebase with real-time updates
        loadHistoryData()

        // Back button
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadHistoryData() {
        // Real-time listener - automatically updates when data changes
        firebaseManager.listenToHistory { history ->
            if (history.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateData(history)
            }
        }
    }

    private fun showDeleteDialog(playerData: PlayerData) {
        AlertDialog.Builder(this)
            .setTitle("🗑️ Delete Entry")
            .setMessage("Delete ${playerData.playerName}'s ${playerData.mode} challenge?\n\n\"${playerData.challenge}\"")
            .setPositiveButton("Delete") { _, _ ->
                firebaseManager.deleteEntry(playerData.id) { success ->
                    if (success) {
                        Toast.makeText(this, "✅ Entry deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "❌ Failed to delete", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Clear All History") { _, _ ->
                showClearAllDialog()
            }
            .show()
    }

    private fun showClearAllDialog() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Clear All History")
            .setMessage("This will permanently delete ALL player history!\n\nAre you absolutely sure?")
            .setPositiveButton("Yes, Delete Everything") { _, _ ->
                firebaseManager.clearHistory { success ->
                    if (success) {
                        Toast.makeText(this, "🗑️ All history cleared", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "❌ Failed to clear history", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No, Keep It") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}