package com.example.petadoption.fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoption.ChatActivity
import com.example.petadoption.R
import com.example.petadoption.UserModel

class ChatHomeAdapter(val context: Context) : RecyclerView.Adapter<ChatHomeAdapter.ViewHolder>()  {
    private var onClickListener: OnClickListener? = null
    private val data: ArrayList<UserModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        holder.textView.text =item.userName

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",item.userName)
            intent.putExtra("recieverId", item.userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView :TextView = itemView.findViewById(R.id.sender_names)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun updateData(updatedData: ArrayList<UserModel>){
        data.clear()
        data.addAll(updatedData)
        notifyDataSetChanged()
        println("update working")
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(userData: String?)
    }
}