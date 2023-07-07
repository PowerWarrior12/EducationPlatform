package com.example.educationplatform.data.remote.chat

import com.example.educationplatform.data.remote.chat.entities.MessagesResponse
import com.example.educationplatform.educationAppApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.IOException
import javax.inject.Inject

class MessagesService @Inject constructor() {
    private val httpClient = OkHttpClient()

    private val moshi = Moshi.Builder().build()
    private val messagesResponseAdapter: JsonAdapter<MessagesResponse> =
        moshi.adapter(MessagesResponse::class.java)

    @Throws(exceptionClasses = [IOException::class])
    fun getChatMessages(chatId: Int, onMessagesUpdate: (MessagesResponse) -> Unit) {
        val request = Request.Builder().url(educationAppApi).build()
        val webSocket = httpClient.newWebSocket(request, object: WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val messagesResponse = messagesResponseAdapter.fromJson(text)
                messagesResponse?.let {
                    onMessagesUpdate(it)
                }
            }
        })
    }
}