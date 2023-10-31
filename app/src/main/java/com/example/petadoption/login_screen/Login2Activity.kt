package com.example.petadoption.login_screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoption.MainActivity
import com.example.petadoption.databinding.ActivityLogin2Binding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class Login2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE)

        binding.button2.setOnClickListener {
            val intent = Intent(this,SignUPActivity::class.java )
            startActivity(intent)
            finish()
        }
        check()
        firebaseAuth = FirebaseAuth.getInstance()
        binding.Loginbutton.setOnClickListener{
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{

                    if(it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        with(sharedPreferences.edit()) {
                putBoolean("isLogin", true)
                apply()
            }
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Either email or password is wrong", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else{
                Toast.makeText(this, "Can not live blank email or password", Toast.LENGTH_SHORT).show()
            }
        }



    }
    private fun check(){
        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE)
        val isLogin = sharedPreferences.getBoolean("isLogin", false)
        if(isLogin){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}