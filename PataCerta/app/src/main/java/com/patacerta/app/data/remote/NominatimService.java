package com.patacerta.app.data.remote;

import com.patacerta.app.data.remote.model.NominatimPlace;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Nominatim (OpenStreetMap) — API pública e gratuita de geocodificação,
 * usada no localizador de clínicas veterinárias (RF07) para buscar
 * "clínica veterinária" próximo a uma cidade/bairro digitado pelo usuário,
 * sem exigir uma chave de API paga (ex.: Google Places).
 * Uso responsável: no máximo ~1 requisição/segundo (ver relatório técnico).
 */
public interface NominatimService {

    @GET("search")
    Call<List<NominatimPlace>> search(
            @Query("q") String query,
            @Query("format") String format,
            @Query("limit") int limit,
            @Query("addressdetails") int addressDetails
    );
}
