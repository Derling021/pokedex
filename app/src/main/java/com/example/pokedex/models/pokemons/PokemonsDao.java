package com.example.pokedex.models.pokemons;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface PokemonDao {

    @Query("SELECT * FROM Pokemon")
    Observable<List<Pokemon>> getAgents(int companyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAgents(List<Pokemon> agents);
}
