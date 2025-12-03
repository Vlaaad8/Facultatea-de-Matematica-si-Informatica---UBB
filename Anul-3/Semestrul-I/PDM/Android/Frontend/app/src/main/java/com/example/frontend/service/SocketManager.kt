package com.example.frontend.service

import com.example.frontend.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.URLEncoder

object SocketManager {

    private const val WS_URL = "ws://10.0.2.2:8081/ws"

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null


    private val _events = MutableSharedFlow<String>(replay = 1)
    val events = _events.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun connect() {
        if (webSocket != null) return
        val token = TokenManager.token ?: return

        val encodedToken = URLEncoder.encode(token, "UTF-8")


        val fullUrl = "$WS_URL?token=$encodedToken"

        println("Incerc conectare la: $fullUrl")

        val request = Request.Builder()
            .url(fullUrl)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket Conectat!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Mesaj primit: $text")
                scope.launch {
                    _events.emit(text)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Eroare WebSocket: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket Inchis: $reason")
            }
        })
    }

    fun close() {
        webSocket?.close(1000, "User logout")
        webSocket = null
    }
}