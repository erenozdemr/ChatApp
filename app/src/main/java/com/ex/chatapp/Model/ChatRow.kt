package com.ex.chatapp.Model

data class ChatRow(
    var otherUser: SimpleUser,
    var lastMessage:String,
    var chatRowId: String,
    val date:String

) {
}