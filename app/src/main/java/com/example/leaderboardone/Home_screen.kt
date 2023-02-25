package com.example.leaderboardone

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.databinding.ActivityHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class Home_screen : AppCompatActivity() {

    lateinit var binding: ActivityHomeScreenBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
//    lateinit var datalist: ArrayList<StudentDetails>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.UpdateProfileImg.setOnClickListener {
            startActivity(Intent(this,Profile_screen::class.java))
        }
        auth = FirebaseAuth.getInstance()
        binding.emailText.text =auth.currentUser?.email
        binding.LearderBoardBtn.setOnClickListener {
            startActivity(Intent(this,LeaderBoard_screen::class.java))
        }
        binding.LogOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,Login_screen::class.java))
            finish()
        }
        // Logic for Img and Link
        val storageRefImg = FirebaseStorage.getInstance().reference.child("images/EventImg.png")
        val localFileImg =  File.createTempFile("EventImg","jpg")
        storageRefImg.getFile(localFileImg).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg.absolutePath)
            binding.EventImage.setImageBitmap(bitmap)
        }
        binding.Form.setOnClickListener {
            val intent = Intent(this,FormView_screen::class.java)
            startActivity(intent)
        }


        //Logic for data-display of Particular logged user
        val docRef = db.collection("COMPS")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document){
                        val allDetails = all.toObject<StudentDetails>()
                        // review in name
                        if (allDetails.collegeEmail.toString() == auth.currentUser?.email.toString()){
                            binding.pointText.text = allDetails.points.toString()
                            binding.nameText.text = allDetails.fullName.toString()
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
    }
}