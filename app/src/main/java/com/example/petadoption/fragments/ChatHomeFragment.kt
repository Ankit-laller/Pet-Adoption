//package com.example.petadoption.fragments
//
//import android.content.ContentValues
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.petadoption.PetImageAdapter
//import com.example.petadoption.PetModel
//import com.example.petadoption.R
//import com.example.petadoption.UserModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//
//class ChatHomeFragment : Fragment() {
//    private lateinit var userDataList:ArrayList<UserModel>
//    private lateinit var ChatHomeAdapter: ChatHomeAdapter
//    private lateinit var dbref : DatabaseReference
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//       userDataList = ArrayList()
//        return inflater.inflate(R.layout.fragment_chat_home, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
//        recyclerView.hasFixedSize()
//        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        getUserList()
//        ChatHomeAdapter = ChatHomeAdapter()
//        recyclerView.adapter = ChatHomeAdapter
//    }
//    private fun getUserList() {
//        dbref = FirebaseDatabase.getInstance().getReference("User Data")
//        dbref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userDataList.clear()
//                if(snapshot.exists()){
//                    for (snaps in snapshot.children){
//                        Log.d("BCCCCC","$snaps")
//                        var userData = snaps.getValue(UserModel::class.java)
//                        if(firebaseAuth.currentUser?.uid !=userData?.userId ){
//                            userDataList.add(userData!!)
//                        }
//                        ChatHomeAdapter.updateData(userDataList)
//
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
//            }
//        })
//    }
//
//
//}