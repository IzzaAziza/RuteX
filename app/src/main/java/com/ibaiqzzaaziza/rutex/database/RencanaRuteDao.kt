package com.ibaiqzzaaziza.rutex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import kotlinx.coroutines.flow.Flow

@Dao
interface RencanaRuteDao {

    @Insert
    suspend fun insert(rencana: RencanaRute): Long

    @Query("SELECT * FROM rencana_rute WHERE emailPengguna = :email ORDER BY id DESC")
    fun getRencana(email: String): Flow<List<RencanaRute>>

    @Query("SELECT * FROM rencana_rute WHERE id = :id")
    suspend fun getRencanaById(id: Long): RencanaRute?

    @Query("DELETE FROM rencana_rute WHERE id = :id")
    suspend fun deleteById(id: Long)
}
