package com.example.pokedex.models.pokemons;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface PokemonsService {
    @GET("pokemon")
    Observable<PokemonResponse> getPokemons();
}
