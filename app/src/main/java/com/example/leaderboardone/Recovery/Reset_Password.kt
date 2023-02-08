package com.example.leaderboardone.Recovery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Reset_Password : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnResetPass.setOnClickListener {
            val email = binding.evResetEmail.text.toString()

            if(checkUserInput()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Check your Email",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Login_screen::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun checkUserInput(): Boolean {
        val email = binding.evResetEmail.text.toString()
        if (binding.evResetEmail.text.toString() == "") {
            Toast.makeText(this, "Enter your Email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Wrong Email Format", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
