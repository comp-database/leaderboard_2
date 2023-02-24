package com.example.leaderboardone.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leaderboardone.FormView_screen
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        binding.emailText.text =auth.currentUser?.email
        //Logic for data-display of Particular logged user
        val docRef = db.collection("COMPS")
        getGreetingMessage()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document){
                        val allDetails = all.toObject<StudentDetails>()
                        // review in name
                        if (allDetails.collegeEmail.toString() == auth.currentUser?.email.toString()){
                            binding.pointText.text = allDetails.points.toString()
                            binding.nameText.text = allDetails.fullName.toString()
                            binding.rank.text = allDetails.rank.toString()
                        }
                        Log.d("TAG","name data  ${allDetails.collegeEmail} ")
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        return root
    }

    private fun getGreetingMessage(): Any {
        val c = Calendar.getInstance()

        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> binding.greetingMessage.text = "Good morning"
            in 12..15 -> binding.greetingMessage.text = "Good afternoon"
            in 16..23 -> binding.greetingMessage.text = "Good evening"
//            in 21..23 -> binding.greetingMessage.text = "Good Night"
            else -> "Hello"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}