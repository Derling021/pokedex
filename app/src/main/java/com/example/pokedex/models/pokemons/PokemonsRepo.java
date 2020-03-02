package com.example.pokedex.models.pokemon;

import com.aimservices.telabook.utils.Constants;
import com.aimservices.telabook.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class AgentsRepo implements IAgentsRepo {


    private IAgentsRemoteRepo remoteRepo;
    private IAgentsLocalRepo localRepo;

    public AgentsRepo(IAgentsRemoteRepo remoteRepo, IAgentsLocalRepo localRepo) {
        this.remoteRepo = remoteRepo;
        this.localRepo = localRepo;
    }


    @Override
    public Observable<List<Pokemon>> getAgents() {
        int companyId = PreferenceHelper.getSharedPreferenceInt(Constants.CURRENT_COMPANY_ID, -1);
        return Observable.mergeDelayError(
                localRepo.getAgentsForCampany(companyId).filter(agents -> !agents.isEmpty()),
                remoteRepo.getAgents().doOnNext(agents -> {
                    ArrayList<Pokemon> newAgentsList = new ArrayList<>();
                    for (Pokemon a :
                            agents) {
                        a.setCompanyId(companyId);
                        newAgentsList.add(a);
                    }
                    localRepo.saveAgents(newAgentsList);
                }));
    }
}
