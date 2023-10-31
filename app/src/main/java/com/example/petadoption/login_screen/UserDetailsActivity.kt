package com.example.petadoption.login_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoption.MainActivity
import com.example.petadoption.UserModel
import com.example.petadoption.databinding.ActivityUserDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var imageUri: Uri
    private lateinit var imageUrl: String
    lateinit var sharedPreferences: SharedPreferences
    var currentUser = FirebaseAuth.getInstance().currentUser?.uid

    var favourite = listOf<String>("Dog", "Cat", "Bird")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE)
        val userName = binding.userNameEt.text.toString()
        val userCity = binding.userCityEt.text.toString()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val userId = intent.getStringExtra("userId")
        val email = intent.getStringExtra("email")
        imageUrl =""
        binding.nextBtn.setOnClickListener {
            if (currentUser != null ) {
                uploadImageToFirebase(imageUri, currentUser!!)
                val image = hashMapOf(
                    "url" to imageUrl
                ).toString()
                val userData = UserModel(currentUser, userName, userCity, imageUrl, email)
                uploadUserData(userData)
//                Log.d("user data", userId.toString())
            }else{
                Toast.makeText(this,"Select image", Toast.LENGTH_SHORT).show()
            }

//
//            val intent =Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }
        binding.userImagePickerBtn.setOnClickListener {
            pickImageFromGallery()
        }
//        binding.nextBtn.setOnClickListener {
//            val selectedBtnId: Int = binding.radioGroup.checkedRadioButtonId
//            if (selectedBtnId != -1) {
//                val selectedBtn = findViewById<RadioButton>(selectedBtnId)
//                var selectedText = selectedBtn.text.toString()
//                if(selectedBtnId ==1){
//                    selectedText ="Dog"
//                }
//
//                Log.d("selected Button", selectedText)
//            } else {
//                // Handle the case when no RadioButton is selected.
//                Log.d("selected Button", "No button selected")
//            }
//        }


    }

    private fun uploadUserData(userData: UserModel) {

        val db = Firebase.firestore

        db.collection("User Data").document(currentUser!!).set(userData)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{ err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebase(imageUri: Uri?, userId:String) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/" + userId)
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