package com.example.pokedex.models.pokemons;

import com.example.pokedex.BuildConfig;
import com.example.pokedex.networking.BaseRemote;

import java.util.List;

import io.reactivex.Observable;

public class PokemonsRemoteRepo extends BaseRemote implements IPokemonsRemoteRepo {
    @Override
    public Observable<List<Pokemons>> getPokemons() {
        return create(PokemonsService.class,BuildConfig.API_URL).getPokemons();
    }
}

