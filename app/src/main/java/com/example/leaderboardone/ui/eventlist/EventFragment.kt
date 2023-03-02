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
import com.example.leaderboardone.ui.forms.FormScreen2
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
        val storageRefImg1 = FirebaseStorage.getInstance().reference.child("images/Event1.png")
        val localFileImg1 =  File.createTempFile("Event1","png")
        storageRefImg1.getFile(localFileImg1).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg1.absolutePath)
            binding.EventImage1.setImageBitmap(bitmap)
            binding.eventPictureLoadingAnimation1.visibility = View.GONE
            binding.eventPictureLoadingAnimation1.pauseAnimation()
        }

        binding.EventImage1.setOnClickListener {
            val intent = Intent(this.context, FormView_screen::class.java)
            startActivity(intent)
        }

        val storageRefImg2 = FirebaseStorage.getInstance().reference.child("images/Event2.png")
        val localFileImg2=  File.createTempFile("Event2","png")
        storageRefImg2.getFile(localFileImg2).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg2.absolutePath)
            binding.EventImage2.setImageBitmap(bitmap)
            binding.eventPictureLoadingAnimation2.visibility = View.GONE
            binding.eventPictureLoadingAnimation2.pauseAnimation()
        }

        binding.EventImage2.setOnClickListener {
            val intent = Intent(this.context, FormScreen2::class.java)
            startActivity(intent)
        }

        return root
    }
}