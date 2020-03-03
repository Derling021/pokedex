package com.example.pokedex.models.pokemons;

import java.util.ArrayList;

import lombok.Data;

@Data
class PokemonResponse {
    int count;
    String next;
    String previous;
    ArrayList<Pokemons> results;
}
