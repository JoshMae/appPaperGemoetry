
package com.example.papergemoetry.network

import com.example.papergemoetry.models.Character

import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyApi {
    @GET("/api/producto")
    fun getCharacters(): Call<List<Character>>
}

data class CharacterResponse(
    val results: List<Character>
)
