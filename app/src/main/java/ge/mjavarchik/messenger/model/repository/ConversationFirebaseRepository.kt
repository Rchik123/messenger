package ge.mjavarchik.messenger.model.repository


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ge.mjavarchik.messenger.model.data.ConversationEntity
import ge.mjavarchik.messenger.model.data.MessageEntity

class ConversationFirebaseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL)

    val conversationLiveData = MutableLiveData<ConversationEntity>()

    fun addMessageToConversation(user1: String, user2: String, newMessage: MessageEntity) {
        val reference = database.getReference("conversations")
        reference.get().addOnSuccessListener { dataSnapshot ->
            for (childSnapshot in dataSnapshot.children) {
                val conversation = childSnapshot.getValue(ConversationEntity::class.java)
                conversation?.let {
                    if ((it.user1 == user1 && it.user2 == user2) ||
                        (it.user1 == user2 && it.user2 == user1)
                    ) {
                        val messageRef = database.getReference("conversations")
                            .child(childSnapshot.key!!).child("messages").push()
                        messageRef.setValue(newMessage)
                        return@addOnSuccessListener
                    }
                }
            }
            val conversationRef = database.getReference("conversations").push()
            conversationRef.setValue(
                mapOf(
                    "user1" to user1,
                    "user2" to user2
                )
            )
            val messageRef = conversationRef.child("messages").push()
            messageRef.setValue(newMessage)

        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error getting data from database", exception)
        }
    }

    fun listenToConversation(user1: String, user2: String) {
        val reference = database.getReference("conversations")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val conversation = childSnapshot.getValue(ConversationEntity::class.java)
                    conversation?.let {
                        if ((it.user1 == user1 && it.user2 == user2) ||
                            (it.user1 == user2 && it.user2 == user1)) {
                            conversationLiveData.postValue(it)
                            return
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error listening to database" + databaseError.toException().toString())
            }
        })
    }


    companion object {
        private const val DATABASE_URL =
            "https://messenger-53d40-default-rtdb.europe-west1.firebasedatabase.app"
    }
}
