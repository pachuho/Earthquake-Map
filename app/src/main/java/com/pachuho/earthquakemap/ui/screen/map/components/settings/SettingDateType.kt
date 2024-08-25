package com.pachuho.earthquakemap.ui.screen.map.components.settings

enum class SettingDateType(val value: String) {
    OneDay("지난 1일"),
    OneWeek("지난 1주"),
    OneMonth("지난 1개월"),
    OneYear("지난 1년"),
    Custom("기간 설정");

    companion object {
        fun getAll(): List<SettingDateType> {
            return SettingDateType.entries.map { it }
        }
    }
}