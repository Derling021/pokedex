package com.example.pokedex.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pokedex.models.pokemons.Pokemons;
import com.example.pokedex.models.pokemons.PokemonsDao;

@Database(entities = {Pokemons.class}, version = 1, exportSchema = false)
//@TypeConverters({com.aimservices.telabook.persistence.TypeConverters.class})
public abstract class PokedexDatabase extends RoomDatabase {

    private static volatile PokedexDatabase INSTANCE;

    public static PokedexDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PokedexDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PokedexDatabase.class, "Pokedex.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract PokemonsDao pokemonsDao();

}
