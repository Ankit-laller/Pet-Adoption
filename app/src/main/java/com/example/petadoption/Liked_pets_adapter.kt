package com.example.petadoption

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class LikedPetsAdapter : RecyclerView.Adapter<LikedPetsAdapter.CourseViewHolder>() {
    private val petList: ArrayList<String> = ArrayList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LikedPetsAdapter.CourseViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.liked_pet_items,
            parent, false
        )
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LikedPetsAdapter.CourseViewHolder, position: Int) {
        val item = petList[position]
        Glide.with(holder.itemView.context.applicationContext).load(item)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_4_24) // Add a placeholder image
                    .error(R.drawable.baseline_chat_bubble_24) // Add an error image
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            ).into(holder.imageView)
    }

    override fun getItemCount(): Int {

        return petList.size
    }
    fun updateData(updatedData: ArrayList<String>){
        petList.clear()
        petList.addAll(updatedData)
        notifyDataSetChanged()
        println("update working")
    }


    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.idIVCourse)
    }
}