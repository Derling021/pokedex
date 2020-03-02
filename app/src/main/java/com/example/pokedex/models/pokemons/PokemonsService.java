package com.example.pokedex.models.pokemon;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface AgentsService {
    @GET("internal_conversations")
    Observable<List<Pokemon>> getAgents();
}
