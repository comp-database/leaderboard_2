package com.example.leaderboardone.ui.Recovery

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import com.example.leaderboardone.Login_screen
import com.example.leaderboardone.Navigation
import com.example.leaderboardone.R
import com.example.leaderboardone.databinding.ActivityResetPasswordBinding
import com.example.leaderboardone.ui.home.HomeFragment
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
        binding.loadingAnimation.pauseAnimation()
        binding.evResetEmail.setOnClickListener {
            binding.tvEmptyFieldForgotPass.visibility = View.GONE
            binding.loadingAnimation.pauseAnimation()
        }
        binding.btnResetPass.setOnClickListener {
            val email = binding.evResetEmail.text.toString()

            if(checkUserInput()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful){
                        binding.loadingAnimation.playAnimation()
                        binding.loadingAnimation.addAnimatorListener(object : Animator.AnimatorListener{
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
                                startActivity(Intent(this@Reset_Password, Login_screen::class.java))
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
        val email = binding.evResetEmail.text.toString()
        if (binding.evResetEmail.text.toString() == "") {
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
