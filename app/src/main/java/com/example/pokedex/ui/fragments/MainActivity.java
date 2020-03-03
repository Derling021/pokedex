package com.example.pokedex.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pokedex.R;
import com.example.pokedex.models.pokemons.Pokemons;
import com.example.pokedex.ui.fragments.pokemons.PokemonsFragment;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements PokemonsFragment.OnPokemonInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void pokemonTouched(Pokemons pokemon) {
        Timber.d("Pokemon %s", pokemon);
    }
}
