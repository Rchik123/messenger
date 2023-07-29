package ge.mjavarchik.messenger.model.api

import java.util.Date

data class Message (
    val from: String?,
    val to: String?,
    val message: String?,
    val date: Date?
)