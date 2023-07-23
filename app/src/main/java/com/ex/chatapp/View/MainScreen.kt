package com.ex.chatapp.View

import android.widget.Space
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@Composable
fun MainScreen(nick:String,navController: NavController,viewModel: MainScreenViewModel =remember{MainScreenViewModel()}){
MainScreenGenerate(nick = nick, navController = navController,viewModel=viewModel)
}
@Composable
fun MainScreenGenerate(nick:String,navController: NavController,viewModel: MainScreenViewModel){


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