package com.example.pokedex.models.pokemons;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import lombok.Data;

@Data
@Entity(indices = {@Index(value = {"name"})})
public class Pokemon {
    @PrimaryKey
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
}
