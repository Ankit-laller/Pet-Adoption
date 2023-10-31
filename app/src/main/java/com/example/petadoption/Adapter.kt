package com.example.petadoption

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class PetImageAdapter() : RecyclerView.Adapter<PetImageAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private val data: ArrayList<PetModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        print("test image url $item.imageUrl")
        Glide.with(holder.itemView.context.applicationContext).load(item.imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_4_24) // Add a placeholder image
                    .error(R.drawable.baseline_chat_bubble_24) // Add an error image
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            ).into(holder.imageView)
        holder.textView.text = item.petName
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rv_pet_image)
        val textView :TextView = itemView.findViewById(R.id.pet_nam)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun updateData(updatedData: ArrayList<PetModel>){
        data.clear()
        data.addAll(updatedData)
        notifyDataSetChanged()
        println("update working")
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(petdata: PetModel)
    }

}
