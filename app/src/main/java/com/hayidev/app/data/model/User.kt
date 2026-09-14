package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val coverUrl: String = "",
    val bio: String = "",
    val age: Int = 0,
    val gender: String = "",
    val country: String = "",
    val city: String = "",
    val interests: List<String> = emptyList(),
    val isOnline: Boolean = false,
    val lastSeen: Timestamp? = null,
    val coins: Int = 0,
    val diamonds: Int = 0,
    val isPremium: Boolean = false,
    val isVerified: Boolean = false,
    val fcmToken: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val blockedUsers: List<String> = emptyList(),
    val likedBy: List<String> = emptyList(),
    val matches: List<String> = emptyList()
)

data class UserProfile(
    val user: User = User(),
    val photoUrls: List<String> = emptyList(),
    val videoUrl: String = "",
    val interests: List<String> = emptyList(),
    val totalLikes: Int = 0,
    val totalMatches: Int = 0
)
