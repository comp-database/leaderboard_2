package com.example.leaderboardone.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.leaderboardone.ui.Recovery.Reset_Password
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.Navigation
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.ActivityNavigationBinding
import com.example.leaderboardone.databinding.FragmentProfileBinding
import com.example.leaderboardone.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class NotificationsFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

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
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()

        auth = FirebaseAuth.getInstance()
        binding.emailProfile.text =auth.currentUser?.email
        //Logic for data-display of Particular logged user
        val docRef = db.collection("COMPS")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document){
                        val allDetails = all.toObject<StudentDetails>()
                        // review in name
                        if (allDetails.collegeEmail.toString() == auth.currentUser?.email.toString()){
                            binding.nameProfile.text = allDetails.fullName.toString()
                            binding.rollProfile.text = allDetails.idNumber.toString()
                            binding.phoneProfile.text = allDetails.contactNo.toString()
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

        binding.resetPass.setOnClickListener {
            startActivity(Intent(this.context, Reset_Password::class.java))
        }

        binding.LogOutBtn.setOnClickListener {
            binding.loadingAnimationProfile.visibility = View.VISIBLE
            binding.loadingAnimationProfile.playAnimation()
            auth.signOut()
            startActivity(Intent(this.context, Login_screen::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
