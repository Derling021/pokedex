package com.example.pokedex.models.pokemon;

import com.aimservices.telabook.networking.BaseRemote;

import java.util.List;

import io.reactivex.Observable;

public class AgentsRemoteRepo extends BaseRemote implements IAgentsRemoteRepo {
    @Override
    public Observable<List<Pokemon>> getAgents() {
        return create(AgentsService.class).getAgents();
    }
}
