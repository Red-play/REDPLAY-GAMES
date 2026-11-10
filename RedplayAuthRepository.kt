package com.redplay.games.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Firebase-backed account and player-data repository.
 * Firestore layout:
 * users/{uid} -> profile fields
 * users/{uid}/favorites/{gameId} -> saved game
 */
class RedplayAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val currentUser get() = auth.currentUser

    suspend fun register(email: String, password: String, displayName: String) {
        require(email.isNotBlank()) { "أدخل البريد الإلكتروني" }
        require(password.length >= 6) { "كلمة المرور يجب ألا تقل عن 6 أحرف" }
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val user = result.user ?: error("تعذر إنشاء الحساب")
        val profile = mapOf(
            "uid" to user.uid,
            "email" to (user.email ?: email.trim()),
            "displayName" to displayName.trim(),
            "photoUrl" to "",
            "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
            "provider" to "email"
        )
        db.collection("users").document(user.uid).set(profile).await()
    }

    suspend fun signInEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        ensureProfile()
    }

    suspend fun signInWithGoogleIdToken(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val user = result.user ?: error("تعذر تسجيل الدخول باستخدام Google")
        val ref = db.collection("users").document(user.uid)
        if (!ref.get().await().exists()) {
            ref.set(mapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: "لاعب REDPLAY"),
                "photoUrl" to (user.photoUrl?.toString() ?: ""),
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "provider" to "google"
            )).await()
        }
    }

    suspend fun saveFavorite(gameId: String, gameName: String, category: String) {
        val uid = currentUser?.uid ?: error("سجّل الدخول أولًا")
        db.collection("users").document(uid)
            .collection("favorites").document(gameId)
            .set(mapOf(
                "gameId" to gameId,
                "gameName" to gameName,
                "category" to category,
                "savedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )).await()
    }

    suspend fun removeFavorite(gameId: String) {
        val uid = currentUser?.uid ?: error("سجّل الدخول أولًا")
        db.collection("users").document(uid).collection("favorites").document(gameId).delete().await()
    }

    suspend fun loadFavorites(): List<Map<String, Any>> {
        val uid = currentUser?.uid ?: return emptyList()
        return db.collection("users").document(uid).collection("favorites")
            .get().await().documents.mapNotNull { it.data }
    }

    suspend fun loadProfile(): Map<String, Any>? {
        val uid = currentUser?.uid ?: return null
        return db.collection("users").document(uid).get().await().data
    }

    suspend fun updateDisplayName(name: String) {
        val uid = currentUser?.uid ?: error("سجّل الدخول أولًا")
        auth.currentUser?.updateProfile(
            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(name.trim()).build()
        )?.await()
        db.collection("users").document(uid).update("displayName", name.trim()).await()
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email.trim()).await()
    }

    fun signOut() = auth.signOut()

    private suspend fun ensureProfile() {
        val user = currentUser ?: return
        val ref = db.collection("users").document(user.uid)
        if (!ref.get().await().exists()) {
            ref.set(mapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: "لاعب REDPLAY"),
                "photoUrl" to (user.photoUrl?.toString() ?: ""),
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "provider" to "email"
            )).await()
        }
    }
}
