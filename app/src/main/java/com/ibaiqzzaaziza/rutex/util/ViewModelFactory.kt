package com.ibaiqzzaaziza.rutex.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibaiqzzaaziza.rutex.database.RuteXDb
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.screen.AuthViewModel
import com.ibaiqzzaaziza.rutex.screen.RiwayatViewModel
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(UserDataStore(context)) as T
        }

        val dao = RuteXDb.getInstance(context).dao
        val dataStore = UserDataStore(context)
        if (modelClass.isAssignableFrom(RuteManualViewModel::class.java)) {
            return RuteManualViewModel(dao, dataStore) as T
        } else if (modelClass.isAssignableFrom(RuteAiViewModel::class.java)) {
            return RuteAiViewModel(dao, dataStore) as T
        } else if (modelClass.isAssignableFrom(RiwayatViewModel::class.java)) {
            return RiwayatViewModel(dao, dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
