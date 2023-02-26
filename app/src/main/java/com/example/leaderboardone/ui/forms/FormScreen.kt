package com.example.leaderboardone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.leaderboardone.ui.forms.Url
import com.example.leaderboardone.ui.home.HomeFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
class FormView_screen : AppCompatActivity() {

    private var db = Firebase.firestore

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_screen)


        // Logic for WebView And Form
        val docRef1 = db.collection("FORM_LINK")
        docRef1.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document){
                        val allDetails = all.toObject<Url>()
                        val UrlString1 = allDetails.EVENT_FORM1.toString()
                        val myWebView: WebView = findViewById(R.id.webView)
                        myWebView.settings.loadsImagesAutomatically = true
                        myWebView.settings.javaScriptEnabled = true
                        myWebView.scrollBarSize = View.SCROLLBARS_INSIDE_OVERLAY
                        myWebView.webViewClient = object : WebViewClient() {
                            @Deprecated("Deprecated in Java")
                            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                if (url != null) {
                                    view?.loadUrl(url)
                                }
                                return true
                            }
                        }
                        myWebView.loadUrl(UrlString1)
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

//        // Logic for WebView And Form
//        val docRef2 = db.collection("FORM_LINK2")
//        docRef2.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    for (all in document){
//                        val allDetails = all.toObject<Url>()
//                        val UrlString2 = allDetails.EVENT_FORM2.toString()
//                        val myWebView: WebView = findViewById(R.id.webView)
//                        myWebView.settings.loadsImagesAutomatically = true
//                        myWebView.settings.javaScriptEnabled = true
//                        myWebView.scrollBarSize = View.SCROLLBARS_INSIDE_OVERLAY
//                        myWebView.webViewClient = object : WebViewClient() {
//                            @Deprecated("Deprecated in Java")
//                            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                                if (url != null) {
//                                    view?.loadUrl(url)
//                                }
//                                return true
//                            }
//                        }
//                        myWebView.loadUrl(UrlString2)
//                    }
//                } else {
//                    Log.d("TAG", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("TAG", "get failed with ", exception)
//            }
//
    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (event.action == KeyEvent.ACTION_DOWN) {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                if (myWebView.canGoBack()) {
//                    myWebView.goBack()
//                } else {
//                    showToastToExit()
//                }
//                return true
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//    private fun showToastToExit() {
//        when {
//            doubleBackToExitPressedOnce -> {
//                onBackPressed()
//            }
//            else -> {
//                doubleBackToExitPressedOnce = true
//                showToast("Redirecting to the previous Page")
//                Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//            }
//        }
//    }
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
}