package com.patacerta.app.ui.petprofile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.remote.ApiClient;
import com.patacerta.app.data.remote.model.Breed;
import com.patacerta.app.data.repository.PetRepository;
import com.patacerta.app.databinding.ActivityAddEditPetBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Cadastro de pet (RF02). Ao escolher a espécie "Cachorro", consulta a
 * TheDogAPI (GET /breeds/search) para sugerir raças por autocomplete —
 * segunda integração de API do projeto, além da autenticação.
 */
public class AddEditPetActivity extends AppCompatActivity {

    private ActivityAddEditPetBinding binding;
    private PetRepository petRepository;
    private String selectedSpecies = "Cachorro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditPetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petRepository = new PetRepository(this);

        binding.btnClose.setOnClickListener(v -> finish());
        binding.chipDog.setOnClickListener(v -> selectSpecies(true));
        binding.chipCat.setOnClickListener(v -> selectSpecies(false));
        binding.btnSave.setOnClickListener(v -> savePet());

        setupBreedAutocomplete();
    }

    private void selectSpecies(boolean isDog) {
        selectedSpecies = isDog ? "Cachorro" : "Gato";
        binding.chipDog.setBackgroundResource(isDog
                ? com.patacerta.app.R.drawable.bg_chip_selected
                : com.patacerta.app.R.drawable.bg_chip_unselected);
        binding.chipCat.setBackgroundResource(!isDog
                ? com.patacerta.app.R.drawable.bg_chip_selected
                : com.patacerta.app.R.drawable.bg_chip_unselected);
        binding.chipDog.setTextColor(getColorCompat(isDog));
        binding.chipCat.setTextColor(getColorCompat(!isDog));
        binding.layoutBreed.setEnabled(isDog);
        binding.layoutBreed.setHint(isDog ? "Raça (buscar via TheDogAPI)" : "Raça");
    }

    private int getColorCompat(boolean selected) {
        return selected
                ? getResources().getColor(com.patacerta.app.R.color.white)
                : getResources().getColor(com.patacerta.app.R.color.text_primary);
    }

    /** Busca a lista de raças assim que o usuário digita >= 2 caracteres. */
    private void setupBreedAutocomplete() {
        binding.inputBreed.setThreshold(2);
        binding.inputBreed.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) loadInitialBreeds();
        });
    }

    private void loadInitialBreeds() {
        ApiClient.dogApiService().listBreeds(30).enqueue(new Callback<List<Breed>>() {
            @Override
            public void onResponse(Call<List<Breed>> call, Response<List<Breed>> response) {
                if (!response.isSuccessful() || response.body() == null) return;
                List<String> names = new ArrayList<>();
                for (Breed b : response.body()) names.add(b.getName());
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddEditPetActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            names);
                    binding.inputBreed.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(Call<List<Breed>> call, Throwable t) {
                // Falha silenciosa: o campo de raça continua editável manualmente (offline-friendly).
            }
        });
    }

    private void savePet() {
        String name = binding.inputName.getText() != null ? binding.inputName.getText().toString().trim() : "";
        if (TextUtils.isEmpty(name)) {
            binding.inputName.setError("Informe o nome do pet");
            return;
        }

        String breed = binding.inputBreed.getText() != null ? binding.inputBreed.getText().toString().trim() : "";
        String birthDate = binding.inputBirthDate.getText() != null ? binding.inputBirthDate.getText().toString().trim() : "";
        String weightStr = binding.inputWeight.getText() != null ? binding.inputWeight.getText().toString().trim() : "";
        double weight = 0;
        try { weight = weightStr.isEmpty() ? 0 : Double.parseDouble(weightStr.replace(",", ".")); } catch (NumberFormatException ignored) {}

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(selectedSpecies);
        pet.setBreed(breed);
        pet.setBirthDate(birthDate);
        pet.setWeightKg(weight);

        binding.progressBar.setVisibility(View.VISIBLE);
        double finalWeight = weight;
        petRepository.savePet(pet, finalWeight, () -> runOnUiThread(() -> {
            Toast.makeText(this, name + " foi adicionado(a) com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }));
    }
}
