
package com.example.papergemoetry.network

import com.example.papergemoetry.models.Character

import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyApi {
    @GET("character")
    fun getCharacters(): Call<CharacterResponse>
}

data class CharacterResponse(
    val results: List<Character>
)
