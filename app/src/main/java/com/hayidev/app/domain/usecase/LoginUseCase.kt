package com.hayidev.app.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hayidev.app.data.model.User
import com.hayidev.app.data.service.AnalyticsService
import com.hayidev.app.data.service.FirebaseService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val firebaseService: FirebaseService,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val analyticsService: AnalyticsService
) {
    data class LoginResult(
        val success: Boolean,
        val user: User? = null,
        val isNewUser: Boolean = false,
        val error: String? = null
    )

    suspend fun loginWithEmail(email: String, password: String): LoginResult {
        return try {
            val firebaseUser = firebaseService.signInWithEmail(email, password)
            val user = getUserOrCreate(firebaseUser)
            analyticsService.logLogin("email")
            analyticsService.setUserId(firebaseUser.uid)
            LoginResult(success = true, user = user)
        } catch (e: Exception) {
            LoginResult(success = false, error = e.message)
        }
    }

    suspend fun loginWithGoogle(idToken: String): LoginResult {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Google sign in failed")
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false
            val user = getUserOrCreate(firebaseUser)
            analyticsService.logLogin("google")
            analyticsService.setUserId(firebaseUser.uid)
            LoginResult(success = true, user = user, isNewUser = isNewUser)
        } catch (e: Exception) {
            LoginResult(success = false, error = e.message)
        }
    }

    suspend fun loginWithFacebook(accessToken: String): LoginResult {
        return try {
            val credential = com.google.firebase.auth.FacebookAuthProvider.getCredential(accessToken)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Facebook sign in failed")
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false
            val user = getUserOrCreate(firebaseUser)
            analyticsService.logLogin("facebook")
            analyticsService.setUserId(firebaseUser.uid)
            LoginResult(success = true, user = user, isNewUser = isNewUser)
        } catch (e: Exception) {
            LoginResult(success = false, error = e.message)
        }
    }

    suspend fun register(email: String, password: String, username: String): LoginResult {
        return try {
            val firebaseUser = firebaseService.signUpWithEmail(email, password)
            val user = User(
                uid = firebaseUser.uid,
                username = username,
                email = email,
                displayName = username
            )
            firestore.collection("users").document(firebaseUser.uid).set(user).await()
            analyticsService.logSignUp("email")
            analyticsService.setUserId(firebaseUser.uid)
            LoginResult(success = true, user = user, isNewUser = true)
        } catch (e: Exception) {
            LoginResult(success = false, error = e.message)
        }
    }

    private suspend fun getUserOrCreate(firebaseUser: FirebaseUser): User {
        val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
        return if (doc.exists()) {
            doc.toObject(User::class.java) ?: User(uid = firebaseUser.uid)
        } else {
            val newUser = User(
                uid = firebaseUser.uid,
                username = firebaseUser.displayName ?: "",
                displayName = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
            firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
            newUser
        }
    }

    fun logout() {
        firebaseService.signOut()
        analyticsService.setUserId(null)
    }

    fun isLoggedIn(): Boolean = firebaseService.isSignedIn

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
