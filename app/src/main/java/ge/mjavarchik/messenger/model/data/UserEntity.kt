package ge.mjavarchik.messenger.model.data

data class UserEntity (
    val username: String,
    val nickname: String,
    val profession: String,
    val hashedPassword: String
)