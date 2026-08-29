package com.patacerta.app.ui.petprofile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.repository.PetRepository;
import com.patacerta.app.databinding.ActivityPetProfileBinding;
import com.patacerta.app.ui.reminder.AddReminderActivity;
import com.patacerta.app.util.Constants;
import com.patacerta.app.util.DateUtils;

/**
 * Tela de perfil do pet: carteira de vacinação (RF03) e evolução de peso
 * (RF06). Os dados vêm exclusivamente do Room, então a tela funciona
 * normalmente offline.
 */
public class PetProfileActivity extends AppCompatActivity {

    private ActivityPetProfileBinding binding;
    private PetRepository petRepository;
    private long petId;
    private String petName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petId = getIntent().getLongExtra(Constants.EXTRA_PET_ID, -1);
        petRepository = new PetRepository(this);

        binding.btnBack.setOnClickListener(v -> finish());

        VaccineAdapter vaccineAdapter = new VaccineAdapter();
        binding.rvVaccines.setAdapter(vaccineAdapter);
        binding.rvVaccines.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        petRepository.observePet(petId).observe(this, this::bindPet);
        petRepository.observeReminders(petId).observe(this, vaccineAdapter::submitList);
        petRepository.observeWeightHistory(petId).observe(this, binding.weightChart::setEntries);

        binding.btnAddReminder.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReminderActivity.class);
            intent.putExtra(Constants.EXTRA_PET_ID, petId);
            startActivity(intent);
        });
    }

    private void bindPet(Pet pet) {
        if (pet == null) return;
        petName = pet.getName();
        binding.txtPetName.setText(pet.getName());

        StringBuilder meta = new StringBuilder();
        if (pet.getBreed() != null && !pet.getBreed().isEmpty()) meta.append(pet.getBreed()).append(" · ");
        meta.append(ageLabel(pet.getBirthDate()));
        if (pet.getSex() != null && !pet.getSex().isEmpty()) meta.append(" · ").append(pet.getSex());
        binding.txtPetMeta.setText(meta.toString());

        binding.btnAddReminder.setText(getString(com.patacerta.app.R.string.add_reminder_for, pet.getName()));
    }

    private String ageLabel(String birthDateIso) {
        // Simplificação didática: exibe a data de nascimento crua quando presente.
        return birthDateIso == null || birthDateIso.isEmpty() ? "idade não informada" : birthDateIso;
    }
}
