package ru.mail.park

import javafx.application.Application.launch
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Server(val port: Int,
             val root: String) {
    fun start() {
        val socket = ServerSocket(port)
        println("Server started on port $port")
        while (true) {
            val accept = socket.accept()
            launch(CommonPool) {
                Client(accept, root).process()
            }
        }
    }
}