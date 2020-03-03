package com.example.pokedex.models.pokemons;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class PokemonsRepo implements IPokemonsRepo {


    private IPokemonsRemoteRepo remoteRepo;
    private IPokemonsLocalRepo localRepo;

    public PokemonsRepo(IPokemonsRemoteRepo remoteRepo, IPokemonsLocalRepo localRepo) {
        this.remoteRepo = remoteRepo;
        this.localRepo = localRepo;
    }


    @Override
    public Observable<List<Pokemons>> getPokemons() {
       // int companyId = PreferenceHelper.getSharedPreferenceInt(Constants.CURRENT_COMPANY_ID, -1);
        return Observable.mergeDelayError(
                localRepo.getPokemons().filter(pokemons -> !pokemons.isEmpty()), // Esto te puede retornar vacío
                remoteRepo.getPokemons().doOnNext(pokemonResponse -> {
                    ArrayList<Pokemons> newPokemonsList = new ArrayList<>(pokemonResponse.getResults());
                    localRepo.savePokemons(newPokemonsList);
                }).map(PokemonResponse::getResults));
    }

}
