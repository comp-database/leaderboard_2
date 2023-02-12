package com.example.leaderboardone.ui.eventlist

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.leaderboardone.FormView_screen
import com.example.leaderboardone.databinding.FragmentEventBinding
import com.example.leaderboardone.ui.home.HomeViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        // Inflate the layout for this fragment

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Logic for Img and Link
        val storageRefImg = FirebaseStorage.getInstance().reference.child("images/EventImg.png")
        val localFileImg =  File.createTempFile("EventImg","jpg")
        storageRefImg.getFile(localFileImg).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg.absolutePath)
            binding.EventImage.setImageBitmap(bitmap)
        }
        binding.EventImage.setOnClickListener {
            val intent = Intent(this.context, FormView_screen::class.java)
            startActivity(intent)
        }
        return root
    }
}