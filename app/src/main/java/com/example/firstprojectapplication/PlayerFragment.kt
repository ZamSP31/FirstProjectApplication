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
        // Personal & Fun
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

        // Relationships
        "Have you ever had a crush on a teacher?",
        "What's the longest you've gone without showering?",
        "Have you ever been in love?",
        "What's your biggest regret in a relationship?",
        "Have you ever ghosted someone? Why?",
        "What's the most childish thing you still do?",
        "Have you ever lied to your best friend?",
        "What's something you're glad your parents don't know about you?",

        // Spicy
        "What's the most trouble you've been in?",
        "Have you ever sent a risky text to the wrong person?",
        "What's the most embarrassing thing in your search history?",
        "Have you ever had a wardrobe malfunction in public?",
        "What's the weirdest place you've ever cried?",
        "Have you ever pretended to like a gift you actually hated?",
        "What's something you've done that you'd never want your family to know?",
        "Have you ever stalked someone on social media?",

        // Random & Funny
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

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_reveal_challenge, null)
        val dialogEmoji = dialogView.findViewById<TextView>(R.id.dialogEmoji)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogChallenge = dialogView.findViewById<TextView>(R.id.dialogChallenge)

        if (mode == "Truth") {
            dialogEmoji.text = "🤐"
            dialogTitle.text = "Your Truth"
        } else {
            dialogEmoji.text = "💪"
            dialogTitle.text = "Your Dare"
        }

        dialogChallenge.text = challenge

        AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)
            .setPositiveButton("Accept Challenge") { _, _ ->
                challengeText.text = challenge
                challengeText.visibility = View.VISIBLE
            }
            .setCancelable(false)
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