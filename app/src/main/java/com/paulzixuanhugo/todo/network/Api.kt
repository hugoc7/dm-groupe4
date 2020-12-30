package com.paulzixuanhugo.todo.network

import android.content.Context
import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.paulzixuanhugo.todo.SHARED_PREF_TOKEN_KEY
import com.paulzixuanhugo.todo.authentication.LoginForm
import com.paulzixuanhugo.todo.authentication.LoginResponse
import com.paulzixuanhugo.todo.authentication.SignUpForm
import com.paulzixuanhugo.todo.authentication.SignUpResponse
import com.paulzixuanhugo.todo.task.Task
import com.paulzixuanhugo.todo.userinfo.UserInfo
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

class Api(private val context: Context) {
    companion object {
        // constantes qui serviront à faire les requêtes
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        lateinit var INSTANCE: Api
    }

    fun getToken () : String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")
    }

    // on construit une instance de parseur de JSON:
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
            jsonSerializer.asConverterFactory("application/json".toMediaType())

    // client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor { chain ->
                    // intercepteur qui ajoute le `header` d'authentification avec votre token:
                    val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${getToken()}")
                            .build()
                    chain.proceed(newRequest)
                }
                .build()
    }

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}

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

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String?): Response<String>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Response<Task>

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Body task: Task, @Path("id") id: String? = task.id): Response<Task>
}