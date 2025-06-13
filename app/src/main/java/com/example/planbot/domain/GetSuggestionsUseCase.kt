package com.example.planbot.domain

import java.util.Calendar

class GetSuggestionsUseCase {

    fun execute(hour: Int, weather: String): List<String> {
        val timeOfDay = when (hour) {
            in 6..11 -> "sabah"
            in 12..16 -> "öğlen"
            in 17..21 -> "akşam"
            else -> "gece"
        }

        val öneriler = mutableListOf<String>()

        when (timeOfDay) {
            "sabah" -> öneriler += listOf("Kahve iç", "Yürüyüş yap", "Kahvaltı et")
            "öğlen" -> öneriler += listOf("Film izle", "Dışarı çık", "Yeni bir şey öğren")
            "akşam" -> öneriler += listOf("Kitap oku", "Dizi izle", "Yemek yap")
            "gece" -> öneriler += listOf("Günlük yaz", "Meditasyon yap", "Müzik dinle")
        }

        if (weather == "rainy") {
            öneriler.remove("Yürüyüş yap")
            öneriler.remove("Dışarı çık")
        }

        return öneriler.shuffled().take(3)
    }
}