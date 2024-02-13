package es.jfp.gallerymodel.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import es.jfp.gallerymodel.classes.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class RetrofitListenerService {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startOnUpdateListener(
        collection: CollectionReference
    ) = flow {
        val messages = mutableListOf<Message>()
        val messagesTemp = mutableListOf<Message>()
        while (true) {
            collection.orderBy("date", Query.Direction.ASCENDING).get()
                .addOnSuccessListener {
                    it.forEach {message ->
                        val color: String = if (message.get("color") == null) {
                            "#00FFE7"
                        } else {
                            message.get("color") as String
                        }
                        val owner: String = if (message.get("owner") == null) {
                            "Default Usename"
                        } else {
                            message.get("owner") as String
                        }
                        messages.add(Message(
                            message.id,
                            owner,
                            message.get("content") as String,
                            getLocalDateTime(message.get("date") as Map<*,*>),
                            color
                        ))
                    }
                }
                .addOnFailureListener {exception ->
                    Log.w("FAIL_GETTING_MESSAGES", exception)
                }
            if (messages.isNotEmpty()) {
                if (!messagesTemp.containsAll(messages)) {
                    messagesTemp.addAll(messages)
                    emit(messages)
                }
            }
            messages.clear()
            delay(1000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocalDateTime(rawLocalDateTime: Map<*,*>): LocalDateTime = LocalDateTime.of(
        (rawLocalDateTime["year"] as Long).toInt(),
        (rawLocalDateTime["monthValue"] as Long).toInt(),
        (rawLocalDateTime["dayOfMonth"] as Long).toInt(),
        (rawLocalDateTime["hour"] as Long).toInt(),
        (rawLocalDateTime["minute"] as Long).toInt(),
        (rawLocalDateTime["second"] as Long).toInt()
    )




}