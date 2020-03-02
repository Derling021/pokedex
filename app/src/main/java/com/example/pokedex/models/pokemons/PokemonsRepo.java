package com.example.pokedex.models.pokemons;

/*import com.aimservices.telabook.utils.Constants;
import com.aimservices.telabook.utils.PreferenceHelper;*/

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
                localRepo.getPokemons(),
                remoteRepo.getPokemons().doOnNext(pokemon -> {
                    ArrayList<Pokemons> newPokemonsList = new ArrayList<>();
                    for (Pokemons a : pokemon) {
                        newPokemonsList.add(a);
                    }
                    localRepo.savePokemons(newPokemonsList);
                }));
    }

}
