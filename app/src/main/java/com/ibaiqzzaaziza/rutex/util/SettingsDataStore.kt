package com.ibaiqzzaaziza.rutex.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ibaiqzzaaziza.rutex.model.DurasiPerjalanan
import com.ibaiqzzaaziza.rutex.model.GayaPerjalanan
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.PreferensiPerjalanan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Dipisah dari data akun (UserDataStore) agar preferensi tidak ikut terhapus saat logout
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings_preference"
)

class SettingsDataStore(private val context: Context) {

    companion object {
        private val JENIS_WISATA = stringSetPreferencesKey("jenis_wisata")
        private val DURASI = stringPreferencesKey("durasi_perjalanan")
        private val GAYA = stringPreferencesKey("gaya_perjalanan")
        private val NOTIFIKASI = booleanPreferencesKey("notifikasi")
    }

    val notifikasiFlow: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[NOTIFIKASI] ?: true
    }

    suspend fun saveNotifikasi(aktif: Boolean) {
        context.settingsDataStore.edit { it[NOTIFIKASI] = aktif }
    }

    private val default = PreferensiPerjalanan()

    val preferensiFlow: Flow<PreferensiPerjalanan> = context.settingsDataStore.data.map { preferences ->
        PreferensiPerjalanan(
            jenisWisata = preferences[JENIS_WISATA]
                ?.mapNotNull { nama -> KategoriDestinasi.entries.find { it.name == nama } }
                ?.toSet()
                ?: default.jenisWisata,
            durasi = DurasiPerjalanan.entries.find { it.name == preferences[DURASI] } ?: default.durasi,
            gaya = GayaPerjalanan.entries.find { it.name == preferences[GAYA] } ?: default.gaya
        )
    }

    suspend fun saveJenisWisata(jenis: Set<KategoriDestinasi>) {
        context.settingsDataStore.edit { it[JENIS_WISATA] = jenis.map { k -> k.name }.toSet() }
    }

    suspend fun saveDurasi(durasi: DurasiPerjalanan) {
        context.settingsDataStore.edit { it[DURASI] = durasi.name }
    }

    suspend fun saveGaya(gaya: GayaPerjalanan) {
        context.settingsDataStore.edit { it[GAYA] = gaya.name }
    }
}
