package com.pachuho.earthquakemap.ui.screen.map.components.settings

import com.naver.maps.map.compose.MapType

enum class SettingMapType(val value: String) {
    Basic("기본"),
    Navi("네비게이션"),
    Hybrid("위성"),
    Terrain("지형도");

    companion object {
        fun getAll(): List<SettingMapType> {
            return entries.map { it }
        }

        fun SettingMapType.find(): MapType {
            return entries
                .find { this == it }
                ?.let {
                    when (it) {
                        Basic -> MapType.Basic
                        Navi -> MapType.Navi
                        Hybrid -> MapType.Hybrid
                        Terrain -> MapType.Terrain
                    }
                } ?: run {
                MapType.Basic
            }
        }
    }
}