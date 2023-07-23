package com.ex.chatapp.View

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MainScreen(nick:String,navController: NavController){
MainScreenGenerate(nick = nick, navController = navController)
}
@Composable
fun MainScreenGenerate(nick:String,navController: NavController){
Text(text = nick)
}