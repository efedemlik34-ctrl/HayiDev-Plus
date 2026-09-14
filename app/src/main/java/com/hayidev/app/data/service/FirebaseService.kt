package com.hayidev.app.data.service

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    val currentUser: FirebaseUser? get() = auth.currentUser
    val isSignedIn: Boolean get() = auth.currentUser != null

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Sign in failed")
    }

    suspend fun signUpWithEmail(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Sign up failed")
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateProfile(displayName: String? = null, photoUri: Uri? = null) {
        val user = auth.currentUser ?: throw Exception("Not signed in")
        displayName?.let { user.updateDisplayName(it).await() }
        photoUri?.let { user.updatePhotoUri(it).await() }
    }

    suspend fun <T> setDocument(
        collection: String,
        documentId: String,
        data: T,
        merge: Boolean = true
    ) {
        firestore.collection(collection)
            .document(documentId)
            .set(data as Any, if (merge) SetOptions.merge() else SetOptions.overwrite())
            .await()
    }

    suspend fun <T> getDocument(collection: String, documentId: String): T? {
        val doc = firestore.collection(collection).document(documentId).get().await()
        @Suppress("UNCHECKED_CAST")
        return doc.data as? T
    }

    suspend fun updateDocument(collection: String, documentId: String, updates: Map<String, Any>) {
        firestore.collection(collection)
            .document(documentId)
            .update(updates)
            .await()
    }

    suspend fun deleteDocument(collection: String, documentId: String) {
        firestore.collection(collection).document(documentId).delete().await()
    }

    suspend fun uploadFile(
        path: String,
        bytes: ByteArray,
        onProgress: ((Int) -> Unit)? = null
    ): Uri {
        val ref = storage.reference.child(path)
        ref.putBytes(bytes).await()
        return ref.downloadUrl.await()
    }

    suspend fun uploadFileFromUri(path: String, uri: Uri): Uri {
        val ref = storage.reference.child(path)
        ref.putFile(uri).await()
        return ref.downloadUrl.await()
    }

    suspend fun deleteFile(path: String) {
        storage.reference.child(path).delete().await()
    }

    suspend fun getFcmToken(): String {
        return com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
            ?: throw Exception("Failed to get FCM token")
    }

    suspend fun saveFcmToken(uid: String) {
        val token = getFcmToken()
        firestore.collection("users").document(uid)
            .update("fcmToken", token)
            .await()
    }
}
