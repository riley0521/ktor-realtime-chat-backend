package com.rpfcoding.data.message

import com.rpfcoding.data.message.Message

interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)
}