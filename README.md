
<p align="center">
  <img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/project_mockup.png" alt="logo"">
</p>
<br>

<p align="center">
<img alt="kotlin" src="https://img.shields.io/badge/kotlin-2.0.0-blue?logo=kotlin"/>
<img alt="api" src="https://img.shields.io/badge/API-24%2B-green?logo=android"/>
<img alt="api" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?logo=apache"/>
</p>

"Earthquake Map" is a map service that allows you to visually check earthquake information. 
Through this service, users can easily grasp details such as the magnitude, location, and time of earthquakes, making it a useful reference for safe responses. Use this service to see which areas experience frequent earthquakes.

<br>

## 🔍 Architecture Overview
<p align="center">
    <img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/project_architecture.jpg?raw=true" alt="architecture" style="width:75%;">
</p>

<br>

## 💡 Stack & Libraries
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/video.gif?raw=true" width="25%" align="right"/>

- Minimum SDK level 24
- Target SDK level 35

- Base on [Kotlin](https://kotlinlang.org/) + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://developer.android.com/kotlin/flow?hl=ko)
- [Compose](https://developer.android.com/jetpack/compose) - modern toolkit for building native UI.
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - help you produce better-organized, and often lighter-weight code, that is easier to maintain.
- [Dagger-Hilt](https://dagger.dev/hilt/) - dependency injection.
- [Room](https://developer.android.com/training/data-storage/room?hl=ko) - SQLite database management on Android.
- [Retrofit2](https://github.com/square/retrofit) - REST APIs.
- [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging for API.
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Moshi](https://github.com/square/moshi) - modern JSON library for Android.
- [Lottie](https://github.com/airbnb/lottie-android) - animation library for Android that renders JSON-based animations.
- [Naver Map](https://github.com/fornewid/naver-map-compose) - map library for android compose.

<br>

📷 Screenshots
----------
<p align="center">
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_downloading.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_basic.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_basic_clustering.png?raw=true" width="30%"/>
</p>

<p align="center">
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_hybrid.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_terrain.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_info.png?raw=true" width="30%"/>
</p>

<p align="center">
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_marker_info.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_setting.png?raw=true" width="30%"/>
<img src="https://github.com/pachuho/EarthquakeMap/blob/main/screenshot/map_setting_date.png?raw=true" width="30%"/>
</p>

<br>

## ✨ API

[Seoul open data potal API](https://data.seoul.go.kr/dataList/OA-21059/S/1/datasetView.do)

<br>

## 🏃 How to Run
Need to add oauth key to local.properties
- NAVER_MAP_CLIENT_ID -> [Here](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)
- EARTHQUAKE_OAUTH_KEY -> [Here](https://data.seoul.go.kr/dataList/OA-21059/S/1/datasetView.do)

<br>

## ⭐ Is this project useful?

Support it by joining [stargazers](https://github.com/pachuho/EarthquakeMap/stargazers) for this repository

<br>

## 📝 License

```
Copyright 2024 Juho Park

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
```
