package com.example.leaderboardone.ui.Recovery

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlin.time.toDuration

class Forgot_Pass : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadingAnimation.pauseAnimation()
        binding.evForgotEmail.setOnClickListener {
            binding.tvEmptyFieldForgotPass.visibility = View.GONE
            binding.loadingAnimation.pauseAnimation()
        }

        auth = Firebase.auth
        binding.btnForgotPass.setOnClickListener {
            val email = binding.evForgotEmail.text.toString()

            if (checkUserInput()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.loadingAnimation.playAnimation()
                        binding.loadingAnimation.addAnimatorListener(object : Animator.AnimatorListener{
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
                                startActivity(Intent(this@Forgot_Pass, Login_screen::class.java))
                                finish()
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        })
                        binding.loadingAnimation.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
        private fun checkUserInput(): Boolean {
            val email = binding.evForgotEmail.text.toString()
            if (binding.evForgotEmail.text.toString() == "") {
                binding.tvEmptyFieldForgotPass.visibility = View.VISIBLE
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Wrong Email Format", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }
}