package com.example.planbot.data.gpt

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class GPTSuggestionService(private val apiKey: String) {

    suspend fun getSuggestions(prompt: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val connection = URL("https://api.openai.com/v1/chat/completions")
                .openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.doOutput = true

            val requestBody = """
                {
                  "model": "gpt-3.5-turbo",
                  "messages": [
                    {"role": "system", "content": "Kısa, sade ve eğlenceli öneriler sunan bir asistan gibi davran."},
                    {"role": "user", "content": "$prompt"}
                  ],
                  "temperature": 0.7
                }
            """.trimIndent()

            connection.outputStream.write(requestBody.toByteArray())

            val response = connection.inputStream.bufferedReader().readText()
            Log.e("GPT_RESPONSE", response)
            val json = JSONObject(response)
            val reply = json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")

            return@withContext reply.split("\n")
                .map { it.trim().removePrefix("-").trim() }
                .filter { it.isNotEmpty() }

        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext listOf("Film izle", "Kahve iç", "Yürüyüşe çık")
        }
    }
}