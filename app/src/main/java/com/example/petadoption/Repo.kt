package com.example.petadoption

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo {
    fun adopt(postId:String){
        val db = Firebase.firestore
        db.collection("User Posts").
        document(postId).update("adopted", true)
            .addOnCompleteListener{
            }.addOnFailureListener{ err->
            }
    }

    fun likePet(currentUser:String, postId: String){
        val db = Firebase.firestore

        db.collection("User Data").document(currentUser)
            .update("likedPets",  FieldValue.arrayUnion(postId))
            .addOnCompleteListener{
            }.addOnFailureListener{ err->
            }
    }
    fun getLikedPet(callback: (ArrayList<String>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val petData = ArrayList<String>()

        val db = Firebase.firestore
        db.collection("User Data").whereEqualTo("userId", currentUser)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (data in document) {
                        val dataModel = data.toObject(UserModel::class.java)
                        for (x in dataModel.likedPets!!) {
                            petData.add(x)
                        }
                    }
                    Log.d("dikkha na dikhana", petData.toString())

                    val likedPetsList = ArrayList<String>()
                    val db1 = Firebase.firestore
                    var count = 0
                    for (postId in petData) {
                        db1.collection("User Posts")
                            .whereEqualTo("postId", postId)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d("chal jaa bsdk", document.documents.toString())
                                    for (data in document) {
                                        val dataModel = data.toObject(PetModel::class.java)
                                        likedPetsList.add(dataModel.imageUrl ?: "")
                                    }
                                } else {
                                    println("liked posts do not exist")
                                }
                                count++
                                if (count == petData.size) {
                                    callback(likedPetsList)
                                }
                            }
                            .addOnFailureListener { exception ->
                                println("Error getting documents: $exception")
                            }
                    }
                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    fun getUserData(callback: (String) -> Unit):String{
        var currentuser =""
        val db = Firebase.firestore
        db.collection("User Data")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for(data in document){
                        val dataModel = data.toObject(UserModel::class.java)
                        if(FirebaseAuth.getInstance().currentUser?.uid ==dataModel?.userId ){
                            currentuser = dataModel.userName.toString()
                            callback(currentuser)
                        }
                    }

                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
        return currentuser
    }
}