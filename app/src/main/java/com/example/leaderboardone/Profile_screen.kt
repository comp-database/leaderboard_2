package com.example.leaderboardone
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class Profile_screen : AppCompatActivity() {
    private lateinit var image : ImageView
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    lateinit var imagePath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        val ibGallery: Button = findViewById(R.id.ib_gallery)
        auth = FirebaseAuth.getInstance()
        // updated photo display
        ibGallery.setOnClickListener {
            image = findViewById(R.id.iv_background)
            uploadImage(image)
        }

        // image load form selected image via Firebase
        val userImg : ImageView = findViewById(R.id.imageView2)
        val storageRefImg = FirebaseStorage.getInstance().reference.child(auth.currentUser?.email.toString())
        val localFileImg =  File.createTempFile(auth.currentUser?.email.toString(),"jpg")
        storageRefImg.getFile(localFileImg).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileImg.absolutePath)
            userImg.setImageBitmap(bitmap)
        }
    }

    private fun uploadImage(imgProfile: ImageView) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
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
}