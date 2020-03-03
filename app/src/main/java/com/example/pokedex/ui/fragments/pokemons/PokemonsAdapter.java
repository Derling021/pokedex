package com.example.pokedex.ui.fragments.pokemons;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.models.pokemons.Pokemons;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PokemonsAdapter extends RecyclerView.Adapter<PokemonsAdapter.ViewHolder> {

    private ArrayList<Pokemons> mPokemons;
    private final PokemonsFragment.OnPokemonInteractionListener mListener;
    private boolean isShimmering = true;
//private ArrayList<WasNotSeen> unreadMessages = new ArrayList<>();

    PokemonsAdapter(PokemonsFragment.OnPokemonInteractionListener listener) {
        mPokemons = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return isShimmering ? 5 : mPokemons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ShimmerFrameLayout shimmer;
        final CircleImageView agentsProfile;
        Pokemons mPokemon;
        final View mView;
        final MaterialTextView pokemonName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            this.shimmer = view.findViewById(R.id.shimmer_view_container);
            this.agentsProfile = view.findViewById(R.id.url_pokemon);

            this.pokemonName = view.findViewById(R.id.pokemon_name);
            //this.messageCount = view.findViewById(R.id.message_count);
        }

        void bind(Agent agent) {
            mAgent = agent;
            agentName.setText(agent.getPersonName());

            if (agent.getUnreadMessagesCount() < 1) {
                messageCount.setVisibility(View.GONE);
            } else {
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(String.valueOf(agent.getUnreadMessagesCount()));
            }
            mView.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.agentTouched(agent);
                }
            });

            agentName.setBackground(null);
            agentsProfile.setBackground(null);
            agentsProfile.setBorderColor(mView.getContext().getColor(R.color.colorPrimary));
            if (agent.getProfileImageUrl() != null) {
                Picasso.get().load(agent.getProfileImageUrl()).into(agentsProfile);
            }


        }
    }

    public void setPokemons(List<Pokemons> pokemons) {
        isShimmering = false;
        this.mPokemons=new ArrayList<>(pokemons);
        //refreshData();
    }


}
