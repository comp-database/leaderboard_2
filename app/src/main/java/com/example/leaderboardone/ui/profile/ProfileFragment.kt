package com.example.leaderboardone.ui.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
import java.io.File
import java.util.UUID

class NotificationsFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var image: ImageView
    private lateinit var storageReference: StorageReference
    lateinit var imagePath: String

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
        binding.emailProfile.text = auth.currentUser?.email
        //Logic for data-display of Particular logged user
        val docRef = db.collection("COMPS")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document) {
                        val allDetails = all.toObject<StudentDetails>()
                        // review in name
                        if (allDetails.collegeEmail.toString() == auth.currentUser?.email.toString()) {
                            binding.nameProfile.text = allDetails.fullName.toString()
                            binding.rollProfile.text = allDetails.idNumber.toString()
                            binding.phoneProfile.text = allDetails.contactNo.toString()
                        }
                        Log.d("TAG", "name data  ${allDetails.collegeEmail} ")
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

        auth = FirebaseAuth.getInstance()
        // updated photo display
        binding.btnUpload.setOnClickListener {
            image = binding.profilePicFirebase
            uploadImage(image)
        }

        // image load form selected image via Firebase
        val storageRefImg = FirebaseStorage.getInstance().reference.child(auth.currentUser?.email.toString())
        val localFileImg = File.createTempFile(auth.currentUser?.email.toString(), "jpg")
        storageRefImg.getFile(localFileImg).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg.absolutePath)
            binding.profilePicFirebase.setImageBitmap(bitmap)
        }
        return root
    }

    private fun uploadImage(imgProfile: ImageView) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            image.setImageURI(data?.data)
            uploadImagetoFB(data?.data!!)
        }
    }

    // upload image
    private fun uploadImagetoFB(imageUri: Uri) {
        storageReference = FirebaseStorage.getInstance().reference
        storageReference.child(auth.currentUser?.email.toString()).putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                val task = taskSnapshot.storage.downloadUrl
                task.addOnCompleteListener {
                    if (it.isSuccessful) {
                        imagePath = it.result.toString()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
