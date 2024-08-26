package com.pachuho.earthquakemap.ui.screen.map.components.settings

enum class SettingDateType(val value: String) {
    OneDay("1일"),
    OneWeek("1주"),
    OneMonth("1개월"),
    OneYear("1년"),
    Custom("기간 설정");

    companion object {
        fun getAll(): List<SettingDateType> {
            return SettingDateType.entries.map { it }
        }
    }
}