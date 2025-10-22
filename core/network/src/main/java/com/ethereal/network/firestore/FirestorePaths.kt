package com.ethereal.network.firestore


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestorePaths @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun userDoc(uid: String): DocumentReference = firestore.collection("users").document(uid)
}