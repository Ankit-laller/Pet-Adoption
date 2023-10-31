//package com.example.petadoption.login_screen
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.opengl.Visibility
//import android.os.Bundle
//import android.telephony.PhoneNumberUtils
//import android.text.TextUtils
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ProgressBar
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.petadoption.MainActivity
//import com.example.petadoption.R
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import java.util.concurrent.TimeUnit
//
//
//class LoginActivity : AppCompatActivity() {
//    // variable for FirebaseAuth class
//    lateinit var  mAuth: FirebaseAuth
//    lateinit var sharedPreferences: SharedPreferences
//    // variable for our text input
//    // field for phone and OTP.
//
//
//    lateinit var  generateOTPBtn: Button
//
//    // string for storing our verification ID
//    private lateinit var storedVerificationId: String
//        private lateinit var progressBar:ProgressBar
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        mAuth = FirebaseAuth.getInstance();
//        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE)
//        progressBar = findViewById(R.id.progressBar)
//        progressBar.visibility = View.GONE
//        val isLogin = sharedPreferences.getBoolean("isLogin", false)
//        if(isLogin){
//            progressBar.visibility = View.VISIBLE
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            progressBar.visibility = View.GONE
//            finish()
//        }
//
//        generateOTPBtn = findViewById(R.id.btn)
//        generateOTPBtn.setOnClickListener {
//            progressBar.visibility = View.VISIBLE
//            val edtPhone = findViewById<EditText?>(R.id.editText).text.toString()
//
//            Log.d("hello", edtPhone)
//            if (TextUtils.isEmpty(edtPhone)) {
//
//                Toast.makeText(
//                    this,
//                    "Please enter a valid phone number.",
//                    Toast.LENGTH_SHORT
//                ).show()
//                progressBar.visibility = View.GONE
//            } else {
//                // if the text field is not empty we are calling our
//                // send OTP method for getting OTP from Firebase.
//            val phone = PhoneNumberUtils.formatNumberToE164(edtPhone, "IN")
//
//                if(phone !=null){
//                    sendVerificationCode(phone)
//                    progressBar.visibility = View.GONE
//                }else{
//                    Toast.makeText(this, "empty number", Toast.LENGTH_SHORT).show()
//                    progressBar.visibility = View.GONE
//                }
//            }
//        }
////        generateOTPBtn.setOnClickListener{
////
////            Log.d("hello","hello")
////            Log.d("contact number is",phone)
////            sendVerificationCode(phone)
////        }
//    }
//    private fun sendVerificationCode(phoneNumber: String) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            phoneNumber,
//            60,
//            TimeUnit.SECONDS,
//            this,
//            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    // This method will be executed if the verification is completed automatically
//                    signInWithPhoneAuthCredential(credential)
//                }
//
//                override fun onVerificationFailed(e: FirebaseException) {
//                    Log.e("TAG", "Verification failed: ${e.message}")
//                    Toast.makeText(this@LoginActivity, "Verification failed", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onCodeSent(
//                    verificationId: String,
//                    token: PhoneAuthProvider.ForceResendingToken
//                ) {
//                    storedVerificationId = verificationId
//                    val intent = Intent(this@LoginActivity, OtpActivity::class.java)
//                    intent.putExtra("storedVerificationId", storedVerificationId)
//                    val intent2 = Intent(this@LoginActivity, UserDetailsActivity::class.java)
//                    intent2.putExtra("userNumber", phoneNumber)
//                    startActivity(intent)
//                }
//            }
//        )
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        mAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success")
//                    val user = task.result?.user
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    Log.w("TAG", "signInWithCredential:failure", task.exception)
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        Toast.makeText(this@LoginActivity, "Invalid code entered...", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//    }
//
//
////
//
//
//
//
//}
