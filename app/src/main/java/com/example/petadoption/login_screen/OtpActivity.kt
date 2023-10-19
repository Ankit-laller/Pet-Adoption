package com.example.petadoption.login_screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.petadoption.MainActivity
import com.example.petadoption.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var verifyButton:Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE)

        verifyButton = findViewById(R.id.otpBtn)


        verifyButton.setOnClickListener {
            storedVerificationId = intent.getStringExtra("storedVerificationId").toString()
            var code = findViewById<EditText>(R.id.editOtp).text.toString()
            Log.d("otp message", code)
            Log.d("submitted otp message", storedVerificationId)
            verifyCode(code)
        }
    }
    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = task.result?.user
                    with(sharedPreferences.edit()) {
                        putBoolean("isLogin", true)
                        apply()
                    }
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this@OtpActivity, "Invalid code entered...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}