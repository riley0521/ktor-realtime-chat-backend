package com.rpfcoding.di

import com.rpfcoding.data.message.MessageDataSource
import com.rpfcoding.data.message.MongoMessageDataSource
import com.rpfcoding.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("ktor-realtime-chat-db")
    }
    single<MessageDataSource> { MongoMessageDataSource(get()) }
    single { RoomController(get()) }
}