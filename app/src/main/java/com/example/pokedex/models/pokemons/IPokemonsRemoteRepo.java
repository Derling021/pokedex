package com.example.pokedex.models.pokemons;

import java.util.List;

import io.reactivex.Observable;


public interface IPokemonsRemoteRepo {
    Observable<List<Pokemons>> getPokemons();
}
