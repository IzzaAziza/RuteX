package com.ibaiqzzaaziza.rutex.util

import android.util.Base64
import org.json.JSONObject

object JwtUtil {

    /**
     * Token JWT terdiri dari 3 bagian: header.payload.signature.
     * Bagian payload berisi data pengguna, termasuk "user_id" yang dibutuhkan endpoint /user/{id}.
     */
    fun getUserId(token: String): Int? {
        return try {
            val payload = token.split(".")[1]
            val json = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
            JSONObject(json).optInt("user_id").takeIf { it > 0 }
        } catch (e: Exception) {
            null
        }
    }
}
