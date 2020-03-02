package com.example.pokedex.models.pokemons;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface PokemonsDao {

    @Query("SELECT * FROM Pokemons")
    Observable<List<Pokemons>> getPokemons();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePokemons(List<Pokemons> pokemons);
}
