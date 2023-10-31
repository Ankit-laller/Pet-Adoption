package com.example.petadoption.login_screen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petadoption.MainActivity
import com.example.petadoption.R
import com.example.petadoption.UserModel
import com.example.petadoption.databinding.ActivitySignUpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Timer
import java.util.TimerTask

class SignUPActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference
    private lateinit var binding: ActivitySignUpactivityBinding
    var currentUser = ""
    private lateinit var imageUri: Uri
    private lateinit var imageUrl: String
    private  var isUploaded:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.userImagePickerBtn.setOnClickListener {
            pickImageFromGallery()
        }
        binding.signUpButton.setOnClickListener{
            val email = binding.etemail.text.toString()
            val pass = binding.etpass.text.toString()
            val pass2 = binding.etpass2.text.toString()
            val userName = binding.etName.text.toString()
            val city = binding.etcity.text.toString()

            imageUrl=""
            signUpFunction(email, pass, pass2){userId->
                if (userId.isNotEmpty() ) {
                    uploadImageToFirebase(userName,imageUri, userId){ downloadUrl ->
                        imageUrl = downloadUrl
                        val userData = UserModel(userId, userName, city, imageUrl, email)
                        uploadUserData(userData)
                        val intent = Intent(this, Login2Activity::class.java)
                        startActivity(intent)
                        finish()
                    }

//                    val userData = UserModel(userId, userName, city, imageUrl, email)
//                    uploadUserData(userData)
//                Log.d("user data", userId.toString())
                }else{
                    Toast.makeText(this," current user is empty", Toast.LENGTH_SHORT).show()
                }

            }


        }
    }
    private fun signUpFunction(email:String, pass:String, pass2:String,callback: (String) -> Unit) {
        var curuser=""
        if(email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()){
            if(pass==pass2){
                if(pass.length>5){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                currentUser =user.uid.trim().toString()
                                callback(currentUser)
                                println("current user is$currentUser")
                            }
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            println("error"+it.exception.toString())
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Don't leave anything empty", Toast.LENGTH_SHORT).show()
        }

    }
    private fun uploadUserData(userData: UserModel) {

        val db = Firebase.firestore

        db.collection("User Data").document(currentUser).set(userData)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{ err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private  fun uploadImageToFirebase(userName:String,imageUri: Uri?, userId:String,callback: (String) -> Unit) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/" +userName+ userId)
        val uploadTask = storageRef.putFile(imageUri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Store the downloadUri in the Firestore database
                imageUrl = downloadUri.toString()
                Toast.makeText(this,imageUrl, Toast.LENGTH_SHORT).show()
                callback(imageUrl)
            }
        }

    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data!!
            binding.userImage.setImageURI(imageUri)
        }


    }

}