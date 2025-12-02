package com.example.frontend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusTargetModifierNode
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(onLoginSuccess: () -> Unit){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    val scope = rememberCoroutineScope();

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Welcome!",style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username")})
        Spacer(modifier = Modifier.height(16.dp ))
        OutlinedTextField(value= password, onValueChange = {password = it}, label = {Text("Password")})
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            scope.launch {
                try {
                    val request = LoginRequest(username, password)
                    val response = RetrofitClient.userService.login(request)

                    if (response.isSuccessful) {
                        val tokenPossible = response.body()?.token
                        println("🎉 SUCCES! Token: $tokenPossible")
                        TokenManager.token = tokenPossible;
                        onLoginSuccess()
                    } else {
                        println("❌ Eroare server: ${response.code()}")
                    }
                } catch (e: Exception) {
                    println("💀 Eroare rețea: ${e.message}")
                }
            }
        }, modifier = Modifier.fillMaxWidth()
        ){Text("Login")}

    }

}