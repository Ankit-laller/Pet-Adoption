package com.example.petadoption

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import com.example.petadoption.databinding.ActivityMainBinding
import com.example.petadoption.databinding.ActivityPutForAdoptionBinding
import com.example.petadoption.login_screen.Login2Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import java.util.UUID

class PutForAdoption : AppCompatActivity() {
    private lateinit var binding: ActivityPutForAdoptionBinding
    private val petTypes = listOf("Cat", "Dog", "Bird")
    private var currentIndex = 0
    private  var currentPetType =petTypes[currentIndex]
    private var petGender="Male"
    private lateinit var imageUri: Uri
    private lateinit var imageUrl: String
    var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    var userData:UserModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPutForAdoptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fetchUserData()

        binding.addBtn.setOnClickListener {
            var postId = generateUniqueUUID()
            val radioGroup = binding.radioGroup
            val selectedRadioBtn :Int = radioGroup.checkedRadioButtonId
            val radioBtn: RadioButton =  findViewById(selectedRadioBtn)
            val selectedGender = radioBtn.text.toString()
            petGender =selectedGender
            val petName =binding.petNameEt.text.toString()
            val petAge = binding.petAgeEt.text.toString()
            val petDescription = binding.petDescriptionEt.text.toString()
            Log.d("petGender", petGender)
            imageUrl=""
            if (currentUser != null ) {
                uploadImageToFirebase(imageUri, currentUser!!, postId){ downloadUrl ->
                    imageUrl = downloadUrl
                    val petData = PetModel(currentUser, postId,petName,imageUrl,petAge,
                        petGender, petDescription,currentPetType, userData)
                    uploadUserData(petData)
                    finish()
                }


//                Log.d("user data", userId.toString())
            }else{
                Toast.makeText(this,"Select image", Toast.LENGTH_SHORT).show()
            }
        }
        val goBack: ImageButton = findViewById(R.id.back_btn)
        goBack.setOnClickListener{
            finish()
        }
        val next_btn = binding.nextBtn
        val prev_btn =binding.previousBtn
        binding.petType.text=currentPetType
        prev_btn.setOnClickListener {
            currentIndex = (currentIndex - 1 + petTypes.size) % petTypes.size
             currentPetType = petTypes[currentIndex]
            binding.petType.text = currentPetType
        }
        next_btn.setOnClickListener {
            currentIndex = (currentIndex + 1) % petTypes.size
             currentPetType = petTypes[currentIndex]
            binding.petType.text = currentPetType
        }

        binding.imagePickerBtn.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) ==
//                    PackageManager.PERMISSION_DENIED){
//                    //permission denied
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    //show popup to request runtime permission
//                    requestPermissions(permissions, PERMISSION_CODE);
//                }
//                else{
//                    //permission already granted
//                    pickImageFromGallery()
//                }
//            }
//            else{
//                //system OS is < Marshmallow
//                pickImageFromGallery()
//            }
            pickImageFromGallery()
        }
    }
    private  fun uploadImageToFirebase(imageUri: Uri?, userId:String, postId:String,callback: (String) -> Unit) {
        var i=0;
        val storageRef =
            FirebaseStorage.getInstance().reference.child("user_posts/" ).child(userId)
                .child("${userId}postNo${++i}")
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
    private fun fetchUserData(){
        println("user id is $currentUser")
        val db = Firebase.firestore
        db.collection("User Data")
            .whereEqualTo("userId", currentUser)
            .get()
            .addOnSuccessListener { document ->

                if (document != null) {
                    println("user data ${document.size()} ")
                    for(data in document){
                        val dataModel = data.toObject(UserModel::class.java)
                        userData =dataModel
                       println("user name is ${dataModel.userName}")
                    }
                } else {
                    println("Document does not exist")
                }

            }
            .addOnFailureListener { exception ->
                // Handle any errors that may occur
                println("Error getting documents: $exception")
            }
    }
    fun generateUniqueUUID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }
    private fun uploadUserData(petData: PetModel) {

        val db = Firebase.firestore

        db.collection("User Posts").document(petData.postId!!).set(petData)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{ err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUri = data?.data!!
            binding.imagePicker.setImageURI(imageUri)
        }
    }


}