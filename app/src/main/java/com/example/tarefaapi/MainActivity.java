package com.example.tarefaapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.tarefaapi.PokedexAPI;
import com.example.tarefaapi.Pokemon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView pokemonName, pokemonId, pokemonHeight, pokemonWeight, pokemonBaseExperience;
    private ImageView pokemonImage;
    private Button btnBuscar;
    private EditText buscarPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pokemonName = findViewById(R.id.pokemonName);
        pokemonId = findViewById(R.id.pokemonId);
        pokemonHeight = findViewById(R.id.pokemonHeight);
        pokemonWeight = findViewById(R.id.pokemonWeight);
        pokemonBaseExperience = findViewById(R.id.pokemonBaseExperience);
        pokemonImage = findViewById(R.id.pokemonImage);
        btnBuscar = findViewById(R.id.btnBuscar);
        buscarPokemon = findViewById(R.id.buscarPokemon);

        btnBuscar.setOnClickListener(view -> {
            if(!buscarPokemon.getText().toString().isEmpty()){
                String pokemon;
                pokemon = buscarPokemon.getText().toString();
                fetchPokemonData(pokemon);
            }else{
                Toast.makeText(this, "Digite o nome de algum pokemon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPokemonData(String pokemonNameParam) {
        PokedexAPI apiService = RetrofitClient.getClient().create(PokedexAPI.class);
        Call<Pokemon> call = apiService.getPokemon(pokemonNameParam);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();

                    pokemonName.setText(pokemon.getName().toUpperCase());
                    pokemonId.setText("ID: #" + pokemon.getId());
                    pokemonHeight.setText("Altura: " + pokemon.getHeight() + " decímetros");
                    pokemonWeight.setText("Peso: " + pokemon.getWeight() + " hectogramas");
                    pokemonBaseExperience.setText("Experiência Base: " + pokemon.getBaseExperience());

                    String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getId() + ".png";
                    Glide.with(MainActivity.this).load(imageUrl).into(pokemonImage);
                } else {
                    Log.e("MainActivity", "Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e("MainActivity", "Erro: " + t.getMessage());
            }
        });
    }
}
