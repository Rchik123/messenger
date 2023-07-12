package ge.mjavarchik.messenger.model.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import ge.mjavarchik.messenger.model.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)

    suspend fun addUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            if (getUser(userEntity.nickname) == null) return@withContext false
            Log.d("repoo", "GAMARJOBA")
            val userReference = database.reference.child("users").child(userEntity.nickname)
            userReference.setValue(
                mapOf("profession" to userEntity.profession, "hashedPassword" to userEntity.hashedPassword)
            )
            true
        }
    }

    suspend fun getUser(nickname: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            val userReference = database.reference.child("users").child(nickname)
            val userData = userReference.get().await()

            if (userData.exists()) {
                val userInformation = userData.value as Map<*, *>

                val profession = userInformation["profession"] as String
                val hashedPassword = userInformation["hashedPassword"] as String

                UserEntity(nickname, profession, hashedPassword)
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