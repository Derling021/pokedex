package com.example.pokedex.models.pokemon;

import java.util.List;

import io.reactivex.Observable;

public interface IAgentsRepo {

    Observable<List<Pokemon>> getAgents();
}
