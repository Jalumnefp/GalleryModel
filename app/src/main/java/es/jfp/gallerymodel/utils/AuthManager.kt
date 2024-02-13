package es.jfp.gallerymodel.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthManager {

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            Log.w("ERROR_LOGIN_AUTHMANAGER", e.printStackTrace().toString())
            null
        }
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            Log.w("ERROR_REGISTER_AUTHMANAGER", e.printStackTrace().toString())
            null
        }
    }

    suspend fun resetPassword(email: String) {
        try {
            auth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            Log.w("ERROR_RESET_PASSWORD_AUTHMANAGER", e.printStackTrace().toString())
        }
    }

    fun logoff() = auth.signOut()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    companion object {
        private var instance: AuthManager? = null
        fun getInstance(): AuthManager? {
            synchronized(this) {
                if (instance ==null) {
                    instance = AuthManager()
                }
                return instance
            }
        }
    }

}