package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import io.github.aakira.napier.Napier

class UserRepositoryImpl(
    private val userService: KtorfitUserService
) : UserRepository {

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        val req = LoginRequest("richardbajomo@yahoo.com", "Password")
        val res = userService.loginWithEmailAndPassword(req)
        Napier.d("FALLLGUYYYY:::$res")
    }
}