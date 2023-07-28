package com.ex.chatapp.Model

data class User(
    var email:String,
    var photoUrl:String="no",
    var chats: List<String> = listOf<String>(),
    var status:Boolean=false


    ) {
}