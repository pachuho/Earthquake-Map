package com.pachuho.earthquakemap

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EarthquakeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT_ID)
    }
}