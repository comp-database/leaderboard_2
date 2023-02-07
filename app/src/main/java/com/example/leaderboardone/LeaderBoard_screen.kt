package com.example.leaderboardone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leaderboardone.Adapter.RvAdapter
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.databinding.ActivityLeaderBoardScreenBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LeaderBoard_screen : AppCompatActivity() {
    lateinit var binding: ActivityLeaderBoardScreenBinding
    var db = Firebase.firestore
    lateinit var datalist: ArrayList<StudentDetails>
    lateinit var adapterRc :RvAdapter
    lateinit var topTenlist : ArrayList<StudentDetails>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        datalist = arrayListOf()
        topTenlist = arrayListOf()
        adapterRc = RvAdapter(topTenlist)
        recyclerView.adapter = adapterRc

        Log.d("data ","${intent.getStringExtra("Email")}")
        EventChangeListenerAll()
        EventChangeListenerTopTen()
    }
    private fun EventChangeListenerAll() {
        db = FirebaseFirestore.getInstance()
        db.collection("COMPS").orderBy("points",Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value : QuerySnapshot?,
                    error : FirebaseFirestoreException?
                ){
                    if (error != null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            datalist.add(dc.document.toObject(StudentDetails::class.java))
                        }
                    }
                    adapterRc.notifyDataSetChanged()
                }
            })
    }
    // For Top Ten Students
    private fun EventChangeListenerTopTen() {
        db = FirebaseFirestore.getInstance()
        db.collection("COMPS").orderBy("points",Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value : QuerySnapshot?,
                    error : FirebaseFirestoreException?
                ){
                    if (error != null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            topTenlist.add(dc.document.toObject(StudentDetails::class.java))
                        }
                    }
                    adapterRc.notifyDataSetChanged()
                }
            })
    }

}