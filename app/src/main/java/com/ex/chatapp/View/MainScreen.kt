package com.ex.chatapp.View

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ex.chatapp.Model.Message
import com.ex.chatapp.Model.ChatRow
import com.ex.chatapp.Model.SimpleUser
import com.ex.chatapp.Model.User
import com.ex.chatapp.R
import com.ex.chatapp.ViewModel.MainScreenViewModel
import com.ex.chatapp.ui.theme.you
import java.sql.Timestamp
import java.util.Locale

@Composable
fun MainScreen(nick:String,navController: NavController,viewModel: MainScreenViewModel =remember{MainScreenViewModel()}){
MainScreenGenerate(nick = nick, navController = navController,viewModel=viewModel)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenGenerate(nick:String,navController: NavController,viewModel: MainScreenViewModel){
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val isError by viewModel.isError.observeAsState(initial = "")
    val goWithID by viewModel.goWithID.observeAsState(initial = "")
    val chatList by viewModel.chatList.observeAsState(listOf())
    val searchItems by viewModel.searchList.observeAsState(initial = listOf())
    var prevError by remember { mutableStateOf("") }
    var otherNick by remember{ mutableStateOf("") }


    var search by remember {
        mutableStateOf("")
    }

    if(goWithID.isNotBlank()&&otherNick.isNotBlank()){
        navController.navigate("ChatScreen/$nick/$otherNick/$goWithID"){
            launchSingleTop = true
        }
    }

    Box() {
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

        Column(modifier = Modifier.fillMaxSize().padding(top = 25.dp)) {

            Box(modifier = Modifier.weight(1f)) {
                Column {


                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(items = chatList) {
                            ChatRow(chatRow = it, nick = nick)
                        }
                    }
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            OutlinedTextField(
                value = search,
                modifier = Modifier
                    .padding(start = 120.dp, end = 120.dp, top = 5.dp)
                    , textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = {
                    search = it.lowercase(Locale.UK)
                    viewModel.searchNick(search,nick)
                },
                label = { Text("Bir hesap ara", fontSize = 10.sp) }
            )
            if (searchItems.isNotEmpty()) {
                LazyColumn(

                ) {
                    items(searchItems) { item ->
                        SearchItem(item = item, navController = navController, userNick = nick ?: "nick couldn't found"){
                            otherNick=it
                            viewModel.goWithNick(nick, otheruserNick = otherNick)
                        }
                    }
                }
            }
        }
    }





}
@Composable
fun SearchItem(item: SimpleUser, navController: NavController, userNick: String,onClick:(String)->Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 120.dp, top = 8.dp, bottom = 8.dp, end = 120.dp)
            .border(1.dp, Color.Black, RectangleShape)
            .background(Color.LightGray)
            .clickable {
                onClick(item.nick)
            }
    ) {
        Text(
            item.nick,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Composable
fun ChatRow(chatRow: ChatRow, nick: String){

    Row(modifier = Modifier
        .background(Color.DarkGray)
        .border(width = 1.dp, Color.Blue)
        .fillMaxWidth()
        .padding(horizontal = 5.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row() {
            var painter= painterResource(id = R.drawable.default_profile_photo)
            if (!chatRow.otherUser.photoUrl.equals("no")){
                painter= rememberAsyncImagePainter(model =chatRow.otherUser.photoUrl )
            }

            Box(
                modifier = Modifier
                    .size(75.dp)
                    .border(1.dp, Color.Blue, CircleShape)

            ) {
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(0.dp, Color.Black, CircleShape),
                    contentScale = ContentScale.FillBounds,
                )
            }

            Column() {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = chatRow.otherUser.nick,
                        modifier = Modifier.padding(start = 15.dp, top = 7.dp),
                        fontSize = 20.sp,
                        color = Color.LightGray,
                        maxLines = 1
                    )
                    Text(text = chatRow.messageList.get(chatRow.messageList.size-1).date.toString()
                        , fontSize = 10.sp)
                }


                Spacer(modifier = Modifier.padding(5.dp))
                Text(text =
                if (chatRow.messageList.get(chatRow.messageList.size-1).sender==nick){
                    "$you: ${chatRow.messageList.get(chatRow.messageList.size-1).text}"
                }else{
                    chatRow.messageList.get(chatRow.messageList.size-1).text
                }
                    , color = Color.White,
                    modifier = Modifier.padding(start = 5.dp),
                    fontSize = 15.sp,
                    maxLines = 1

                )
            }
        }







    }




}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreenGenerate(nick = "ggci", navController = NavController(LocalContext.current), viewModel = MainScreenViewModel() )
}