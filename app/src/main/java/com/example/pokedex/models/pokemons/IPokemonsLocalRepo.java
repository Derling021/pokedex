package com.example.pokedex.models.pokemons;

import java.util.List;

import io.reactivex.Observable;

public interface IPokemonLocalRepo {
    Observable<List<Pokemons>> getPokemons();

    void savePokemons(List<Pokemons> pokemons);
}
