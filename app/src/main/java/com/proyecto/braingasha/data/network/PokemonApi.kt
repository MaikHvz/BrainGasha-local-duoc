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
            val other = sprites.optJSONObject("other")
            val official = other?.optJSONObject("official-artwork")?.optString("front_default")
            val dreamWorld = other?.optJSONObject("dream_world")?.optString("front_default")
            val frontDefault = sprites.optString("front_default", "")

            // Tomar la primera imagen disponible, si no hay usar fallback directo
            val imageUrl = listOf(official, dreamWorld, frontDefault)
                .firstOrNull { !it.isNullOrBlank() }
                ?: officialArtworkUrl(id) // fallback directo a GitHub

            PokemonInfo(id = id, name = name, imageUrl = imageUrl)
        } catch (e: Exception) {
            Log.e("PokemonApi", "fetchPokemon error", e)
            // fallback para evitar imageUrl vacío
            return@withContext PokemonInfo(
                id = id,
                name = "Pokémon #$id",
                imageUrl = officialArtworkUrl(id)
            )
        }
    }

    private fun officialArtworkUrl(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}
