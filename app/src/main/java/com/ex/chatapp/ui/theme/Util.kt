package com.ex.chatapp.ui.theme

import android.util.Log
import androidx.compose.runtime.Composable
import com.ex.chatapp.Model.PushNotification
import com.ex.chatapp.Services.RetroiftInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun sendNotification(notification:PushNotification){
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response=RetroiftInstance.api.postNotification(notification)
            if(!response.isSuccessful){
                Log.e("send notification error",response.errorBody().toString())
            }
        }catch (e:Exception){
            Log.e("send notificaion error",e.localizedMessage)
        }
    }
}