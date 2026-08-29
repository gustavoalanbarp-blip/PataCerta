package com.patacerta.app.data.remote;

import com.patacerta.app.data.remote.model.Breed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * TheDogAPI (https://www.thedogapi.com) — usada para sugerir raças e uma
 * foto de referência ao cadastrar um cão (RF02). Requer uma chave gratuita
 * (x-api-key), configurada em ApiClient.
 */
public interface DogApiService {

    @GET("breeds/search")
    Call<List<Breed>> searchBreeds(@Query("q") String query);

    @GET("breeds")
    Call<List<Breed>> listBreeds(@Query("limit") int limit);
}
