package com.example.petadoption.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.petadoption.PutForAdoption
import com.example.petadoption.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =inflater.inflate(R.layout.fragment_profile, container, false)
        var add_pet :ImageButton = rootView.findViewById(R.id.put_for_adoption)
        add_pet.setOnClickListener{
            val intent =Intent(requireContext(), PutForAdoption::class.java)
            startActivity(intent)
        }
        return rootView
    }

}