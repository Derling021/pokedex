package com.example.pokedex.ui.fragments.pokemons;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.pokedex.models.pokemons.PokemonsLocalRepo;
import com.example.pokedex.models.pokemons.PokemonsRemoteRepo;
import com.example.pokedex.models.pokemons.PokemonsRepo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PokemonsViewModel extends AndroidViewModel implements ValueEventListener {
    private final PokemonsRepo pokemonsRepo;

    public PokemonsViewModel(Application application) {
        super(application);
        pokemonsRepo = new PokemonsRepo(new PokemonsRemoteRepo(), new PokemonsLocalRepo(application));
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
