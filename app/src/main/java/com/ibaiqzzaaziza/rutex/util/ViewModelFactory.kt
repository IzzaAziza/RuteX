package com.ibaiqzzaaziza.rutex.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibaiqzzaaziza.rutex.database.RuteXDb
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = RuteXDb.getInstance(context).dao
        if (modelClass.isAssignableFrom(RuteManualViewModel::class.java)) {
            return RuteManualViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(RuteAiViewModel::class.java)) {
            return RuteAiViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
