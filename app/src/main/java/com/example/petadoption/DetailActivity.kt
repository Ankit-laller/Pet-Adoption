package com.example.petadoption

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var imageView:ImageView = findViewById(R.id.imageView)

        var image: Int? = null
        // checking if the intent has extra
        if(intent.hasExtra("detail_screen")){
            image = intent.getIntExtra("detail_screen",0);
        }

            imageView.setImageResource(R.drawable.lady_with_pet)

    }
}
