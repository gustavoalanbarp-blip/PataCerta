package com.patacerta.app.ui.locator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.patacerta.app.data.remote.ApiClient;
import com.patacerta.app.data.remote.model.NominatimPlace;
import com.patacerta.app.databinding.ActivityVetLocatorBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Localizador de clínicas veterinárias (RF07). Consome a API pública e
 * gratuita Nominatim (OpenStreetMap) para geocodificar a busca textual do
 * usuário — dispensa uma chave paga do Google Places para esta demonstração.
 */
public class VetLocatorActivity extends AppCompatActivity {

    private ActivityVetLocatorBinding binding;
    private ClinicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetLocatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ClinicAdapter();
        binding.rvClinics.setLayoutManager(new LinearLayoutManager(this));
        binding.rvClinics.setAdapter(adapter);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearch = actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            if (isSearch) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = binding.inputSearch.getText() != null ? binding.inputSearch.getText().toString().trim() : "";
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Digite uma cidade ou bairro para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullQuery = "clínica veterinária " + query;
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);

        ApiClient.nominatimService()
                .search(fullQuery, "json", 10, 1)
                .enqueue(new Callback<List<NominatimPlace>>() {
                    @Override
                    public void onResponse(Call<List<NominatimPlace>> call, Response<List<NominatimPlace>> response) {
                        runOnUiThread(() -> {
                            binding.progressBar.setVisibility(View.GONE);
                            List<NominatimPlace> places = response.body();
                            adapter.submitList(places);
                            boolean empty = places == null || places.isEmpty();
                            binding.txtEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
                            if (empty) {
                                binding.txtEmpty.setText("Nenhuma clínica encontrada para \"" + query
                                        + "\". Tente buscar pelo nome da cidade.");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<NominatimPlace>> call, Throwable t) {
                        runOnUiThread(() -> {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.txtEmpty.setVisibility(View.VISIBLE);
                            binding.txtEmpty.setText("Não foi possível buscar agora. Verifique sua conexão e tente novamente.");
                            Toast.makeText(VetLocatorActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }
}
