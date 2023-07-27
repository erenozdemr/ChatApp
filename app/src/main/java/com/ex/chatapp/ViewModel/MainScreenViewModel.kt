package com.ex.chatapp.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.chatapp.Model.Chat
import com.ex.chatapp.Model.ChatRow
import com.ex.chatapp.Model.Message
import com.ex.chatapp.Model.SimpleChat
import com.ex.chatapp.Model.SimpleUser
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.type.Date
import java.util.UUID

class MainScreenViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _chatList = MutableLiveData(listOf<ChatRow>())
    private val _goWithID = MutableLiveData("")
    private val _isError = MutableLiveData("")
    private val _searchList = MutableLiveData(listOf<SimpleUser>())
    private val allNicks = MutableLiveData(listOf<SimpleUser>())

    val chatList: LiveData<List<ChatRow>> = _chatList
    val isLoading: LiveData<Boolean> = _isLoading
    val goWithID: LiveData<String> = _goWithID
    val isError: LiveData<String> = _isError
    val searchList: LiveData<List<SimpleUser>> = _searchList


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun searchNick(nick: String, userNick: String) {
        if (nick.isBlank()) {
            _searchList.value = listOf()
        } else if (allNicks.value.isNullOrEmpty()) {

            database.child("users").get().addOnSuccessListener { dataSnapshot ->
                val list = mutableListOf<SimpleUser>()
                dataSnapshot.children.forEach { snapshot ->
                    val nickName = snapshot.key
                    val photoUrl = snapshot.child("photoUrl").getValue(String::class.java) ?: "no"
                    if (nickName != userNick) {
                        val user = SimpleUser(nickName!!, photoUrl)
                        list.add(user)
                    }

                }

                // Tüm nick'leri "allNicks" değişkenine ata
                allNicks.value = list
            }.addOnFailureListener { exception ->
                _isError.value = exception.localizedMessage
            }
        } else {
            _searchList.value =
                allNicks.value!!.filter { it.nick.contains(nick, ignoreCase = true) }
        }
    }

    fun goWithNick(userNick: String, otheruserNick: String) {
        _isLoading.value = true

        // Kullanıcının sohbetlerini çekin ve karşılaştırma yapın
        database.child("users").child(userNick).child("chats").get()
            .addOnSuccessListener { dataSnapshot ->
                val chats = dataSnapshot.children

                var id = ""

                for (chatSnapshot in chats) {
                    println(chatSnapshot.childrenCount)
                    val chat = chatSnapshot.child("otherUser").getValue(String::class.java)
                    if (chat != null && chat == otheruserNick) {
                        id = chatSnapshot.child("id").getValue(String::class.java)!!
                        break
                    }
                }

                if (id.isBlank()) {
                    // Eşleşen bir sohbet kimliği yok, yeni bir sohbet başlatın ve her iki kullanıcının "chats" dizisine ekleyin
                    val uuid = UUID.randomUUID().toString()
                    val chatUser = Chat(uuid, otheruserNick)
                    val chatOther = Chat(uuid, userNick)

                    database.child("users").child(userNick).child("chats").child(uuid)
                        .setValue(chatUser)
                        .addOnSuccessListener {
                            database.child("users").child(otheruserNick).child("chats").child(uuid)
                                .setValue(chatOther).addOnSuccessListener {
                                    _isLoading.value = false
                                    _goWithID.value = uuid

                                }
                        }

                } else {
                    // Eşleşen bir sohbet kimliği bulundu
                    _isLoading.value = false
                    _goWithID.value = id
                }
            }
            .addOnFailureListener { exception ->
                // Verileri çekerken hata durumu
                println("Veri çekerken hata oluştu: ${exception.message}")
                _isLoading.value = false
            }
    }

    fun loadMessages(nick: String) {
        _isLoading.value = true

        database.child("users").child(nick).child("chats").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var children = snapshot.children
                    val list= mutableListOf<SimpleChat>()

                    for (child in children) {
                      val temp: SimpleChat =SimpleChat(
                          child.child("id").getValue(String::class.java)!!,
                      child.child("otherUser").getValue(String::class.java)!!)
                        list.add(temp)
                    }
                    database.child("chats").addListenerForSingleValueEvent(
                        object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                            val children2=snapshot.children
                                val chatList= mutableListOf<ChatRow>()

                                children2.forEach { child ->
                                  list.forEach{ listItem->
                                      if (child.key==listItem.id){
                                          val messageList= mutableListOf<Message>()
                                          val child3=child.children
                                          for (it in child3){
                                              val mesage =Message(it.child("text").getValue(String::class.java)!!,
                                              it.child("sender").getValue(String::class.java)!!,
                                              it.child("messageId").getValue(String::class.java)!!,
                                              it.child("date").getValue(Long::class.java)!!


                                             )

                                              messageList.add(mesage)

                                          }
                                          messageList.sortBy {
                                              it.date
                                          }
                                          val chatRow=ChatRow(
                                              otherUser = SimpleUser(listItem.otherUser, "no")
                                              ,messageList.get(messageList.size-1).text,
                                              listItem.id,
                                              messageList.get(messageList.size-1).date,
                                              messageList.get(messageList.size-1).sender
                                          )
                                            chatList.add(chatRow)

                                      }

                                  }
                                }

                                chatList.sortByDescending {
                                    it.date
                                }
                                _isLoading.value=false
                                _chatList.value=chatList
                                loadProfiles()



                            }

                            override fun onCancelled(error: DatabaseError) {
                                _isLoading.value=false
                                _isError.value=error.message
                            }

                        }
                    )
                }

                override fun onCancelled(error: DatabaseError) {
_isLoading.value=false
                    _isError.value=error.message
                }


            }
        )

    }

fun loadProfiles(){
    if(!_chatList.value.isNullOrEmpty()){
        database.child("users").addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users=snapshot.children
                    chatList.value!!.forEach {
                     it.otherUser.photoUrl= snapshot.child(it.otherUser.nick).child("photoUrl").getValue(String::class.java)!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    _isError.value=error.message
                }

            }
        )
    }
}
}



