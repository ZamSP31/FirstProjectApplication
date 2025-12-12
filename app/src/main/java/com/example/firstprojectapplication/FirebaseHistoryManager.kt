package com.example.firstprojectapplication

import com.google.firebase.database.*

class FirebaseHistoryManager {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val historyRef = database.child("playerHistory")

    // Save player data to Firebase
    fun savePlayerData(playerData: PlayerData, onComplete: (Boolean) -> Unit = {}) {
        val key = historyRef.push().key ?: return
        val playerWithId = playerData.copy(id = key)

        historyRef.child(key).setValue(playerWithId)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    // Get all player history
    fun getPlayerHistory(onDataReceived: (List<PlayerData>) -> Unit) {
        historyRef.orderByChild("timestamp")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playerList = mutableListOf<PlayerData>()

                    for (childSnapshot in snapshot.children) {
                        val player = childSnapshot.getValue(PlayerData::class.java)
                        player?.let { playerList.add(it) }
                    }

                    // Reverse to show newest first
                    onDataReceived(playerList.reversed())
                }

                override fun onCancelled(error: DatabaseError) {
                    onDataReceived(emptyList())
                }
            })
    }

    // Real-time listener for history updates
    fun listenToHistory(onDataChanged: (List<PlayerData>) -> Unit) {
        historyRef.orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playerList = mutableListOf<PlayerData>()

                    for (childSnapshot in snapshot.children) {
                        val player = childSnapshot.getValue(PlayerData::class.java)
                        player?.let { playerList.add(it) }
                    }

                    onDataChanged(playerList.reversed())
                }

                override fun onCancelled(error: DatabaseError) {
                    onDataChanged(emptyList())
                }
            })
    }

    // Clear all history
    fun clearHistory(onComplete: (Boolean) -> Unit = {}) {
        historyRef.removeValue()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    // Delete specific entry
    fun deleteEntry(playerId: String, onComplete: (Boolean) -> Unit = {}) {
        historyRef.child(playerId).removeValue()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}
