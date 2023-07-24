package com.ex.chatapp.Model

import android.net.Uri
import com.google.firebase.Timestamp
import java.util.Date

data class MesageRow(
    var id:String,
    var user1: String , var user2: String ,
    var date: Timestamp , var lastMessage:String


) {
}