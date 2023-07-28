package com.ex.chatapp

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ex.chatapp.View.ChatScreen
import com.ex.chatapp.View.LoginScreen
import com.ex.chatapp.View.MainScreen
import com.ex.chatapp.View.RegisterScreen
import com.ex.chatapp.View.VideoCallScreen
import com.ex.chatapp.ui.theme.ChatAppTheme
import com.ex.chatapp.ui.theme.loginText
import com.google.firebase.FirebaseApp


lateinit var viewModel: MainActivityViewModel
class MyApplication : Application() {
    override fun onCreate() {
        viewModel= MainActivityViewModel()
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(applicationContext)
        viewModel=MainActivityViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {


                val navController= rememberNavController()
                NavHost(navController =navController, startDestination = "LoginScreen"){
                    composable(route = "LoginScreen"){

                        LoginScreen(navController = navController){
                            onDestroy()
                        }

                    }

                    composable(route = "RegisterScreen"){
                        RegisterScreen(navController = navController)

                    }

                    composable(route = "MainScreen/{nick}", arguments = listOf(
                        navArgument("nick"){
                            type= NavType.StringType
                        }
                    )){
                        val nick=it.arguments?.getString("nick","noNick")
                        viewModel.saveNick(nick!!)
                        viewModel.setStatus(online = true)

                        MainScreen(navController = navController, nick = nick!!, loginNavController =navController)

                    }

                    composable("VideoCallScreen/{roomName}/{agoraid}", arguments = listOf(
                        navArgument("roomName"){
                            type= NavType.StringType
                        },
                        navArgument("agoraid"){
                            type= NavType.StringType
                        }
                    )){
                        val roomName=it.arguments?.getString("roomName","noNick")
                        val agoraid=it.arguments?.getString("agoraid","noNick")

                        VideoCallScreen(navController,roomName!!,agoraid!!)

                    }

                    composable(route = "ChatScreen/{userNick}/{otherUserNick}/{chatID}", arguments = listOf(
                        navArgument("userNick"){
                            type= NavType.StringType
                        },
                        navArgument("otherUserNick"){
                            type= NavType.StringType
                        },
                        navArgument("chatID"){
                            type= NavType.StringType
                        }
                    )){
                        val userNick=it.arguments?.getString("userNick","noNick")
                        val otherUserNick=it.arguments?.getString("otherUserNick","noNick")
                        val chatID=it.arguments?.getString("chatID","noID")
                        viewModel.saveNick(userNick!!)
                        viewModel.setStatus(online = true)

                        ChatScreen(navController = navController, userNick = userNick!!,otherUserNick=otherUserNick!!,chatID=chatID!!)

                    }
                }

            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setStatus(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setStatus(false)
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {

    }
}