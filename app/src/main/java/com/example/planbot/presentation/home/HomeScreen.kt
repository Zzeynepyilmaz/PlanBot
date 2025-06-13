package com.example.planbot.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.planbot.data.gpt.GPTSuggestionService
import com.example.planbot.data.weather.WeatherRepository
import com.example.planbot.domain.GetSuggestionsUseCase
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen() {
    var showSuggestions by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var weather by remember { mutableStateOf("sunny") }
    var gptSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val repo = WeatherRepository("API_KEY")
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = repo.getCurrentWeatherByLocation(it.latitude, it.longitude)
                        withContext(Dispatchers.Main) {
                            weather = result
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(showSuggestions) {
        if (showSuggestions) {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val gptService = GPTSuggestionService("GPT_API_KEY")
            val prompt = "Saat şu an $hour ve hava $weather. Canı sıkılan birine 3 öneri ver."
            try {
                val result = gptService.getSuggestions(prompt)
                gptSuggestions = result
            } catch (e: Exception) {
                errorMessage = "Öneri alınamadı, lütfen daha sonra tekrar deneyin."
            } finally {
                isLoading = false
            }
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PlanBot'a hoş geldin!", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            showSuggestions = true
            isLoading = true
            errorMessage = null
        }) {
            Text("Canım sıkılıyor")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showSuggestions) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                gptSuggestions.isNotEmpty() -> {
                    gptSuggestions.forEach { SuggestionCard(it) }
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}