package com.ex.chatapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivityViewModel:ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance().reference

    private val _nick=MutableLiveData<String?>(null)

    fun saveNick(nick:String){
        _nick.value=nick
    }
    fun saveToken(token:String){
        if(_nick.value!=null){
            database.child("users").child(_nick.value!!).child("token").setValue(token).addOnSuccessListener {

            }
        }
    }

    fun setStatus(online:Boolean){
        if(auth.currentUser!=null && _nick.value!=null){
            database.child("users").child(_nick.value!!).child("status").setValue(online)
        }
    }
}