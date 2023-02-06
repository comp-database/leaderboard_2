package com.example.leaderboardone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.leaderboardone.databinding.ActivityLoginScreenBinding
import com.example.leaderboardone.ui.dashboard.DashboardFragment
import com.google.firebase.auth.FirebaseAuth

class Login_screen : AppCompatActivity() {
    lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Firebase Instance
        auth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            // Email/Password Login Logic
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, Navigation::class.java)
                        intent.putExtra("Email",binding.email.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            val intent = Intent(this, Navigation::class.java)
            intent.putExtra("Email",binding.email.text.toString())
            startActivity(intent)
        }
    }
}