package com.rpfcoding.data.message

import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoMessageDataSource(
    private val db: CoroutineDatabase
) : MessageDataSource {

    private val messages = db.getCollection<Message>(collectionName = "ChatCollection")

    override suspend fun getAllMessages(): List<Message> {
        return messages
            .find()
            .descendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}