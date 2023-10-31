package com.example.petadoption.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoption.LikedPetsAdapter
import com.example.petadoption.Message
import com.example.petadoption.PetModel
import com.example.petadoption.R
import com.example.petadoption.Repo
import com.example.petadoption.msgAdapter
import com.google.firebase.database.DatabaseReference


class BlankFragment : Fragment() {
    private lateinit var recyclerView:  RecyclerView
    private lateinit var petList :ArrayList<String>
    private lateinit var mAdapter: LikedPetsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        petList = ArrayList()
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView =view.findViewById(R.id.rv1)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        mAdapter = LikedPetsAdapter()
        recyclerView.layoutManager = layoutManager
        Repo().getLikedPet { list->
            mAdapter.updateData(list)
        }
        recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()


    }
}