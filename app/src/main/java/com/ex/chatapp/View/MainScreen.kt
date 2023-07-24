package com.ex.chatapp.View

import android.widget.ImageButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ex.chatapp.R
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController) {
MainScreenGenerate()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenGenerate() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

   ModalNavigationDrawer(
       drawerContent = {
            ModalDrawerSheet() {
                Text("Hello")
            }
       },
       drawerState = drawerState
   ) {
       Column(){
           Row(modifier=Modifier.fillMaxWidth()) {
               SearchItem("search...")
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

           UserEachRow()
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
                        text = "Cem", modifier = Modifier
                            .padding(vertical = 1.dp)
                            .padding(start = 15.dp)
                            , fontSize = 23.sp

                    )
                    Text(
                        text = "son mes", modifier = Modifier
                            .padding(vertical = 2.dp)
                            .padding(start = 15.dp), fontSize = 20.sp, maxLines = 1, color = Color.Gray
                    )
                }
                Text(text = "salı", modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp), textAlign = TextAlign.End, fontSize = 20.sp)

            }



        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview() {
 MainScreenGenerate()
}