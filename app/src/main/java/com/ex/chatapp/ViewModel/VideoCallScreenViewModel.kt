package com.ex.chatapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.chatapp.Model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VideoCallScreenViewModel:ViewModel() {
    private val _hasAudioPermisson= mutableStateOf(false)
    val hasAudioPermission: State<Boolean> =_hasAudioPermisson

    private val _hasCameraPermisson= mutableStateOf(false)
    val hasCameraPermission: State<Boolean> =_hasCameraPermisson

    fun onPermissonResult(
        acceptedAudiopermisson:Boolean,
        acceptedCamerapermisson:Boolean
    ){
        _hasAudioPermisson.value=acceptedAudiopermisson
        _hasCameraPermisson.value=acceptedCamerapermisson
    }








}