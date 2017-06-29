package ru.mail.park


enum class Status(val code: Int, val value: String) {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed")
}