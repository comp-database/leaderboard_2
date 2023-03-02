package com.example.leaderboardone.ui.leaderboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leaderboardone.Adapter.RvAdapter
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.databinding.FragmentLeaderboardBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LeaderBoardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null

    private var db = Firebase.firestore
    lateinit var datalist: ArrayList<StudentDetails>
    lateinit var adapterRc : RvAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val leaderBoardViewModel =
            ViewModelProvider(this)[LeaderBoardViewModel::class.java]

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

//        bindings = ActivityLeaderBoardScreenBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        val recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        datalist = arrayListOf()
        adapterRc = RvAdapter(datalist)
        recyclerView.adapter = adapterRc

//        Log.d("data ","${intent.getStringExtra("Email")}")
        EventChangeListener()

        return root

    }
    private fun EventChangeListener() {
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
                            datalist.add(dc.document.toObject(StudentDetails::class.java))
                        }
                    }
                    adapterRc.notifyDataSetChanged()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}