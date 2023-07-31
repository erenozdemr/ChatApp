package com.ex.chatapp.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.chatapp.Model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.UUID

class ChatScreenViewModel(chatId: String):ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _status = MutableLiveData(false)
    private val _messagleList = MutableLiveData(listOf<Message>())
    private val _isSuccess = MutableLiveData("")
    private val _profileUrl = MutableLiveData("no")
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val status: LiveData<Boolean> = _status
    val messageList: LiveData<List<Message>> = _messagleList
    val isSuccess: LiveData<String> = _isSuccess
    val profileUrl: LiveData<String> = _profileUrl
    val isError: LiveData<String> = _isError

    private val _agoraId = MutableLiveData("")
    val agoraID: LiveData<String> = _agoraId


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance().reference
    private val storage=FirebaseStorage.getInstance().reference.child("images")

    init {
        database.child("chats").child(chatId).orderByChild("date").addChildEventListener(object:ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var list= mutableListOf<Message>()
                val children=snapshot

                val message=Message(children.child("text").getValue(String::class.java)?:"",
                    children.child("sender").getValue(String::class.java)?:"",
                    children.key?:"",
                    children.child("date").getValue(Long::class.java)?:16565,
                    children.child("imageUrl").getValue(String::class.java)
                )

                var temp=if(!_messagleList.value.isNullOrEmpty())_messagleList.value as MutableList? else mutableListOf()
                temp?.add(message)
                _isLoading.value=false
                _messagleList.value=temp
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("system message","on cancelled e girildi")
                _isLoading.value=false
                _isError.value=error.message
            }

        })
    }



    fun loadChat(userNick:String,chatId:String){
        _isLoading.value=true
        database.child("chats").child(chatId).orderByChild("date").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list= mutableListOf<Message>()
                val children=snapshot.children
                for (child in children){
                    val message=Message(child.child("text").getValue(String::class.java)?:"",
                        child.child("sender").getValue(String::class.java)?:"",
                        child.key?:"",
                        child.child("date").getValue(Long::class.java)?:16565,
                        child.child("imageUrl").getValue(String::class.java)
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
        database.child("users").child(otherUserNick).child("photoUrl").get().addOnSuccessListener {
            _profileUrl.value=it.getValue(String::class.java)
        }
        database.child("users").child(otherUserNick).child("status").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _status.value=snapshot.getValue(Boolean::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun sendNotification(userNick: String,message:String,otherUserNick:String){
       database.child("users").child(otherUserNick).child("token").get().addOnSuccessListener {
           try {
               val jsonObject=JSONObject()

               val notificationJSONObject=JSONObject()
               notificationJSONObject.put("title","${userNick} Sana Yeni Bir Mesaj Gönderdi")
               notificationJSONObject.put("body",message)

               val dataObject=JSONObject()

               jsonObject.put("notification",notificationJSONObject)
               jsonObject.put("data",dataObject)
               jsonObject.put("to",it.getValue(String::class.java))

               callApi(jsonObject)




           }catch (e:Exception){
                Log.e("Json error",e.localizedMessage)
           }
       }


    }
    private fun callApi(jsonObject:JSONObject){

        database.child("utils").child("serverKey").get().addOnSuccessListener {
            val key=it.getValue(String::class.java)
            val json= "application/json; charset=utf-8".toMediaType()
            val client=OkHttpClient()
            var url="https://fcm.googleapis.com/fcm/send"
            var body= jsonObject.toString().toRequestBody(json)
            val request=Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer $key")
                .build()
            client.newCall(request = request).enqueue(object:Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("okthttp errır",e.localizedMessage)
                }

                override fun onResponse(call: Call, response: Response) {


                }

            })
        }

    }

    fun sendMessage(userNick:String,message:String,chatId: String,imageUri:Uri?){
        _isLoading.value=true
        val id=UUID.randomUUID().toString()
        if (imageUri!=null){
            val imageID=UUID.randomUUID().toString()
            storage.child(imageID).putFile(imageUri).addOnSuccessListener {
                storage.child(imageID).downloadUrl.addOnSuccessListener {
                    val mes=Message(
                        text = message,
                        sender = userNick,
                        messageId = id,
                        date = com.google.firebase.Timestamp.now().seconds,
                        imageUrl = it.toString()
                    )
                    database.child("chats").child(chatId).child(id).setValue(mes).addOnSuccessListener {
                        _isLoading.value=false
                        loadChat(userNick,chatId)

                    }
                }.addOnFailureListener {
                    _isLoading.value=false
                }
            }.addOnFailureListener {
                _isLoading.value=false
            }
        }else{
            val mes=Message(
                text = message,
                sender = userNick,
                messageId = id,
                date = com.google.firebase.Timestamp.now().seconds,
            )
            database.child("chats").child(chatId).child(id).setValue(mes).addOnSuccessListener {
                _isLoading.value=false
                loadChat(userNick,chatId)

            }
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