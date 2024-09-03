package com.pachuho.earthquakemap.data.datasource

import android.content.SharedPreferences
import com.pachuho.earthquakemap.data.di.SharedPreferencesModule.Companion.NAME_DATA
import com.pachuho.earthquakemap.data.di.get
import com.pachuho.earthquakemap.data.di.set
import javax.inject.Inject
import javax.inject.Named

class UpdatePreferencesDataSource @Inject constructor(
    @Named(NAME_DATA) private val pref: SharedPreferences
) {

    var lastUpdateDateTime: Int?
        get() = pref.get<Int>(Key.LAST_UPDATE_DATE)
        set(value) {
            pref.set(Key.LAST_UPDATE_DATE, value)
        }

    private object Key {
        const val LAST_UPDATE_DATE = "lastUpdateDate"
    }
}