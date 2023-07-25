package com.ex.chatapp.View


import android.widget.ImageButton
import androidx.compose.foundation.BorderStroke

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

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
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
import com.ex.chatapp.Model.ChatRow
import com.ex.chatapp.Model.SimpleUser
import com.ex.chatapp.R
import com.ex.chatapp.ViewModel.MainScreenViewModel
import com.ex.chatapp.ui.theme.you
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun MainScreen(nick:String, navController: NavController, viewModel: MainScreenViewModel =remember{ MainScreenViewModel() },loginNavController: NavController){
    MainScreenGenerate(nick = nick, navController = navController, viewModel=viewModel, loginNavController  )
    viewModel.loadMessages(nick)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenGenerate(nick:String,navController: NavController,viewModel: MainScreenViewModel,loginNavController: NavController){
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

    if(!goWithID.isNullOrBlank()&&!otherNick.isNullOrBlank()){
        navController.navigate("ChatScreen/$nick/$otherNick/$goWithID"){
            launchSingleTop = true
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

   ModalNavigationDrawer(
       drawerContent = {
            ModalDrawerSheet() {
                Button(
                    onClick = {
                        val mAuth = FirebaseAuth.getInstance()

                        loginNavController.navigate("loginScreen") {
                            launchSingleTop = true
                         
                        }
                        mAuth.signOut()
                    },
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Çıkış yap")
                }
            }
       },
       drawerState = drawerState
   ) {

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

           Column(modifier = Modifier
               .fillMaxSize()
               .padding(top = 90.dp)) {

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
                Row() {
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
                    Image(painter = painterResource(id = R.drawable.user),
                        contentDescription = "new_message",
                        Modifier
                            .size(45.dp)
                            .padding(top = 5.dp)
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                }

                            })
                }

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




}

@Composable
fun SearchItem(item: String) {
    Row() {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable {
                    //   navController.navigate("profile_screen/$userNick/$item")
                }
                .padding(start = 25.dp, top = 8.dp, bottom = 4.dp, end = 5.dp)
                .size(45.dp)
                .background(
                    color = Color(0xFFDEE1E7)
                )
        ) {
            Row() {
               /* Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "Search", Modifier.size(50.dp)
                )*/
                Text(
                    item,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.CenterVertically)
                )

            }



        }



    }


}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideOutMenu() {
    var menuVisible by remember { mutableStateOf(false) }

    val drawerContentModifier = Modifier.size(280.dp)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Slide Out Menu Example") },
                navigationIcon = {

                },

            )
        },
        content = { innerPadding ->
Column(modifier = Modifier.padding(innerPadding)) {

}

        }
    )
}


*/

@Composable
fun UserEachRow(
    chatRow:ChatRow, nick: String

) {
    Card(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
        Column(modifier = Modifier
            .padding(vertical = 10.dp)
            .padding(start = 10.dp)
            .clickable {
                //    navController.navigate("chat_screen/$userNick/${mesageRow.id}")
            }
        ) {

            Row() {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .border(BorderStroke(0.dp, Color.Transparent), CircleShape),
                    painter = painterResource(id = R.drawable.eye),contentDescription = "Person"

                )

                Spacer(modifier = Modifier.height(10.dp))
                Column() {

                    Text(
                        text = chatRow.otherUser.toString(), modifier = Modifier
                            .padding(vertical = 1.dp)
                            .padding(start = 15.dp)
                            , fontSize = 23.sp

                    )
                    Text(
                        text = chatRow.lastMessage, modifier = Modifier
                            .padding(vertical = 2.dp)
                            .padding(start = 15.dp), fontSize = 20.sp, maxLines = 1, color = Color.Gray
                    )
                }
                Text(text =chatRow.date, modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp), textAlign = TextAlign.End, fontSize = 20.sp)

            }



        }
    }









}
@Composable
fun SearchItem(item: SimpleUser, navController: NavController, userNick: String, onClick:(String)->Unit) {
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
                    Text(text = chatRow.date
                        , fontSize = 10.sp)
                }


                Spacer(modifier = Modifier.padding(5.dp))
                Text(text =
                if (chatRow.otherUser.nick==nick){
                    "$you: ${chatRow.lastMessage}"
                }else{
                    chatRow.lastMessage
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

    MainScreenGenerate(nick = "ggci", navController = NavController(LocalContext.current), loginNavController = NavController(LocalContext.current), viewModel = MainScreenViewModel() )
}