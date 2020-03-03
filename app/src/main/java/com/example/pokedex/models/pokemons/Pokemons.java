package com.example.pokedex.models.pokemons;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
@Entity(indices = {@Index(value = {"name"})})
public class Pokemons {
    @PrimaryKey(autoGenerate = true)
    private int id =  0;
    @SerializedName("name")
    private String pokemonName;
    @SerializedName("url")
    private String pokemonUrl;
}
