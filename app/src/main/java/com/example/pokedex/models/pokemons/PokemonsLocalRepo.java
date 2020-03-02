package com.example.pokedex.models.pokemons;

import android.app.Application;

import com.example.pokedex.persistence.PokedexDatabase;

import java.util.List;

import io.reactivex.Observable;

public class PokemonsLocalRepo implements IPokemonsLocalRepo {

    private final PokemonsDao pokemonsDao;

    public PokemonsLocalRepo(Application app) {
        PokedexDatabase tDb = PokedexDatabase.getDatabase(app);
        pokemonsDao = tDb.pokemonsDao();
    }

    @Override
    public Observable<List<Pokemons>> getPokemons() {
        return pokemonsDao.getPokemons();
    }

    @Override
    public void savePokemons(List<Pokemons> pokemons) {
        pokemonsDao.savePokemons(pokemons);
    }
}

