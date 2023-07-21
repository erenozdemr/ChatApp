package com.ex.chatapp.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ex.chatapp.R
import java.util.Locale

@Composable
fun LoginScreen(navController: NavController) {

   LoginScreenGenerate(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenGenerate(navController:NavController) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailEmpty by remember { mutableStateOf(false) }
    var passwordEmpty by remember { mutableStateOf(false) }

Box(modifier = Modifier){
    Box(
        modifier = Modifier
            .fillMaxSize()

            .alpha(0.9f)
            .background(LinearGradient())
            .clip(
                CutCornerShape(
                    topStart = 8.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp, bottomEnd = 8.dp
                )
            )

    ) {




    }
    Box(modifier = Modifier, contentAlignment = Alignment.Center){
        Card(modifier = Modifier.padding(top = 150.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF44336))) {
            Column(
                modifier = Modifier


                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = "Giriş",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()
                )



                OutlinedTextField(value = email,
                    onValueChange = {
                        if (it.isEmpty()) {
                            email = it
                        } else if (!it[it.length - 1].isWhitespace()) {
                            email = it.lowercase(Locale.ENGLISH)
                        }
                    },
                    label = { Text(text = "E-posta") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.LightGray

                    ),

                    supportingText = {
                        if (emailEmpty) {
                            Text(
                                text = "Doldurulması zorunlu alan",
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            if (it.isEmpty()) {
                                password = it
                            } else if (!it[it.length - 1].isWhitespace()) {
                                password = it
                            }
                        },  colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.LightGray),
                        label = { Text(text = "Şifre") },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(), maxLines = 1,
                        isError = passwordEmpty,
                        supportingText = {
                            if (passwordEmpty) {
                                Text(
                                    text = "Doldurulması zorunlu alan",
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                                )
                            }
                        }
                    )
                    IconButton(
                        onClick = {
                            passwordVisibility = !passwordVisibility
                        },

                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp, top = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (passwordVisibility) R.drawable.show else R.drawable.eye),
                            contentDescription = if (passwordVisibility) "Şifreyi Gizle" else "Şifreyi Göster",
                            tint = Color.Gray, modifier = Modifier.size(60.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                    Color(0xFF2C519B)
                    )
                ,onClick = {
                    emailEmpty=false
                    passwordEmpty=false

                    if(email.isNullOrEmpty()){
                        emailEmpty=true
                    }
                    if(password.isNullOrEmpty()){
                        passwordEmpty=true
                    }
                    if(!emailEmpty&&!passwordEmpty){
                       // viewModel.Login(email, password = password, navController = navController)
                    }
                }) {
                    Text(text = "Giriş")
                }
                Spacer(modifier = Modifier.height(150.dp))
                TextButton(
                    onClick = {

                        navController.navigate("register_screen")

                    },
                    shape = RoundedCornerShape(8.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 50.dp, end = 50.dp)
                ) {
                    Row() {
                        Text(text = "Hesabın yok mu?",fontSize = 17.sp, color = Color.DarkGray)
                        Text(text = " kayıt Ol", color = Color.Black, fontSize = 17.sp)

                    }
                }



            }
        }
    }

}

}


@Composable
fun LinearGradient() :Brush{
    val gradient = Brush.verticalGradient(
        0.05f to Color(0xFFDDD8D8),
        0.2f to Color(0xFF211D5C),
        0.99f to Color(0xFF43C6AC),
        startY = 0.0f,
        endY = 1000.0f
    )
    return gradient
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreenGenerate(navController = NavController(LocalContext.current))
}
