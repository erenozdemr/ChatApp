package com.ex.chatapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ex.chatapp.Model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _messagleList = MutableLiveData(listOf<Message>())
    private val _isSuccess = MutableLiveData("")
    private val _profileUrl = MutableLiveData("no")
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val messageList: LiveData<List<Message>> = _messagleList
    val isSuccess: LiveData<String> = _isSuccess
    val profileUrl: LiveData<String> = _profileUrl
    val isError: LiveData<String> = _isError

    private val _agoraId = MutableLiveData("")
    val agoraID: LiveData<String> = _agoraId

    private val _onJoinEvent = MutableSharedFlow<String>()
    val onjoinEvent = _onJoinEvent.asSharedFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance().reference



    fun loadChat(userNick:String,chatId:String){
        _isLoading.value=true
        database.child("chats").child(chatId).orderByChild("date").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list= mutableListOf<Message>()
                Log.e("system message","on data changed e girildi")
                val children=snapshot.children
                for (child in children){
                    val message=Message(child.child("text").getValue(String::class.java)?:"",
                                      child.child("sender").getValue(String::class.java)?:"",
                                             child.key?:"",
                                        child.child("date").getValue(String::class.java)?:""
                    )
                    list.add(message)
                }
                _isLoading.value=false
                _messagleList.value=list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("system message","on cancelled e girildi")
                _isLoading.value=false
                _isError.value=error.message
            }

        })
    }
    fun getPhotoOfOther(otherUserNick:String){
        database.child("users").child(otherUserNick).child("profileUrl").get().addOnSuccessListener {
            _profileUrl.value=it.getValue(String::class.java)
        }
    }
    fun sendMessage(userNick:String,message:String,chatId: String){
        _isLoading.value=true
        val id=UUID.randomUUID().toString()
        val mes=Message(
            text = message,
        sender = userNick,
        messageId = id,
        date = com.google.firebase.Timestamp.now().toDate().toString())
        database.child("chats").child(chatId).child(id).setValue(mes).addOnSuccessListener {
            _isLoading.value=false
            loadChat(userNick,chatId)

        }

    }


    fun getAgorId(){
        database.child("utils").child("agoraid").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _agoraId.value=snapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}