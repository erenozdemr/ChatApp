package com.ex.chatapp.Model

data class ChatRow(
    val otherUser: SimpleUser,
    val lastMessage:String,
    val chatRowId: String,
    val date:String,
    val whoSendLastmessage:String

) {
}