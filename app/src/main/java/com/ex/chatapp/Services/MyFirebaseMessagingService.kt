package com.ex.chatapp.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.ex.chatapp.MainActivity
import com.ex.chatapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

const val CHANNEL_ID="notification_channel"
const val CHANNEL_NAME="com.ex.chatapp.Services"
const val BASE_URL = "https://fcm.googleapis.com"
const val SERVER_KEY = "ENTER SERVER KEY HERE"
const val CONTENT_TYPE = "application/json"

open class MyFirebaseMessagingService :FirebaseMessagingService(){

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = kotlin.random.Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setVibrate(longArrayOf(1000,1000,1000,1000,500,300,500,300,500,300))
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.Green.hashCode()
        }
        notificationManager.createNotificationChannel(channel)
    }
}