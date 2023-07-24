package com.ex.chatapp.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ex.chatapp.Model.Message
import com.ex.chatapp.ViewModel.ChatScreenViewModel
import com.ex.chatapp.ViewModel.MainScreenViewModel

@Composable
fun ChatScreen(navController: NavController,
               userNick:String,
               otherUserNick:String,
               chatID:String,
               viewModel: ChatScreenViewModel=remember{ChatScreenViewModel()}){
    viewModel.loadChat(userNick,chatID)

    ChatScreenGenerate(
        navController = navController,
        userNick = userNick,
        otherUserNick = otherUserNick,
        chatID = chatID,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenGenerate(navController: NavController,
                       userNick:String,
                       otherUserNick:String,
                       chatID:String,
                       viewModel: ChatScreenViewModel){
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val isError by viewModel.isError.observeAsState(initial = "")
    val isSucces by viewModel.isSuccess.observeAsState(initial = "")
    val messageList by viewModel.messageList.observeAsState(listOf())
    var prevError by remember { mutableStateOf("") }

    var message by remember{ mutableStateOf("") }
    Column() {

        if (isLoading) {
            prevError=""

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }
        if (isError.isNotEmpty() && prevError != isError) {
            Toast.makeText(LocalContext.current, isError, Toast.LENGTH_LONG).show()
            prevError = isError
        }


        LazyColumn(){
            items(messageList){
                MessageBuble(message = it)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()){

            OutlinedTextField(value = message, onValueChange = {
                message=it
            })
            Button(onClick = {
                if (message.isNotBlank()){
                    viewModel.sendMessage(userNick,message,chatID)
                }

            }) {
                Text(text = "Send")
            }
        }
    }





}

@Composable
fun MessageBuble(message: Message){
    Text(text = "${message.sender} : ${message.text}")
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreenGenerate(userNick = "ggci", navController = NavController(LocalContext.current), viewModel = ChatScreenViewModel(), otherUserNick = "hakan", chatID = "ads" )
}