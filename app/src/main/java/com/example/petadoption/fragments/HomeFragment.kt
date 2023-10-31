package com.example.adoptablepaws.fragments

import ScaleTransformer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petadoption.ChatHomeActivity
import com.example.petadoption.DetailActivity
import com.example.petadoption.MainActivity
import com.example.petadoption.PetImageAdapter
import com.example.petadoption.PetModel
import com.example.petadoption.R
import com.example.petadoption.Repo
import com.example.petadoption.UserModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    val imageList = listOf(
        R.drawable.dog,
        R.drawable.dog,
        R.drawable.dog,
        R.drawable.dog,
        R.drawable.dog,
        R.drawable.dog,
    )
    private lateinit var petDataList:ArrayList<PetModel>
    private lateinit var petImageAdapter: PetImageAdapter
    private var currentuser =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        petDataList = ArrayList()

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val radioGroup = view.findViewById<RadioGroup>(R.id.filterView)
        var selectedChipValue  = ""
        val chatBtn: ImageView = view.findViewById(R.id.chat_frag)
        chatBtn.setOnClickListener {
            val intent = Intent(context, ChatHomeActivity::class.java)
            startActivity(intent)
        }
        val userName = view.findViewById<TextView>(R.id.tv1)
        currentuser = Repo().getUserData(){ ownerName->
            userName.text= "Hello $ownerName :)"
        }




        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            selectedChipValue = radioButton.text.toString()
            fetchParticularData(selectedChipValue)
            Log.d("test pet",selectedChipValue!! )
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fetchData()

         petImageAdapter = PetImageAdapter()
        recyclerView.adapter = petImageAdapter
        val scaleTransformer = ScaleTransformer(0.9f)
        recyclerView.addItemDecoration(scaleTransformer)

        val targetPosition = 1 // Second index
        recyclerView.post {
            recyclerView.scrollToPosition(targetPosition)
        }
        petImageAdapter.setOnClickListener(object :PetImageAdapter.OnClickListener{
            override fun onClick(petdata: PetModel) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(petName, petdata.petName)
                intent.putExtra(petAge, petdata.petAge)
                intent.putExtra(petDescription, petdata.petDescription)
                intent.putExtra(petImage, petdata.imageUrl)
                intent.putExtra(ownerName, petdata.userData?.userName)
                intent.putExtra(postId,petdata.postId)
                intent.putExtra(isAdopted, petdata.isAdopted)
                intent.putExtra(userId, petdata.userId)
                startActivity(intent)
            }

        })
    }

    private fun fetchData(){
        val db = Firebase.firestore
        db.collection("User Posts")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    petDataList.clear()
                    for(data in document){
                        val dataModel = data.toObject(PetModel::class.java)
                        petDataList.add(dataModel)
                    }
                    petImageAdapter.updateData(petDataList)

                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }


    private fun fetchParticularData(petType:String){
        val db = Firebase.firestore
        db.collection("User Posts")
            .whereEqualTo("petType", petType)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    petDataList.clear()
                    for(data in document){
                        val dataModel = data.toObject(PetModel::class.java)
                        petDataList.add(dataModel)
                    }
                    petImageAdapter.updateData(petDataList)

                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that may occur
                println("Error getting documents: $exception")
            }
    }
    companion object{
        const val petName= "petName"
        const val petAge= "petAge"
        const val petImage="petImage"
        const val petDescription ="petDescription"
        const val ownerName ="OwnerName"
        const val postId ="postId"
        const val isAdopted ="isAdopted"
        const val userId ="userId"
    }
}



