package com.example.leaderboardone.ui.forms

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.leaderboardone.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FormScreen2 : AppCompatActivity()  {
    private var db = Firebase.firestore

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_screen2)

        // Logic for WebView And Form
        val docRef2 = db.collection("FORM_LINK2")
        docRef2.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (all in document){
                        val allDetails = all.toObject<Url>()
                        val UrlString2 = allDetails.EVENT_FORM2.toString()
                        val myWebView: WebView = findViewById(R.id.webView2)
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
                        myWebView.loadUrl(UrlString2)
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