package com.pachuho.earthquakemap.data.datasource

import android.content.SharedPreferences
import com.pachuho.earthquakemap.data.di.SharedPreferencesModule.Companion.NAME_DATA
import com.pachuho.earthquakemap.data.di.get
import com.pachuho.earthquakemap.data.di.set
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingDateType
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType
import com.pachuho.earthquakemap.ui.util.getCurrentTimeAsLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Named

class SettingPreferencesDataSource @Inject constructor(
    @Named(NAME_DATA) private val pref: SharedPreferences
) {
    private val _settingsFlow = MutableSharedFlow<Settings>(replay = 1)
    val settingsFlow: Flow<Settings> = _settingsFlow.asSharedFlow()

    init {
        _settingsFlow.tryEmit(Settings(
            dateType = dateType,
            dateStart = dateStart,
            dateEnd = dateEnd,
            magStart = magStart,
            magEnd = magEnd,
            mapType = mapType
        ))
    }

    fun updateSetting(action: SettingResult) {
        val currentSettings = _settingsFlow.replayCache.last()

        val newSettings = when (action) {
            is SettingResult.DateType -> {
                dateType = action.dateType
                currentSettings.copy(dateType = action.dateType)
            }
            is SettingResult.DateStartAndEnd -> {
                dateStart = action.start
                dateEnd = action.end
                currentSettings.copy(
                    dateStart = action.start,
                    dateEnd = action.end
                )
            }
            is SettingResult.MagnitudeRange -> {
                magStart = action.start
                magEnd = action.end
                currentSettings.copy(
                    magStart = action.start,
                    magEnd = action.end
                )
            }
            is SettingResult.MapType -> {
                mapType = action.mapType
                currentSettings.copy(mapType = action.mapType)
            }

        }
        _settingsFlow.tryEmit(newSettings)
    }

    private var dateType: SettingDateType
        get() = pref.get<SettingDateType>(Key.SETTING_DATE_TYPE) ?: SettingDateType.OneYear
        set(value) {
            pref.set(Key.SETTING_DATE_TYPE, value)
        }

    private var dateStart: Long
        get() = pref.get<Long>(Key.SETTING_DATE_START) ?: 201602230145L
        set(value) {
            pref.set(Key.SETTING_DATE_START, value)
        }

    private var dateEnd: Long
        get() = pref.get<Long>(Key.SETTING_DATE_END) ?: getCurrentTimeAsLong()
        set(value) {
            pref.set(Key.SETTING_DATE_END, value)
        }

    private var magStart: Float
        get() = pref.get<Float>(Key.SETTING_MAG_START) ?: 1.0f
        set(value) {
            pref.set(Key.SETTING_MAG_START, value)
        }

    private var magEnd: Float
        get() = pref.get<Float>(Key.SETTING_MAG_END) ?: 10.0f
        set(value) {
            pref.set(Key.SETTING_MAG_END, value)
        }

    private var mapType: SettingMapType
        get() = pref.get<SettingMapType>(Key.SETTING_MAP_TYPE) ?: SettingMapType.Basic
        set(value) {
            pref.set(Key.SETTING_MAP_TYPE, value)
        }

    fun clearPref() {
        pref.edit().clear().apply()
    }

    object Key {
        const val SETTING_DATE_TYPE = "settingDateType"
        const val SETTING_DATE_START = "settingDateStart"
        const val SETTING_DATE_END = "settingDateEnd"
        const val SETTING_MAG_START = "settingMagStart"
        const val SETTING_MAG_END = "settingMagEnd"
        const val SETTING_MAP_TYPE = "settingMapType"
    }
}

sealed class SettingResult {
    data class DateType(val dateType: SettingDateType) : SettingResult()
    data class DateStartAndEnd(val start: Long, val end: Long) : SettingResult()
    data class MagnitudeRange(val start: Float, val end: Float) : SettingResult()
    data class MapType(val mapType: SettingMapType) : SettingResult()
}

data class Settings(
    val dateType: SettingDateType = SettingDateType.OneYear,
    val dateMin: Long = 201602230145L,
    val dateStart: Long = 201602230145L,
    val dateEnd: Long = getCurrentTimeAsLong(),
    val magStart: Float = 1.0f,
    val magEnd: Float = 10.0f,
    val mapType: SettingMapType = SettingMapType.Basic
) {
    fun isSameForMarker(settings: Settings): Boolean {
        return this.dateType == settings.dateType &&
            this.dateStart == settings.dateStart &&
            this.dateEnd == settings.dateEnd &&
            this.magStart == settings.magStart &&
            this.magEnd == settings.magEnd
    }
}