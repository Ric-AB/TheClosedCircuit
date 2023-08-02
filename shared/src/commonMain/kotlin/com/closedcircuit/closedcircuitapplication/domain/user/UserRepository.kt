package com.closedcircuit.closedcircuitapplication.domain.user

interface UserRepository {

    suspend fun loginWithEmailAndPassword(email: String, password: String)
}