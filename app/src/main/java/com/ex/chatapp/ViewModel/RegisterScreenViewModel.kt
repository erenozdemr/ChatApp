package com.ex.chatapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.chatapp.Model.User
import com.ex.chatapp.ui.theme.nickClaimedText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterScreenViewModel:ViewModel() {


    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(false)
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isError: LiveData<String> = _isError


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance().reference

    fun register(email: String, password: String, nick: String) {

        _isError.value=""
        _isLoading.value = true

        database.child("users").child(nick).get().addOnSuccessListener {
            if(it.exists()){
                _isLoading.value=false
                _isError.value= nickClaimedText
            }else{

                val user= User(email = email)
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                    database.child("users").child(nick).setValue(user).addOnSuccessListener {
                        _isLoading.value=false
                        _isSuccess.value=true

                    }.addOnFailureListener {
                        _isLoading.value=false
                        _isError.value=it.localizedMessage
                    }

                }.addOnFailureListener {
                    _isLoading.value=false
                    _isError.value=it.localizedMessage
                }


            }
        }.addOnFailureListener {
            _isLoading.value=false
            _isError.value=it.localizedMessage
        }


           /*
            .addSnapshotListener{ value, error ->


            if(error==null){
                if(value==null || value.isEmpty){
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener { exception ->
                            _isLoading.value = false
                            _isError.value = exception.localizedMessage
                            Log.e("Error",exception.localizedMessage!!)
                        }
                        .addOnSuccessListener {
                            var hash=HashMap<String,Any>()
                            hash.put("nick",nick)
                            hash.put("email",email)
                            hash.put("chats", listOf<String>())
                            hash.put("photoUrl","https://firebasestorage.googleapis.com/v0/b/kunapp-17107.appspot.com/o/images%2Fa73d61ed-3154-4f20-b2da-29444fe08057.jpg?alt=media&token=ee6515c6-3303-4d76-a24c-9c1222b0877b")
                            database.collection("Users").add(hash).addOnSuccessListener {
                                _isLoading.value = false
                                _isSuccess.value = true
                            }.addOnFailureListener {
                                _isLoading.value = false
                                _isError.value = it.localizedMessage
                                Log.e("Error",it.localizedMessage!!)
                            }.addOnCanceledListener {
                                _isLoading.value = false
                            }


                        }
                        .addOnCompleteListener {
                            _isLoading.value = false
                        }

                }else{
                    _isLoading.value = false
                    _isError.value="Bu nick daha önceden alınmış"
                    Log.e("Error","Bu nick daha önceden alınmış")
                }
            }else{
                _isLoading.value = false
                _isError.value=error.localizedMessage
                Log.e("Error",error.localizedMessage!!)
            }
        }*/


    }
}