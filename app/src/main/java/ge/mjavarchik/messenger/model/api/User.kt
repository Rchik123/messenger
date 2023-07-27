package ge.mjavarchik.messenger.model.api

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    val username: String,
    val nickname: String,
    val profession: String,
    val avatar: String? = null
)