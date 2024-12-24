package com.example.tarefaapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokedexAPI {
    @GET("pokemon/{pokemon}")
    Call<Pokemon> getPokemon(@Path("pokemon")String pokemonName);
}
