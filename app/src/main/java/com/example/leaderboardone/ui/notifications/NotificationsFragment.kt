package com.example.leaderboardone.ui.notifications

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leaderboardone.Recovery.Reset_Password
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

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

        binding.resetPass.setOnClickListener {
            startActivity(Intent(this.context, Reset_Password::class.java))
        }

//        binding.btnUpload.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent,0,)
//        }

//        storageReference!!.child(auth.uid!!).child("/images/Profile pic")
//            .downloadUrl.addOnSuccessListener {
//                uri ->
//                binding.ivProfilePic
//            }
        //Picasso.get().load("https://media.geeksforgeeks.org/wp-content/cdn-uploads/logo-new-2.svg").into(binding.ivProfilePic)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.profilePic.setImageURI(it)
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
//            uploadImageToFirebaseStorage()
//            binding.profilePic.setBackgroundResour
        }

//        binding.btnUpload.setOnClickListener {
//            storageRef.getReference("images").child("Profile pic").child(System.currentTimeMillis().toString())
//                .putFile(uri)
//                .addOnSuccessListener {
//                    task ->
//                    task.metadata!!.reference!!.downloadUrl
//                        .addOnSuccessListener {
//                            val userId = FirebaseAuth.getInstance().currentUser!!.uid
//                            val mapImage = mapOf(
//                                "url" to it.toString()
//                            )
//                            val databaseReference = FirebaseDatabase.getInstance().getReference("userImages")
//                            databaseReference.child(userId).setValue(mapImage)
//                                .addOnSuccessListener { error ->
//                                    Toast.makeText(this.context,"Successful",Toast.LENGTH_SHORT).show()
//                                }.addOnFailureListener {
//                                    Toast.makeText(this.context,"Failed",Toast.LENGTH_SHORT).show()
//                                }
//                        }
//                }
//        }

        return root
    }

    private fun uploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uri)
        ref.downloadUrl.addOnSuccessListener {
            saveUserToFirebaseDatabase(it.toString())
            Toast.makeText(this.context,"Success",Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            Toast.makeText(this.context,"Failed to download url",Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,profileImageUrl)

        ref.setValue(user).addOnSuccessListener {
            Toast.makeText(this.context,"Success",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this.context,"Failed to save user to database",Toast.LENGTH_LONG).show()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == 0 && requestCode == Activity.RESULT_OK && data !=null ){
//            Log.d("NotificationActivity","Photo was selected")
//
//            val uri = data.data
//
//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
//        }
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class User(val uid: String, val profileUrl: String)