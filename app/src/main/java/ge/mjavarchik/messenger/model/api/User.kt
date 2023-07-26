package ge.mjavarchik.messenger.model.api

data class User (
    val username: String,
    val nickname: String,
    val profession: String,
    val avatar: String? = null
)