package com.ex.chatapp

data class User(
    var email:String,
    var photoUrl:String="no",
    var chats: List<String> = listOf<String>(),

    ) {
}