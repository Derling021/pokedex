package com.example.pokedex.models.pokemons;

import com.example.pokedex.networking.BaseRemote;

import io.reactivex.Observable;

public class PokemonsRemoteRepo extends BaseRemote implements IPokemonsRemoteRepo {
    @Override
    public Observable<PokemonResponse> getPokemons() {
        return create(PokemonsService.class).getPokemons();
    }
}

