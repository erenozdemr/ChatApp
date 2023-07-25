package com.ex.chatapp.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ex.chatapp.Model.Message
import com.ex.chatapp.R
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
    val profileUrl by viewModel.profileUrl.observeAsState(initial = "no")
    val isError by viewModel.isError.observeAsState(initial = "")
    val isSucces by viewModel.isSuccess.observeAsState(initial = "")
    val messageList by viewModel.messageList.observeAsState(listOf())
    var prevError by remember { mutableStateOf("") }

    var message by remember{ mutableStateOf("") }


   Scaffold(
       modifier = Modifier.fillMaxSize(),
       topBar = {
           Row(modifier = Modifier.fillMaxWidth().background(Color.LightGray, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))) {
               if (profileUrl=="no"){
                   Image(modifier=Modifier.size(70.dp),painter = painterResource(id = R.drawable.default_profile_photo), contentDescription ="user's profile photo" )
               }else{
                   Image(modifier=Modifier.size(70.dp),painter = rememberAsyncImagePainter(model = profileUrl), contentDescription ="user's profile photo" )
               }
               Text(text = otherUserNick)
           }
       }
            ) {
       Column(modifier = Modifier.padding(it)) {

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


           Box(modifier = Modifier.weight(1f)) {
               LazyColumn(
                   modifier = Modifier.fillMaxHeight(),
                   reverseLayout = true,

                   ) {
                   items(items = messageList.reversed()) { message ->
                       MessageBuble(
                           message = message,
                           userNick = userNick
                       )
                   }
               }
           }

           Row(modifier = Modifier.padding(bottom = 5.dp)) {
               OutlinedTextField(
                   modifier = Modifier.weight(1f),
                   value = message, maxLines = 1,
                   onValueChange = { message = it },
                   placeholder = { Text(text = "message...") },
                   colors = TextFieldDefaults.textFieldColors(
                       containerColor = Color.Gray,
                       unfocusedIndicatorColor = Color.Transparent,
                       focusedIndicatorColor = Color.Transparent
                   ),
                   shape = CircleShape
               )
               Spacer(modifier = Modifier.width(8.dp))
               ImageButton(
                   modifier = Modifier.size(50.dp),
                   painter = painterResource(id = R.drawable.send_m)
               ) {
                   if (message.isNotBlank()) {
                       viewModel.sendMessage(userNick, message, chatID)
                       message = ""
                   }
               }
           }
       }
   }





}



@Composable
fun MessageBuble(message: Message,userNick:String){
    Text(text = "${message.sender} : ${message.text}")
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreenGenerate(userNick = "ggci", navController = NavController(LocalContext.current), viewModel = ChatScreenViewModel(), otherUserNick = "hakan", chatID = "ads" )
}

@Composable
fun ImageButton(modifier:Modifier, painter: Painter, description:String?=null, enabled:Boolean=true, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick, enabled = enabled),
        color = Color.Transparent
    ) {
        Image(
            painter = painter,
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
