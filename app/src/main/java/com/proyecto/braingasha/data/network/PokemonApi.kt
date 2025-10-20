package com.proyecto.braingasha.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class PokemonInfo(
    val id: Int,
    val name: String,
    val imageUrl: String
)

object PokemonApi {
    private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"

    suspend fun fetchPokemon(id: Int): PokemonInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000

            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                Log.e("PokemonApi", "HTTP error: $code")
                return@withContext null
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(body)

            val name = json.getString("name").replaceFirstChar { it.uppercase() }
            val sprites = json.getJSONObject("sprites")
            val other = sprites.getJSONObject("other")
            val official = other.getJSONObject("official-artwork")
            val imageUrl = official.getString("front_default")

            PokemonInfo(id = id, name = name, imageUrl = imageUrl)
        } catch (e: Exception) {
            Log.e("PokemonApi", "fetchPokemon error", e)
            null
        }
    }
}