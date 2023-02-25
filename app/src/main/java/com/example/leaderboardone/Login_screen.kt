package com.example.leaderboardone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.leaderboardone.ui.Recovery.Reset_Password
import com.example.leaderboardone.databinding.ActivityLoginScreenBinding
import com.example.leaderboardone.ui.Recovery.Forgot_Pass
import com.google.firebase.auth.FirebaseAuth
import com.google.protobuf.Empty
import org.w3c.dom.Text
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.times
import kotlin.time.toDuration

class Login_screen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Firebase Instance
        auth = FirebaseAuth.getInstance()
        binding.loadingAnimation.pauseAnimation()
        binding.loginBtn.visibility = View.VISIBLE

        binding.email.setOnClickListener {
            binding.tvWrongEmail.visibility = View.GONE
            binding.tvEmptyField.visibility = View.GONE
        }
        binding.password.setOnClickListener {
            binding.tvWrongEmail.visibility = View.GONE
            binding.tvEmptyField.visibility = View.GONE
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            binding.tvWrongEmail.visibility = View.GONE
            binding.tvEmptyField.visibility = View.GONE
            binding.loadingAnimation.visibility = View.VISIBLE
            binding.loginBtn.visibility = View.GONE
            binding.loadingAnimation.playAnimation()
            // Email/Password Login Logic

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, Navigation::class.java)
                        intent.putExtra("Email",binding.email.text.toString())
                        startActivity(intent)
                        finishActivity(0)
                    } else {
                        binding.loadingAnimation.visibility = View.GONE
                        binding.loadingAnimation.pauseAnimation()
                        binding.tvWrongEmail.visibility = View.VISIBLE
                        binding.loginBtn.visibility = View.VISIBLE
                        binding.email.text.clear()
                        binding.password.text.clear()
                    }
                }
            } else {
                binding.loadingAnimation.visibility = View.GONE
                binding.loadingAnimation.pauseAnimation()
                binding.tvEmptyField.visibility = View.VISIBLE
                binding.loginBtn.visibility = View.VISIBLE
            }
        }

        binding.btnForgotPass.setOnClickListener {
            startActivity(Intent(this, Forgot_Pass::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            val intent = Intent(this, Navigation::class.java)
            intent.putExtra("Email",binding.email.text.toString())
            startActivity(intent)
            finish()
        }
    }
}