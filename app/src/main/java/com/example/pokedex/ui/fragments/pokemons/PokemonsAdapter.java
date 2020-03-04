package com.example.pokedex.ui.fragments.pokemons;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
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
import timber.log.Timber;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_item, parent, false);
        return new PokemonsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShimmering){
            holder.shimmer.startShimmer();
        }else{
            holder.shimmer.stopShimmer();
            holder.shimmer.hideShimmer();
            holder.bind(mPokemons.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return isShimmering ? 5 : mPokemons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ShimmerFrameLayout shimmer;
        final CircleImageView pokemonUrl;
        Pokemons mPokemons;
        final View mView;
        final MaterialTextView pokemonName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            this.shimmer = view.findViewById(R.id.shimmer_view_container);
            this.pokemonUrl = view.findViewById(R.id.url_pokemon);

            this.pokemonName = view.findViewById(R.id.pokemon_name);
            //this.messageCount = view.findViewById(R.id.message_count);
        }

        @SuppressLint("TimberArgCount")
        void bind(Pokemons pokemons) {
            mPokemons = pokemons;
            pokemonName.setText(pokemons.getPokemonName());
            /*if (agent.getUnreadMessagesCount() < 1) {
                messageCount.setVisibility(View.GONE);
            } else {
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(String.valueOf(agent.getUnreadMessagesCount()));
            }*/
            mView.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.pokemonTouched(pokemons);
                }
            });

            pokemonName.setBackground(null);
            if (pokemons.getPokemonUrl() != null) {

                String s1=pokemons.getPokemonUrl();
                 String s2= s1.replace("https://pokeapi.co/api/v2/pokemon/","");
                String s3=s2.replace("/","");
                Timber.e("veamos que es",s3);
                Picasso.get().load("https://picsum.photos/700/400?random").into(pokemonUrl);
            }


        }
    }

    public void setPokemons(List<Pokemons> pokemons) {
        isShimmering = false;
        this.mPokemons=new ArrayList<>(pokemons);
        notifyDataSetChanged();
        //refreshData();
    }


}
