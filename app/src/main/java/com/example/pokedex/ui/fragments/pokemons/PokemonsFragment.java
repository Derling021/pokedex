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

import com.example.pokedex.R;
import com.example.pokedex.models.pokemons.Pokemons;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class PokemonsFragment extends Fragment {
    private OnPokemonInteractionListener mListener;

    private CompositeDisposable disposables = new CompositeDisposable();
    private PokemonsAdapter pokemonsAdapter;
    private RecyclerView rvPokemons;
    private View errorLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PokemonsViewModel pokemonsViewModel = new ViewModelProvider(requireActivity()).get(PokemonsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pokemons, container, false);
        pokemonsAdapter = new PokemonsAdapter(mListener);
        rvPokemons = root.findViewById(R.id.agents_list);
        rvPokemons.setHasFixedSize(true);
        rvPokemons.setItemViewCacheSize(20);
        pokemonsAdapter.setHasStableIds(true);
        rvPokemons.setAdapter(pokemonsAdapter);
        errorLayout = root.findViewById(R.id.error_layout);
        errorLayout.findViewById(R.id.retryBtn).setOnClickListener(v -> {
            rvPokemons.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            disposables.add(pokemonsViewModel.getPokemons().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::pokemonsObtained, this::errorFetchinPokemons));
        });
        disposables.add(pokemonsViewModel.getPokemons().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::pokemonsObtained, this::errorFetchinPokemons));
        //pokemonsViewModel.getUnreadConversations().observe(getViewLifecycleOwner(), this::conversationsObtained);
        return root;
    }

 /*   private void conversationsObtained(ArrayList<WasNotSeen> wasNotSeens) {
        agentsAdapter.updateUnreadMessages(wasNotSeens);
    }*/

    private void errorFetchinPokemons(Throwable throwable) {
        Timber.w(throwable, "Error obtaining pokemons");
        rvPokemons.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void pokemonsObtained(List<Pokemons> pokemons) {
        Timber.d("%d pokemons obtained!!", pokemons.size());
        rvPokemons.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        pokemonsAdapter.setPokemons(pokemons);
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
        if (context instanceof OnPokemonInteractionListener) {
            mListener = (OnPokemonInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAgentInteractionListener");
        }
    }

    public interface OnPokemonInteractionListener {
        void pokemonTouched(Pokemons pokemon);
    }
}
