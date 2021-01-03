package com.paulzixuanhugo.todo.network

import com.paulzixuanhugo.todo.authentication.LoginForm
import com.paulzixuanhugo.todo.authentication.LoginResponse
import com.paulzixuanhugo.todo.authentication.SignUpForm
import com.paulzixuanhugo.todo.authentication.SignUpResponse
import com.paulzixuanhugo.todo.userinfo.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signup(@Body user: SignUpForm): Response<SignUpResponse>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>
}