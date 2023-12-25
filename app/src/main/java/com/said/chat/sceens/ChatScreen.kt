package com.said.chat.sceens

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.said.chat.R
import com.said.chat.api.Firebase
import com.said.chat.api.SharedHelper
import com.said.chat.model.Message
import com.said.chat.model.User
import com.said.chat.ui.theme.Background
import com.said.chat.ui.theme.BlueLight
import com.said.chat.ui.theme.Gray

@Composable
fun ChatScreen(navController: NavController, key:String){
    val user = remember { mutableStateOf(User("", "", "", "", "", "")) }
    val messages = remember { mutableStateListOf<Message>() }
    val context = LocalContext.current

    Firebase.getUser(key){
        user.value = it

    }
    Firebase.getMessages(context, key){
        messages.clear()
        messages.addAll(it)
    }



    Column(modifier = Modifier.background(Background)){
        ChatTopBar(user)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally

        ) {
            items(messages.size) { index ->
                MessageItem(messages[index], index)
            }
        }

        if (user.value.firstName!!.isNotEmpty()) EnterMessage(userKey = user.value.key!!)

    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    message: Message,
//    deleteDialogOpen: MutableState<Boolean>,
//    deleteIndex: MutableState<Int>,
    index: Int
) {
    val currentUserKey = SharedHelper.getInstance(LocalContext.current).getKey()
    val fromMe = message.from == currentUserKey
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (fromMe) Spacer(modifier = Modifier.width(100.dp))
        Card(
            modifier = Modifier
                .padding(12.dp)
                .combinedClickable(
                    onClick = {

                    },
//                    onLongClick = {
//                        deleteDialogOpen.value = true
//                        deleteIndex.value = index
//                    }
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(if (fromMe) BlueLight else Gray)
        ) {
            Text(
                text = message.text!!,
                color = Color.White,
                modifier = Modifier.padding(12.dp),
                textAlign = if (fromMe) TextAlign.End else TextAlign.Start
            )
        }
        if (!fromMe) Spacer(modifier = Modifier.width(100.dp))
    }
}

@Composable
fun ChatTopBar(user: MutableState<User>) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Row (modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.Center){
        IconButton(onClick = { backDispatcher?.onBackPressed() }) {
            Icon(Icons.Rounded.ArrowBack, "", Modifier.size(40.dp))
        }
        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            Text(text = user.value.firstName + " " + user.value.lastName, textAlign = TextAlign.Center, color = Color.Black)
            Text(text = user.value.username!!, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterMessage(
    userKey: String
) {
    val context = LocalContext.current
    val message = remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = message.value,
        onValueChange = { message.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            Color.Black,
            containerColor = Color.White,
            focusedBorderColor = BlueLight,
            unfocusedBorderColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    Firebase.writeMessage(message.value.trim(), context, userKey)
                    message.value = ""
                    focusManager.clearFocus()
                },
                enabled = message.value.isNotEmpty(),
            ) {
                Icon(
                    Icons.Rounded.Send,
                    contentDescription = "",
                    tint = if (message.value.isNotEmpty()) BlueLight else Color.Gray
                )
            }
        },
        maxLines = 3,
        placeholder = {
            Text(
                text = "Write a message", color = Color.Gray
            )
        }

    )
}