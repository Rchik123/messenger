package ge.mjavarchik.messenger.model.repository


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.nfc.Tag
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import ge.mjavarchik.messenger.model.data.UserEntity
import kotlinx.coroutines.tasks.await
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class FirebaseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(STORAGE_URL)

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

    suspend fun updateUser(
        username: String,
        newNickname: String,
        newProfession: String,
        newAvatar: Bitmap?
    ) {
        return withContext(Dispatchers.IO) {
            val user = getUserByUsername(username)
            if (user != null) {

                val userReference = database.reference.child("users").child(user.username)
                userReference.child("nickname").setValue(newNickname)
                userReference.child("profession").setValue(newProfession)
                val os = ByteArrayOutputStream()
                if(newAvatar != null) {

                    newAvatar.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    val bytearray: ByteArray = os.toByteArray()
                    val storageRef = storage.reference
                    storageRef.child(user.username).child("image.jpeg").putBytes(bytearray)
                        .addOnSuccessListener { taskSnap ->
                            taskSnap.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                userReference.child("avatar").setValue(downloadUri.toString())
                            }
                        }.addOnFailureListener {
                            Log.d("ERROR", "Image uri not saved")
                        }
                }
            }
        }
    }

    companion object {
        private const val DATABASE_URL =
            "https://messenger-53d40-default-rtdb.europe-west1.firebasedatabase.app"
        private const val STORAGE_URL = "gs://messenger-53d40.appspot.com"
    }
}
