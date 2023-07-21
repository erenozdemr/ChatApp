package com.ex.chatapp

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ex.chatapp.View.ChatScreen
import com.ex.chatapp.View.LoginScreen
import com.ex.chatapp.View.MainScreen
import com.ex.chatapp.View.RegisterScreen
import com.ex.chatapp.ui.theme.ChatAppTheme
import com.ex.chatapp.ui.theme.loginText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {


                val navController= rememberNavController()
                NavHost(navController =navController, startDestination = "LoginScreen"){
                    composable(route = "LoginScreen"){
                        LoginScreen(navController = navController)

                    }

                    composable(route = "RegisterScreen"){
                        RegisterScreen(navController = navController)

                    }

                    composable(route = "MainScreen"){
                        MainScreen(navController = navController)

                    }

                    composable(route = "ChatScreen"){
                        ChatScreen(navController = navController)

                    }
                }

            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {

    }
}