package com.ex.chatapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("")
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val isError: LiveData<String> = _isError


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance().reference

    fun directLogin(){

        if(auth.currentUser!=null){
            _isLoading.value=true
            database.child("users").orderByChild("email").equalTo(auth.currentUser!!.email).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    _isLoading.value=false
                    snapshot.children.forEach {
                        _isSuccess.value=it.key
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _isLoading.value=false
                    _isError.value=error.message
                }

            })

        }
    }
    fun Login(email:String,password:String){
        _isLoading.value=true
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            database.child("users").orderByChild("email").equalTo(auth.currentUser!!.email).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    _isLoading.value=false
                    snapshot.children.forEach {
                        _isSuccess.value=it.key
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _isLoading.value=false
                    _isError.value=error.message
                }

            })


        }.addOnFailureListener {
            _isLoading.value=false
            _isError.value=it.localizedMessage
        }
    }
}