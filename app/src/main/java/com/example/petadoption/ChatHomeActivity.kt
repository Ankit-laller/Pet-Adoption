package com.example.petadoption

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoption.fragments.ChatHomeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatHomeActivity : AppCompatActivity() {
    private lateinit var userDataList:ArrayList<UserModel>
    private lateinit var ChatHomeAdapter: ChatHomeAdapter
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_home)
        userDataList = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        val recyclerView: RecyclerView = findViewById(R.id.Chat_recyclerview)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getUserList()
        ChatHomeAdapter = ChatHomeAdapter(this)
        recyclerView.adapter = ChatHomeAdapter
    }
    private fun getUserList() {
        val db = Firebase.firestore
        db.collection("User Data")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userDataList.clear()
                    for(data in document){
                        val dataModel = data.toObject(UserModel::class.java)
                        if(firebaseAuth.currentUser?.uid !=dataModel?.userId ){
                            userDataList.add(dataModel!!)
                        }
                    }
                    ChatHomeAdapter.updateData(userDataList)

                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }


}