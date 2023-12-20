package com.said.chat.sceens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.said.chat.R
import com.said.chat.api.Firebase
import com.said.chat.navigation.Screens
import com.said.chat.ui.theme.Background
import com.said.chat.ui.theme.BlueLight

@Preview
@Composable
fun Registprev (){
    RegisterScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController){
    val context = LocalContext.current
    val firstName = remember { mutableStateOf("") }
    var pressed = false
    val userName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val retypePassword = remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
  ){
        Image(painter = painterResource(id = R.drawable.img), contentDescription = "", Modifier.padding(16.dp)
            )
        TextField(
            modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            //shape Card viyuv ni vazifasini bajarmoqa
            value = firstName.value,
            onValueChange = { firstName.value = it.trim() },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            label = { Text("Firstname") },
            leadingIcon ={
                Icon(
             imageVector = Icons.Rounded.Person, contentDescription = "", tint = Color.Black
                )
            }
        )

        TextField(modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = lastName.value,

            onValueChange = { lastName.value = it.trim() },
            label = { Text("LastName")},
            leadingIcon ={
                Icon(
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Color.Black
                )
            })



        TextField(modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = userName.value,

            onValueChange = { userName.value = it.trim() },
            label = { Text("Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "",
                    tint = Color.Black
                )
            },)

        TextField(modifier = Modifier.padding(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = password.value,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { password.value = it.trim() },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock, contentDescription = "", tint = Color.Black
                )
            },
            )
        TextField(modifier = Modifier.padding(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = retypePassword.value,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { retypePassword.value = it.trim() },
            label = { Text("Retype password") },
            leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Lock, contentDescription = "", tint = Color.Black
            )
        },)
        Button(
            onClick = {
                if (pressed) return@Button
                if (firstName.value.isNotEmpty() && userName.value.isNotEmpty() && password.value.isNotEmpty() && password.value == retypePassword.value){
                    pressed = true
                    Firebase.usernameAvailable(userName.value){ available->
                        if(!available){
                            Toast.makeText(context, "Username olingan", Toast.LENGTH_SHORT).show()
                            pressed = false
                        }else{
                            val user = com.said.chat.model.User(userName.value.lowercase(), password.value, firstName.value, lastName.value, "")
                            Firebase.register(user, context){
                                if (it){
                                    navController.navigate(Screens.Home.route)
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(context, "Ma'lumotlarni to'g'ri kiriting", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight)

        ) {
            Text(
                text = "Sign up",
                modifier = Modifier .padding(6.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {
                navController.navigate(Screens.Login.route)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)

        ) {
            Text(
                text = "Log in",
                modifier = Modifier .padding(6.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
        }

    }

}