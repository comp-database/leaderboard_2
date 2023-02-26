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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    lateinit var datalist: ArrayList<StudentDetails>
    private var db = Firebase.firestore
    private var storage = Firebase.storage

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
        datalist = arrayListOf()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        binding.emailText.text =auth.currentUser?.email
        binding.homePictureLoadingAnimation.visibility = View.VISIBLE
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
                            binding.tvDiv.text = allDetails.div.toString()
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

        // image load form selected image via Firebase
        val storageRefImg = FirebaseStorage.getInstance().reference.child(auth.currentUser?.email.toString())
        val localFileImg = File.createTempFile(auth.currentUser?.email.toString(), "jpg")
        storageRefImg.getFile(localFileImg).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg.absolutePath)
            binding.homePictureLoadingAnimation.visibility = View.GONE
            binding.profilePicHomeFirebase.setImageBitmap(bitmap)
        }

        eventChangeListener()

        return root
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        db.collection("COMPS")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value : QuerySnapshot?,
                    error : FirebaseFirestoreException?
                ){
                    if (error != null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            datalist.add(dc.document.toObject(StudentDetails::class.java))
                        }
                    }

                }
            })
    }

    private fun getGreetingMessage(): Any {
        val c = Calendar.getInstance()

        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> binding.greetingMessage.text = "Good morning"
            in 12..15 -> binding.greetingMessage.text = "Good afternoon"
            in 16..20 -> binding.greetingMessage.text = "Good evening"
            in 21..23 -> binding.greetingMessage.text = "Good Night"
            else -> "Hello"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding != null
    }
}