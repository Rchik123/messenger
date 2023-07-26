package ge.mjavarchik.messenger.model.repository


import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import ge.mjavarchik.messenger.model.data.UserEntity
import kotlinx.coroutines.tasks.await
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)

    suspend fun addUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            if (getUserByUsername(userEntity.username) != null) return@withContext false
            val userReference = database.reference.child("users").child(userEntity.username)
            userReference.setValue(
                mapOf(
                    "nickname" to userEntity.nickname,
                    "profession" to userEntity.profession,
                    "avatar" to userEntity.avatar,
                    "hashedPassword" to userEntity.hashedPassword
                )
            )
            true
        }
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            val userReference = database.reference.child("users").child(username)
            val userData = userReference.get().await()

            if (userData.exists()) {
                val userInformation = userData.value as Map<*, *>

                val nickname = userInformation["nickname"] as String
                val profession = userInformation["profession"] as String
                val hashedPassword = userInformation["hashedPassword"] as String
                val avatar = userInformation["avatar"] as String
                UserEntity(username, nickname, profession, avatar, hashedPassword)
            } else {
                null
            }
        }
    }

    suspend fun updateUser(username: String, newNickname: String, newProfession: String, newAvatar: String) {
        return withContext(Dispatchers.IO) {
            val user = getUserByUsername(username)
            user?.let {
                val userReference = database.reference.child("users").child(it.username)
                userReference.setValue(
                    mapOf(
                        "nickname" to newNickname,
                        "profession" to newProfession,
                        "avatar" to newAvatar,
                        "hashedPassword" to it.hashedPassword
                    )
                )
            }
        }
    }

    companion object {
        private const val DATABASE_URL =
            "https://messenger-53d40-default-rtdb.europe-west1.firebasedatabase.app"
    }
}