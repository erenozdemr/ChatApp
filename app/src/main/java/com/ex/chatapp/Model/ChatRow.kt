package com.ex.chatapp.Model

import com.google.firebase.Timestamp


data class ChatRow(
    val otherUser: SimpleUser,
    val lastMessage:String,
    val chatRowId: String,
    val date: Long,
    val whoSendLastmessage:String

) {
}