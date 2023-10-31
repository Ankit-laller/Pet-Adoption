package com.example.petadoption

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.adoptablepaws.fragments.HomeFragment
import com.example.petadoption.databinding.ActivityDetailBinding
import com.example.petadoption.databinding.ActivityPutForAdoptionBinding
import com.google.firebase.auth.FirebaseAuth

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    var currentUser = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val petName = intent.getStringExtra(HomeFragment.petName)
        val petAge = intent.getStringExtra(HomeFragment.petAge)
        val petDes = intent.getStringExtra(HomeFragment.petDescription)
        val petImage = intent.getStringExtra(HomeFragment.petImage)
        val ownerName =intent.getStringExtra(HomeFragment.ownerName)
        val postId = intent.getStringExtra(HomeFragment.postId)
        val isAdopted =intent.getBooleanExtra(HomeFragment.isAdopted, false)
        val userId = intent.getStringExtra(HomeFragment.userId)
        if (petName != null) {
            Glide.with(this).load(petImage)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.baseline_person_4_24) // Add a placeholder image
                        .error(R.drawable.baseline_chat_bubble_24) // Add an error image
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(binding.petImage2)
            binding.petName.text = petName
            binding.petDescription.text = petDes
            binding.ownerName.text = ownerName
            binding.petName2.text = (petName+"'s Owner")
            if (isAdopted) {
                binding.adoptionBtn.text = "Adopted"
            } else {
                binding.adoptionBtn.text = "Adopt"
            }

        }
        binding.adoptionBtn.setOnClickListener {
            if (postId != null) {
                Repo().adopt(postId)
            }
        }
        binding.likeBtn.setOnClickListener {
            if (postId!=null&& currentUser!=null){
                Repo().likePet(currentUser!!, postId)
            }
        }
        binding.chatBtn.setOnClickListener{
            val intent =Intent(this,ChatActivity::class.java)
            if(userId!=currentUser){
                intent.putExtra("recieverId", userId)
                startActivity(intent)
            }

        }

    }
}
