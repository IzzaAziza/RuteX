package com.ibaiqzzaaziza.rutex.network

import com.ibaiqzzaaziza.rutex.model.ApiResponse
import com.ibaiqzzaaziza.rutex.model.ProfilData
import com.ibaiqzzaaziza.rutex.model.TokenData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

// Semua endpoint RuteX selalu diawali /api, contoh: /auth/login -> https://api.rutex.id/api/auth/login
private const val BASE_URL = "https://api.rutex.id/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RuteXApiService {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse<TokenData>>

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout(
        @Field("token") refreshToken: String
    ): Response<ApiResponse<Any>>

    @GET("user/{id}")
    suspend fun getUser(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int
    ): Response<ApiResponse<ProfilData>>

    @Multipart
    @PUT("user/{id}")
    suspend fun updateUser(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody
    ): Response<ApiResponse<Any>>
}

object RuteXApi {
    val service: RuteXApiService by lazy {
        retrofit.create(RuteXApiService::class.java)
    }

    private val errorAdapter = moshi.adapter<ApiResponse<Any>>(
        Types.newParameterizedType(ApiResponse::class.java, Any::class.java)
    )

    /**
     * Mengambil pesan error dari respons gagal (HTTP 4xx/5xx).
     * Hasilnya null jika server tidak mengirim pesan yang bisa dibaca.
     */
    fun getErrorMessage(response: Response<*>): String? {
        val body = response.errorBody()?.string() ?: return null
        val error = try {
            errorAdapter.fromJson(body)
        } catch (e: Exception) {
            null
        }
        return error?.message?.let { formatMessage(it) }
    }

    // Mengubah {"email": ["Pesan 1"], "phone": ["Pesan 2"]} menjadi "Pesan 1\nPesan 2"
    fun formatMessage(message: Any): String? {
        return when (message) {
            is String -> message
            is Map<*, *> -> message.values
                .flatMap { if (it is List<*>) it else listOf(it) }
                .filterNotNull()
                .joinToString(separator = "\n")
                .ifBlank { null }
            is List<*> -> message.filterNotNull().joinToString(separator = "\n").ifBlank { null }
            else -> null
        }
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
