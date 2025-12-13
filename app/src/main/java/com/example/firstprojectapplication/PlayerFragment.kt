package com.example.firstprojectapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class PlayerFragment : Fragment() {

    private val truthList = listOf(
        "What's the most embarrassing thing you've done while drunk?",
        "Have you ever had a crush on someone in this room?",
        "What's a secret you've never told anyone?",
        "Who was your first crush?",
        "Who was your first kiss, and how was it?",
        "What's the biggest lie you've ever told?",
        "Have you ever cheated on a test?",
        "What's your biggest fear?"
    )

    private val dareList = listOf(
        "Do 10 jumping jacks!",
        "Run around the Main Building fountain three times yelling your favorite song.",
        "Act like a chicken for 10 seconds.",
        "Text someone 'I love pineapples 🍍'.",
        "Walk through España Blvd. waving at every person passing by.",
        "Do your best celebrity impression.",
        "Speak in an accent for the next 3 rounds.",
        "Let someone else post a status on your social media."
    )

    private var playerName: String = ""
    private var currentChallenge: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerName = arguments?.getString("playerName") ?: "Player"

        val nameText = view.findViewById<TextView>(R.id.fragmentPlayerName)
        val challengeText = view.findViewById<TextView>(R.id.fragmentChallenge)
        val btnTruth = view.findViewById<Button>(R.id.fragmentBtnTruth)
        val btnDare = view.findViewById<Button>(R.id.fragmentBtnDare)
        val btnReveal = view.findViewById<Button>(R.id.fragmentBtnReveal)

        nameText.text = playerName

        var selectedMode: String? = null

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

        btnReveal.setOnClickListener {
            if (selectedMode != null) {
                showRevealDialog(selectedMode!!, challengeText)

                // Reset selection for next round
                btnTruth.alpha = 1.0f
                btnDare.alpha = 1.0f
                selectedMode = null
            } else {
                Toast.makeText(
                    requireContext(),
                    "Choose Truth or Dare first!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showRevealDialog(mode: String, challengeText: TextView) {
        val challenge = if (mode == "Truth") {
            truthList.random()
        } else {
            dareList.random()
        }

        currentChallenge = challenge

        val title = if (mode == "Truth") "🤐 Your Truth" else "💪 Your Dare"

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(challenge)
            .setPositiveButton("Accept Challenge") { _, _ ->
                challengeText.text = challenge
                challengeText.visibility = View.VISIBLE



            }
            .setNegativeButton("Skip (Chicken!)") { dialog, _ ->
                Toast.makeText(
                    requireContext(),
                    "$playerName chickened out! 🐔",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        fun newInstance(playerName: String): PlayerFragment {
            val fragment = PlayerFragment()
            val args = Bundle()
            args.putString("playerName", playerName)
            fragment.arguments = args
            return fragment
        }
    }
}