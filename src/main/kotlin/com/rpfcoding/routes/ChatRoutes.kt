package com.rpfcoding.routes

import com.rpfcoding.room.MemberAlreadyExistsException
import com.rpfcoding.room.RoomController
import com.rpfcoding.session.ChatSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocketRoute(
    roomController: RoomController
) {
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if(session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Fuck you"))
            return@webSocket
        }

        try {
            roomController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )

            incoming.consumeEach { frame ->
                if(frame is Frame.Text) {
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        } catch (e: MemberAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict, e.message.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(username = session.username)
        }
    }
}

fun Route.getAllMessages(
    roomController: RoomController
) {
    get("/messages") {
        call.respond(HttpStatusCode.OK, roomController.getAllMessages())
    }
}