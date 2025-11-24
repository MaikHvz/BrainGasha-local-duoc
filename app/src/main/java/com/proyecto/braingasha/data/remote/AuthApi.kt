package com.proyecto.braingasha.data.remote

import com.proyecto.braingasha.data.remote.dto.LoginRequest
import com.proyecto.braingasha.data.remote.dto.RegisterRequest
import com.proyecto.braingasha.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>

    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<UserResponse>
}