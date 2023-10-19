package com.example.adoptablepaws.fragments

import ScaleTransformer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoption.DetailActivity
import com.example.petadoption.PetImageAdapter
import com.example.petadoption.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageList = listOf(
            R.drawable.dog,
            R.drawable.dog,
            R.drawable.dog,
            R.drawable.dog,
            R.drawable.dog,
            R.drawable.dog,
        )

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerview)

        val adapter = PetImageAdapter(imageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val scaleTransformer = ScaleTransformer(0.9f)
        recyclerView.addItemDecoration(scaleTransformer)

        val targetPosition = 1 // Second index
        recyclerView.post {
            recyclerView.scrollToPosition(targetPosition)
        }
        adapter.setOnClickListener(object :PetImageAdapter.OnClickListener{
            override fun onClick(image: Int) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                // Passing the data to the
                // EmployeeDetails Activity
                intent.putExtra("detail_Screen", image)
                startActivity(intent)
            }

        })

        return rootView
    }
}