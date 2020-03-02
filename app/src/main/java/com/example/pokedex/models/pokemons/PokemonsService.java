package com.example.pokedex.models.pokemons;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface PokemonsService {
    @GET("pokemon")
    Observable<List<Pokemons>> getPokemons();
}
