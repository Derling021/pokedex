package com.example.pokedex.ui.fragments.pokemons;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.aimservices.telabook.R;
import com.aimservices.telabook.models.agents.Agent;
import com.aimservices.telabook.models.firebase.WasNotSeen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class AgentsFragment extends Fragment {
    private OnAgentInteractionListener mListener;

    private CompositeDisposable disposables = new CompositeDisposable();
    private AgentsAdapter agentsAdapter;
    private RecyclerView rvAgents;
    private View errorLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PokemonsViewModel agentsViewModel = new ViewModelProvider(requireActivity()).get(PokemonsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agents, container, false);
        agentsAdapter = new AgentsAdapter(mListener);
        rvAgents = root.findViewById(R.id.agents_list);
        rvAgents.setHasFixedSize(true);
        rvAgents.setItemViewCacheSize(20);
        agentsAdapter.setHasStableIds(true);
        rvAgents.setAdapter(agentsAdapter);
        errorLayout = root.findViewById(R.id.error_layout);
        errorLayout.findViewById(R.id.retryBtn).setOnClickListener(v -> {
            rvAgents.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            disposables.add(agentsViewModel.getAgents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::agentsObtained, this::errorFetchinAgents));
        });
        disposables.add(agentsViewModel.getAgents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::agentsObtained, this::errorFetchinAgents));
        agentsViewModel.getUnreadConversations().observe(getViewLifecycleOwner(), this::conversationsObtained);
        return root;
    }

    private void conversationsObtained(ArrayList<WasNotSeen> wasNotSeens) {
        agentsAdapter.updateUnreadMessages(wasNotSeens);
    }

    private void errorFetchinAgents(Throwable throwable) {
        Timber.w(throwable, "Error obtaining agents");
        rvAgents.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void agentsObtained(List<Agent> agents) {
        Timber.d("%d Agents obtained!!", agents.size());
        rvAgents.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        agentsAdapter.setAgents(agents);



    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (!disposables.isDisposed()) {
            disposables.dispose();
            disposables.clear();
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAgentInteractionListener) {
            mListener = (OnAgentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAgentInteractionListener");
        }
    }

    public interface OnAgentInteractionListener {
        void agentTouched(Agent agent);
    }
}
