package com.ibaiqzzaaziza.rutex.model

/**
 * Bentuk umum respons API RuteX:
 * { "status": "success" | "error", "message": ..., "data": ... }
 *
 * [message] bisa berupa teks biasa ("Login successful") atau objek berisi
 * error per field ({"email": ["This field is required."]}), jadi tipenya Any.
 */
data class ApiResponse<T>(
    val status: String,
    val message: Any? = null,
    val data: T? = null
)

data class TokenData(
    val refresh: String,
    val access: String
)

// Field lain dari server (profile_picture, date_joined, dll.) belum dipakai, jadi diabaikan
data class ProfilData(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null
)

/** Data akun yang disimpan di perangkat setelah pengguna berhasil masuk. */
data class User(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)
