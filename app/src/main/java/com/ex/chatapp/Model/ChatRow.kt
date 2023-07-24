package com.ex.chatapp.Model

data class ChatRow(
    var otherUser: SimpleUser,
    var messageList:List<Message>,
    var chatRowId: String

) {
}