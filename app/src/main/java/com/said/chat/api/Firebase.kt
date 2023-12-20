package com.said.chat.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.said.chat.model.Message
import com.said.chat.model.User
import java.text.SimpleDateFormat
import java.util.Date

class Firebase private constructor() {
    companion object {

        private val users = FirebaseDatabase.getInstance().reference.child("users")

        fun register(user: User, context: Context, callback: (Boolean) -> Unit) {
            val key = users.push().key.toString()
            user.key = key
            users.child(key).setValue(user)
            SharedHelper.getInstance(context).saveKey(key)
            callback(true)
        }

        fun usernameAvailable(username: String, callback: (Boolean) -> Unit) {
            users.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.children
                    u.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.username == username.trim().lowercase()) {
                            callback(false)
                        }
                    }
                    callback(true)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: ${error.message}")
                }
            })
        }

        fun logIn(username: String, password: String, callback: (key: String?) -> Unit) {
            users.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children
                    users.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.username == username) {
                            if (user.password!! == password) callback(it.key!!)
                            else callback(null)
                            return
                        }
                    }
                    callback(null)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "$error")
                }
            })
        }

        fun writeMessage(text: String, context: Context, to: String) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            val currentUser = SharedHelper.getInstance(context).getKey()!!

            val key = Firebase.database.reference.push().key.toString()
            val message = Message(to, currentUser, text, currentDate, key)
            users.child(to).child("messages").child(key).setValue(message)
            users.child(currentUser).child("messages").child(key).setValue(message)
        }

        fun getMessages(
            context: Context,
            userKey: String,
            callback: (List<Message>) -> Unit
        ) {
            val key = SharedHelper.getInstance(context).getKey()!!
            users.child(key).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val m = snapshot.children
                        val messages = mutableListOf<Message>()
                        m.forEach {
                            val message = it.getValue(Message::class.java)!!
                            if (message.from == userKey || message.to == userKey) messages.add(
                                message
                            )
                        }
                        messages.sortByDescending { it.date }
                        messages.reverse()
                        callback(messages)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }


        fun getAllUsers(context: Context, searchKey: String, callback: (List<User>) -> Unit) {
            users.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.children
                    val users = mutableListOf<User>()
                    u.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.key != SharedHelper.getInstance(context).getKey()) {
                            if (searchKey.isEmpty()) users.add(user)
                            else if ((user.firstName + user.lastName + user.username).contains(
                                    searchKey
                                )
                            ) users.add(
                                user
                            )
                        }
                    }
                    callback(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: $error")
                }
            })
        }

        private fun getCurrentUser(context: Context, callback: (User) -> Unit) {
            getUser(SharedHelper.getInstance(context).getKey()) {
                callback(it)
            }
        }

        fun updateUser(key: String, user: User, context: Context) {
            users.child(key).child("username").setValue(user.username)
            users.child(key).child("firstName").setValue(user.firstName)
            users.child(key).child("lastName").setValue(user.lastName)
            users.child(key).child("password").setValue(user.password)
            users.child(key).child("image").setValue(user.image)
            Toast.makeText(context, "Account data updated", Toast.LENGTH_SHORT).show()
        }

        fun getUser(key: String, callback: (User) -> Unit) {
            users.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.getValue(User::class.java)
                    if (u != null) callback(u)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", error.toString())
                }
            })
        }

        fun deleteMessage(message: Message) {
            val user1 = message.from!!
            val user2 = message.to!!
            val key = message.key!!
            users.child(user1).child("messages").child(key).removeValue()
            users.child(user2).child("messages").child(key).removeValue()
        }

        fun deleteChat(key1: String, key2: String) {
            deleteAll(key1, key2)
            deleteAll(key2, key1)
        }

        private fun deleteAll(key1: String, key2: String) {
            val messages = users.child(key1).child("messages")
            messages.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.children
                    u.forEach {
                        val message = it.getValue(Message::class.java)!!
                        if ((message.from == key1 && message.to == key2) || (message.from == key2 && message.to == key1)) messages.child(
                            message.key!!
                        ).removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", error.toString())
                }

            })
        }


        fun getChats(
            searchKey: String,
            context: Context,
            callback: (contacts: List<User>) -> Unit
        ) {
            val currentUserKey = SharedHelper.getInstance(context).getKey()
            val mes = users.child(currentUserKey).child("messages")
            mes.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = mutableListOf<String>()
                    val users = mutableListOf<User>()

                    val u = snapshot.children
                    val messages = mutableListOf<Message>()
                    u.forEach {
                        val message = it.getValue(Message::class.java)!!
                        messages.add(message)
                    }
                    if (messages.isEmpty()) callback(listOf())
                    messages.sortByDescending { it.date }
                    messages.forEach { message ->
                        val userKey =
                            if (message.from == currentUserKey) message.to!! else message.from!!
                        if (!keys.contains(userKey)) {
                            keys.add(userKey)
                        }
                }
                keys.forEach { userKey ->
                    getUser(userKey) {
                        if ((it.firstName + it.lastName + it.username).contains(searchKey)) users.add(it)
                        if (keys.size == users.size) callback(users)
                    }
                }
            }

                    override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "$error")
            }

        })
    }
}
}