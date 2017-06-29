package ru.mail.park

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Socket
import java.net.URLDecoder


class Client(val socket: Socket,
             val root: String) {
    fun process() {
        try {
            val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
            val split = requestLine.split(" ")
            val method = split[0]
            if ("GET" != method && "HEAD" != method) {
                Response(socket.getOutputStream(), Status.METHOD_NOT_ALLOWED).send()
                return
            }
            var url = URLDecoder.decode(split[1], "UTF-8")
            url = root + url
            url = url.substringBefore('?')
            val isIndex = url.endsWith('/')
            if (isIndex) {
                url += "index.html"
            }
            val file = File(url)
            if (file.isFile) {
                Response(socket.getOutputStream(), Status.OK).send(file, "GET" == method)
                return
            }
            if (isIndex) {
                Response(socket.getOutputStream(), Status.FORBIDDEN).send()
                return
            }
            Response(socket.getOutputStream(), Status.NOT_FOUND).send()
        } catch (e: Exception) {
        } finally {
            socket.close()
        }
    }
}
