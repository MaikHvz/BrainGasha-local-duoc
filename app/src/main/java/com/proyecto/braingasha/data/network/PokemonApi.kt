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
    private const val PLACEHOLDER_IMAGE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"

    suspend fun fetchPokemon(id: Int): PokemonInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("PokemonApi", "HTTP error: ${connection.responseCode}")
                return@withContext null
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(body)

            val name = json.optString("name", "Pokémon #$id").replaceFirstChar { it.uppercase() }
            val sprites = json.optJSONObject("sprites")
            val other = sprites?.optJSONObject("other")
            val officialArtwork = other?.optJSONObject("official-artwork")?.optString("front_default")
            val dreamWorld = other?.optJSONObject("dream_world")?.optString("front_default")
            val frontDefault = sprites?.optString("front_default")

            val imageUrl = listOf(officialArtwork, dreamWorld, frontDefault, PLACEHOLDER_IMAGE)
                .firstOrNull { !it.isNullOrBlank() } ?: PLACEHOLDER_IMAGE

            PokemonInfo(id = id, name = name, imageUrl = imageUrl)

        } catch (e: Exception) {
            Log.e("PokemonApi", "fetchPokemon error", e)
            null
        }
    }
}
