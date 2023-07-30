package com.ex.chatapp.Model

data class Message(
    var text:String,
    var sender:String,
    var messageId:String,
    var date: Long,
    var imageUrl:String?=null
) {


}