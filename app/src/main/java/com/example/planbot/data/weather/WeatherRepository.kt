package com.example.planbot.data.weather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class WeatherRepository(private val apiKey: String) {

    suspend fun getCurrentWeather(city: String): String = withContext(Dispatchers.IO) {
        try {
            val url =
                "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}&lang=tr"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val weather = json.getJSONArray("weather")
                .getJSONObject(0)
                .getString("main")
                .lowercase()

            return@withContext when {
                weather.contains("rain") -> "rainy"
                weather.contains("clear") -> "sunny"
                weather.contains("cloud") -> "cloudy"
                else -> "unknown"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "unknown"
        }
    }
}