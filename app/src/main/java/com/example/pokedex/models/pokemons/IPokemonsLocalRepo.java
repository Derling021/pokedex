package com.example.pokedex.models.pokemons;

import java.util.List;

import io.reactivex.Observable;

public interface IPokemonsLocalRepo {
    Observable<List<Pokemons>> getPokemons();

    void savePokemons(List<Pokemons> pokemons);
}
