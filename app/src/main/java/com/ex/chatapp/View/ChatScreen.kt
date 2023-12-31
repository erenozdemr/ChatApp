package com.ex.chatapp.View

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ex.chatapp.Model.Message
import com.ex.chatapp.R
import com.ex.chatapp.ViewModel.ChatScreenViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(navController: NavController,
               userNick:String,
               otherUserNick:String,
               chatID:String,
               viewModel: ChatScreenViewModel=remember{ChatScreenViewModel(chatID)}){
    viewModel.loadChat(userNick,chatID)
    viewModel.getPhotoOfOther(otherUserNick)
    viewModel.getAgorId()
    BackHandler() {
        navController.navigate("MainScreen/$userNick"){
            launchSingleTop=true
        }
    }


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
    val status by viewModel.status.observeAsState(initial = false)
    val profileUrl by viewModel.profileUrl.observeAsState(initial = "no")
    val isError by viewModel.isError.observeAsState(initial = "")
    val agoraid by viewModel.agoraID.observeAsState(initial = "")
    val isSucces by viewModel.isSuccess.observeAsState(initial = "")
    val imageLoader = ImageLoader(LocalContext.current)

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val messageList by viewModel.messageList.observeAsState(listOf())
    var prevError by remember { mutableStateOf("") }
    var painter :Painter? by remember {
        mutableStateOf(null)
    }

    var message by remember{ mutableStateOf("") }


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri.value = uri }
    )
    val pickImageButton = {
        pickImageLauncher.launch("image/*")

    }
    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUri.value)
        .target { result ->
            val bitmap = (result as BitmapDrawable).bitmap
            painter = BitmapPainter(bitmap.asImageBitmap())
        }
        .build()
    if (imageUri.value != null) {
        imageLoader.enqueue(request)
    }

   Scaffold(
       modifier = Modifier.fillMaxSize(),
       topBar = {
           Row(modifier = Modifier
               .fillMaxWidth()
               .background(
                   Color.LightGray,
                   RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
               ), horizontalArrangement = Arrangement.SpaceBetween) {

               Row {
                   Box(
                       modifier = Modifier
                           .size(60.dp)



                   ) {
                       if (profileUrl=="no"){
                           Image(
                               painter = painterResource(id = R.drawable.default_profile_photo),
                               contentDescription = "Profile Picture",
                               modifier = Modifier
                                   .fillMaxSize()
                                   .clip(CircleShape)
                                   .border(0.dp, Color.Black, CircleShape),
                               contentScale = ContentScale.FillBounds,
                           )
                       }else{
                           Image(
                               painter = rememberAsyncImagePainter(model = profileUrl),
                               contentDescription = "Profile Picture",
                               modifier = Modifier
                                   .fillMaxSize()
                                   .clip(CircleShape)
                                   .border(0.dp, Color.Black, CircleShape),
                               contentScale = ContentScale.FillBounds,
                           )
                       }
                       Box(
                           modifier = Modifier
                               .align(Alignment.BottomEnd)
                               .size(20.dp)
                               .background(
                                   if (status) Color.Green else Color.Red,
                                   CircleShape
                               )
                       )
                   }


                   Text( modifier = Modifier.padding(top = 15.dp), text = otherUserNick)

               }
               Box(
                   modifier = Modifier
                       .size(40.dp)
                       .padding(top = 10.dp, end = 10.dp)
                       .clickable {
                           if (agoraid.isNotBlank()) {
                               navController.navigate("VideoCallScreen/$chatID/$agoraid")
                           }
                       }


               ) {

                       Image(
                           painter = painterResource(id = R.drawable.video_call_image),
                           contentDescription = "video call button",
                           modifier = Modifier
                               .fillMaxSize()
                               ,
                           contentScale = ContentScale.FillBounds,
                       )

               }
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
                       MessageBubble(
                           message = message,
                           userNick = userNick
                       )
                   }
               }
           }

           Column(modifier = Modifier
               .fillMaxWidth()
               .background(Color.LightGray)) {
               if(imageUri.value!=null && painter!=null){
                 Image(painter = painter!!, contentDescription = "message image", modifier = Modifier.size(height = 150.dp, width = 300.dp))
               }
               Row(modifier = Modifier.padding(bottom = 5.dp,top=5.dp)) {
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
                   Spacer(modifier = Modifier.width(5.dp))
                   ImageButton(
                       modifier = Modifier
                           .size(50.dp)
                           ,
                       painter = painterResource(id = R.drawable.plus)
                   ) {
                       pickImageButton()
                   }
                   Spacer(modifier = Modifier.width(3.dp))
                   ImageButton(
                       modifier = Modifier
                           .size(50.dp)
                           ,
                       painter = painterResource(id = R.drawable.send_m)
                   ) {
                       if (message.isNotBlank() || imageUri.value!=null) {
                           viewModel.sendMessage(userNick, message, chatID,imageUri.value)
                           viewModel.sendNotification(userNick,message,otherUserNick)
                           message = ""
                           imageUri.value=null
                           painter=null
                       }
                   }
               }
           }
       }
   }





}




@Composable
fun MessageBubble(message: Message, userNick:String){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()){
        if (userNick==message.sender){
            Spacer(modifier = Modifier.size(width = 60.dp, height = 10.dp))
        }
        Row( modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .background(
                if (userNick == message.sender) {
                    Color.Cyan
                } else {
                    Color.Green
                }, shape = RoundedCornerShape(10.dp)
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if(!message.imageUrl.isNullOrEmpty()){
                    Image(
                        painter = rememberAsyncImagePainter(model = message.imageUrl!!),
                        contentDescription = "message image ",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )

                }
                if(message.text.isNotBlank()){
                    Text(modifier=Modifier.padding(5.dp),
                        text = message.text
                    )
                }

            }

        }


        if (userNick!=message.sender){
            Spacer(modifier = Modifier.size(width = 60.dp, height = 10.dp))
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreenGenerate(userNick = "ggci", navController = NavController(LocalContext.current), viewModel = ChatScreenViewModel(""), otherUserNick = "hakan", chatID = "ads" )
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
