package ge.mjavarchik.messenger.model.data

data class UserEntity (
    val username: String,
    val nickname: String,
    val profession: String,
    val avatar: String? = null,
    val hashedPassword: String
)