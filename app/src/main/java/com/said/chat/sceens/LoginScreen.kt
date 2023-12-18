package com.said.chat.sceens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.said.chat.R

@Preview
@Composable
fun loginprw(){
    LoginScreen(navController = rememberNavController())
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LoginScreen(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val nickname= remember { mutableStateOf("") }
    val password=remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier=Modifier
                .padding(12.dp),
            shape= RoundedCornerShape(16.dp),
            //shape Card viyuv ni vazifasini bajarmoqa
            value = name.value,
            onValueChange = {name.value = it},
            colors= TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
              containerColor = Color.
            ),
            label = {Text("name")})
        TextField(modifier=Modifier.padding(12.dp),value = nickname.value, onValueChange = {nickname.value=it}, label ={ Text("nickname")})
        TextField(modifier=Modifier.padding(12.dp),value = password.value, onValueChange = {password.value=it}, label = {Text("password")})
    }
}



