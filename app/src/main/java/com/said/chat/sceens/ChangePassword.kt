package com.said.chat.sceens

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.said.chat.api.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen() {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val password = remember {
        mutableStateOf("")
    }
    val passwordOld = remember {
        mutableStateOf("")
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            IconButton(onClick = { backDispatcher?.onBackPressed() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "")
            }
        }
        TextField(modifier = Modifier.padding(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#ECFEFF")),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = passwordOld.value,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { passwordOld.value = it.trim() },
            label = { Text("Eski parol") })
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
            label = { Text("Yangi parol") })
        val context = LocalContext.current
        Button(
            onClick = {
                Firebase.updatePassword(password.value, passwordOld.value, context) { success ->
                    if (success) {
                        Toast.makeText(context, "Parol yangilandi", Toast.LENGTH_SHORT).show()
                        backDispatcher?.onBackPressed()
                    } else {
                        Toast.makeText(context, "Parol mos kelmadi", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Green),
            enabled = password.value.isNotEmpty() && passwordOld.value.isNotEmpty()
        ) {
            Text(text = "Saqlash", color = Color.White)
        }
    }
}