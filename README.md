# 📅 PlanBot

**PlanBot**, canı sıkılanlara günün saatine ve hava durumuna göre öneriler sunan mini bir yapay zeka uygulamasıdır.  
Jetpack Compose ile geliştirilmiştir ve kullanıcıya anlık, akıllı öneriler verir.

---

## ✨ Özellikler

- ⏰ Günün saatine göre öneri verir (sabah, öğlen, akşam, gece)
- 🌦️ Konumunuza göre hava durumu bilgisini alır (OpenWeatherMap)
- 🤖 GPT-3.5 API ile doğal dilde öneriler sunar
- 📱 Jetpack Compose ile modern ve sade bir arayüz

---

## 🧠 Örnek Öneriler

*Hava güneşliyse ve saat öğleyse...*

- "Kahve iç"
- "Bir film izle"
- "Dışarı çık ve yürüyüş yap"

---

## ⚙️ Kullanılan Teknolojiler

- Kotlin + Jetpack Compose
- OpenWeatherMap API
- OpenAI GPT-3.5 Turbo
- Google Play Services - Konum Servisi

---

## 🔐 API Anahtarları

Uygulamanın çalışması için 2 API anahtarına ihtiyaç vardır:

- `GPT_API_KEY` → [OpenAI'den alınır](https://platform.openai.com/account/api-keys)
- `API_KEY` → [OpenWeatherMap'ten alınır](https://openweathermap.org/appid)


