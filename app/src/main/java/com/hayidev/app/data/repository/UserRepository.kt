package com.hayidev.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.hayidev.app.data.model.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val usersCollection = firestore.collection("users")
    private val matchesCollection = firestore.collection("matches")

    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return getUserById(uid)
    }

    suspend fun getUserById(uid: String): User? {
        return try {
            val doc = usersCollection.document(uid).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    suspend fun updateProfilePhoto(url: String) {
        val uid = auth.currentUser?.uid ?: return
        usersCollection.document(uid).update("photoUrl", url).await()
    }

    suspend fun updateCoverPhoto(url: String) {
        val uid = auth.currentUser?.uid ?: return
        usersCollection.document(uid).update("coverUrl", url).await()
    }

    suspend fun getDiscoverUsers(
        limit: Long = 20,
        excludeIds: List<String> = emptyList()
    ): List<User> {
        return try {
            val snapshot = usersCollection
                .whereNotIn("uid", excludeIds)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun likeUser(targetUserId: String) {
        val currentUid = auth.currentUser?.uid ?: return
        val currentUser = getCurrentUser() ?: return

        // Kullanıcıyı beğen
        usersCollection.document(targetUserId)
            .update("likedBy", com.google.firebase.firestore.FieldValue.arrayUnion(currentUid))
            .await()

        // Eşleşme kontrolü
        val targetUser = getUserById(targetUserId)
        if (targetUser != null && currentUid in targetUser.likedBy) {
            // Eşleşme oldu!
            createMatch(currentUid, targetUserId)
        }
    }

    private suspend fun createMatch(userId1: String, userId2: String) {
        val matchId = matchesCollection.document().id
        val match = mapOf(
            "matchId" to matchId,
            "users" to listOf(userId1, userId2),
            "createdAt" to com.google.firebase.Timestamp.now()
        )
        matchesCollection.document(matchId).set(match).await()

        // Her iki kullanıcının da matches listesini güncelle
        usersCollection.document(userId1)
            .update("matches", com.google.firebase.firestore.FieldValue.arrayUnion(userId2))
            .await()
        usersCollection.document(userId2)
            .update("matches", com.google.firebase.firestore.FieldValue.arrayUnion(userId1))
            .await()
    }

    suspend fun passUser(targetUserId: String) {
        // Geçilen kullanıcıyı işaretle (opsiyonel)
    }

    suspend fun blockUser(targetUserId: String) {
        val uid = auth.currentUser?.uid ?: return
        usersCollection.document(uid)
            .update("blockedUsers", com.google.firebase.firestore.FieldValue.arrayUnion(targetUserId))
            .await()
    }

    suspend fun uploadProfileImage(imageBytes: ByteArray): String {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val ref = storage.reference.child("profile_images/$uid/${System.currentTimeMillis()}")
        ref.putBytes(imageBytes).await()
        return ref.downloadUrl.await().toString()
    }
}
