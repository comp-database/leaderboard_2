package com.example.leaderboardone.ui.notifications

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.Navigation
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlin.math.PI

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private lateinit var auth: FirebaseAuth
    private var storageRef = Firebase.storage
    private lateinit var uri : Uri
    private var storageReference: StorageReference? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


//        storageReference!!.child(auth.uid!!).child("/images/Profile pic")
//            .downloadUrl.addOnSuccessListener {
//                uri ->
//                binding.ivProfilePic
//            }

        Picasso.get().load("https://media.geeksforgeeks.org/wp-content/cdn-uploads/logo-new-2.svg").into(binding.ivProfilePic)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.ivImage.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            }
        )

        binding.LogOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this.context, Login_screen::class.java))

        }
//
        binding.btnBrowse.setOnClickListener {
            galleryImage.launch("image/*")
        }

        binding.btnUpload.setOnClickListener {
            storageRef.getReference("images").child("Profile pic").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener {
                    task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val userId = FirebaseAuth.getInstance().currentUser!!.uid
                            val mapImage = mapOf(
                                "url" to it.toString()
                            )
                            val databaseReference = FirebaseDatabase.getInstance().getReference("userImages")
                            databaseReference.child(userId).setValue(mapImage)
                                .addOnSuccessListener { error ->
                                    Toast.makeText(this.context,"Successful",Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this.context,"Failed",Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}