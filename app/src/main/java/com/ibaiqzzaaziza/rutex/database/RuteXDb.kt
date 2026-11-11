package com.ibaiqzzaaziza.rutex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ibaiqzzaaziza.rutex.model.RencanaRute

@Database(entities = [RencanaRute::class], version = 2, exportSchema = false)
@TypeConverters(RuteConverters::class)
abstract class RuteXDb : RoomDatabase() {

    abstract val dao: RencanaRuteDao

    companion object {

        @Volatile
        private var INSTANCE: RuteXDb? = null

        fun getInstance(context: Context): RuteXDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RuteXDb::class.java,
                        "rutex.db"
                    )
                        // Versi 2 menambah kolom pemilik rencana. Data lama (hanya data uji) dihapus.
                        .fallbackToDestructiveMigration(dropAllTables = true)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
