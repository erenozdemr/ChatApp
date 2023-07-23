package com.ex.chatapp.Model

import java.sql.Timestamp

data class Message(
    var text:String,
    var sender:String,
    var messageId:String,
    var date:Timestamp
) {


}