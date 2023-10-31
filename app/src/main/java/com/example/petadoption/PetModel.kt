package com.example.petadoption

import java.io.Serializable

class PetModel (
    var userId:String?=null,
    var postId :String?=null,
    var petName:String?=null,
    var imageUrl:String?=null,
    var petAge:String?=null,
    var petGender:String?=null,
    var petDescription:String?=null,
    var petType:String?=null,
    var userData:UserModel?=null,
    var isAdopted:Boolean?=null
)