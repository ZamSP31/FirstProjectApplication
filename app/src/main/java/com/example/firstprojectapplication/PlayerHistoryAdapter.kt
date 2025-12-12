package com.example.firstprojectapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerHistoryAdapter(
    private var playerList: List<PlayerData>,
    private val onItemLongClick: (PlayerData) -> Unit = {} // For delete functionality
) : RecyclerView.Adapter<PlayerHistoryAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameText: TextView = itemView.findViewById(R.id.playerNameText)
        val modeText: TextView = itemView.findViewById(R.id.modeText)
        val challengeText: TextView = itemView.findViewById(R.id.challengeText)
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_history, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList[position]

        holder.playerNameText.text = player.playerName
        holder.modeText.text = player.mode
        holder.challengeText.text = player.challenge

        // Format timestamp
        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        holder.timestampText.text = dateFormat.format(Date(player.timestamp))

        // Color code the mode badge
        if (player.mode == "Truth") {
            holder.modeText.setBackgroundColor(Color.parseColor("#8FA17F"))
        } else {
            holder.modeText.setBackgroundColor(Color.parseColor("#B89F6B"))
        }

        // Long click to delete
        holder.itemView.setOnLongClickListener {
            onItemLongClick(player)
            true
        }
    }

    override fun getItemCount(): Int = playerList.size

    fun updateData(newList: List<PlayerData>) {
        playerList = newList
        notifyDataSetChanged()
    }
}