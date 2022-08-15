package com.rpfcoding.plugins

import com.rpfcoding.room.RoomController
import com.rpfcoding.routes.chatSocketRoute
import com.rpfcoding.routes.getAllMessages
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing) {
        chatSocketRoute(roomController)
        getAllMessages(roomController)
    }
}
