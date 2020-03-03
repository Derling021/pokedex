package com.example.pokedex.models.pokemons;

import io.reactivex.Observable;


public interface IPokemonsRemoteRepo {
    Observable<PokemonResponse> getPokemons();
}
