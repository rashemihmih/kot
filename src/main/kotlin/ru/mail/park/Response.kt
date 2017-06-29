package ru.mail.park

import java.io.*
import java.text.SimpleDateFormat
import java.util.*

val dateFormat by lazy {
    val format = SimpleDateFormat("EEE, dd MMM, yyyy HH:mm:ss z", Locale.ENGLISH)
    format.timeZone = TimeZone.getTimeZone("GMT");
    format
}

class Response(val stream: OutputStream,
               val status: Status) {
    fun send() {
        stream.write(commonResponse().toString().toByteArray())
    }

    fun send(file: File, includeContent: Boolean) {
        val response = commonResponse()
        response.appendln("Content-Length: ${file.length()}")
        getContentType(file.extension)?.let { response.appendln("Content-Type: $it") }
        response.appendln()
        stream.write(response.toString().toByteArray())
        if (includeContent) {
            file.inputStream().use { it.copyTo(stream) }
        }
    }

    private fun commonResponse(): StringBuilder {
        val sb = StringBuilder()
        sb.appendln("HTTP/1.1 ${status.code} ${status.value}")
                .appendln("Date: ${dateFormat.format(Date())}")
                .appendln("Server: kot")
                .appendln("Connection: Close")
        return sb
    }

    private fun getContentType(extension: String): String? = when (extension) {
        "txt" -> "text/plain"
        "html" -> "text/html"
        "css" -> "text/css"
        "js" -> "text/javascript"
        "jpg" -> "image/jpeg"
        "jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "png" -> "image/png"
        "swf" -> "application/x-shockwave-flash"
        else -> null
    }
}