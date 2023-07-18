package ge.mjavarchik.messenger.model.repository

import com.google.firebase.database.FirebaseDatabase
import ge.mjavarchik.messenger.model.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)

    suspend fun addUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            if (getUserByUsername(userEntity.nickname) != null) return@withContext false
            val userReference = database.reference.child("users").child(userEntity.nickname)
            userReference.setValue(
                mapOf(
                    "nickname" to userEntity.nickname,
                    "profession" to userEntity.profession,
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

                UserEntity(username, nickname, profession, hashedPassword)
            } else {
                null
            }
        }
    }

    companion object {
        private const val DATABASE_URL =
            "https://messenger-53d40-default-rtdb.europe-west1.firebasedatabase.app"
    }
}