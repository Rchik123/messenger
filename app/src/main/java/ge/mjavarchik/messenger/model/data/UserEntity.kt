package ge.mjavarchik.messenger.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserEntity (
    val username: String,
    val nickname: String,
    val profession: String,
    val avatar: String? = null,
    val hashedPassword: String
)