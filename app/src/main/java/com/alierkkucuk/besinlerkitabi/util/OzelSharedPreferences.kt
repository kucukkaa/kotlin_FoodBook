package com.alierkkucuk.besinlerkitabi.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class OzelSharedPreferences {
    companion object{
        private var sharedPreferences : SharedPreferences? = null
        private val ZAMAN = "zaman"

        @Volatile private var instance : OzelSharedPreferences? = null
        private val lock = Any()
        operator fun invoke(context: Context) : OzelSharedPreferences = instance ?: synchronized(lock){
            instance ?: ozelSharedPreferencesYap(context).also {
                instance = it
            }
        }

        private fun ozelSharedPreferencesYap(context : Context) : OzelSharedPreferences{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return OzelSharedPreferences()
        }
    }

    fun zamaniKaydet(zaman : Long){
        sharedPreferences?.edit(commit = true){
            putLong(ZAMAN, zaman)
        }
    }

    fun zamaniAl() = sharedPreferences?.getLong(ZAMAN,0)
}