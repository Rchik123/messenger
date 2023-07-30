package ge.mjavarchik.messenger.model.repository


import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import ge.mjavarchik.messenger.model.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class FirebaseRepository(
    private val context: Context
) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(STORAGE_URL)

    val usersLiveData = MutableLiveData<List<UserEntity>>()

    init {
        listenToAllUsers()
    }

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
                if (newAvatar != null) {

                    newAvatar.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    val bytearray: ByteArray = os.toByteArray()
                    val storageRef = storage.reference
                    storageRef.child(user.username).child("image.jpeg").putBytes(bytearray)
                        .addOnSuccessListener { taskSnap ->
                            taskSnap.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                userReference.child("avatar").setValue(downloadUri.toString())
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error: image could not be saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

    private fun listenToAllUsers() {
        val reference = database.getReference("users")
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<UserEntity>()
                for (childSnapshot in dataSnapshot.children) {
                    val username = childSnapshot.key as String
                    val userMap = childSnapshot.value as Map<*, *>
                    userList.add(
                        UserEntity(
                            username,
                            userMap["nickname"] as String,
                            userMap["profession"] as String,
                            userMap["avatar"] as String,
                            userMap["hashedPassword"] as String
                        )
                    )
                }
                usersLiveData.postValue(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Error: could not retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val DATABASE_URL =
            "https://messenger-53d40-default-rtdb.europe-west1.firebasedatabase.app"
        private const val STORAGE_URL = "gs://messenger-53d40.appspot.com"
    }
}
