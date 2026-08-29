package com.patacerta.app.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.data.repository.AuthRepository;
import com.patacerta.app.data.repository.PetRepository;
import com.patacerta.app.databinding.ActivityHomeBinding;
import com.patacerta.app.ui.locator.VetLocatorActivity;
import com.patacerta.app.ui.notifications.NotificationsActivity;
import com.patacerta.app.ui.petprofile.AddEditPetActivity;
import com.patacerta.app.ui.petprofile.PetProfileActivity;
import com.patacerta.app.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hub central do app (ver diagrama de fluxo da Atividade 1). A partir daqui
 * o usuário acessa perfil do pet, cria lembretes, consulta notificações e
 * usa o localizador de clínicas.
 */
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private PetRepository petRepository;
    private PetAdapter petAdapter;
    private ReminderAdapter reminderAdapter;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> { /* segue normalmente mesmo se negado */ });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestNotificationPermissionIfNeeded();

        petRepository = new PetRepository(this);
        AuthRepository authRepository = new AuthRepository(this);
        binding.txtGreeting.setText(getString(com.patacerta.app.R.string.home_greeting, authRepository.getDisplayName()));

        setupPetsList();
        setupRemindersList();
        setupBottomNav();

        binding.btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        binding.swipeRefresh.setOnRefreshListener(() -> binding.swipeRefresh.setRefreshing(false));

        observeData();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void setupPetsList() {
        petAdapter = new PetAdapter(new PetAdapter.Listener() {
            @Override
            public void onPetClick(Pet pet) {
                Intent intent = new Intent(HomeActivity.this, PetProfileActivity.class);
                intent.putExtra(Constants.EXTRA_PET_ID, pet.getId());
                startActivity(intent);
            }

            @Override
            public void onAddClick() {
                startActivity(new Intent(HomeActivity.this, AddEditPetActivity.class));
            }
        });
        binding.rvPets.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvPets.setAdapter(petAdapter);
    }

    private void setupRemindersList() {
        reminderAdapter = new ReminderAdapter(reminder -> { /* abrir detalhe futuramente */ });
        binding.rvReminders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReminders.setAdapter(reminderAdapter);
    }

    private void setupBottomNav() {
        binding.bottomNav.setSelectedItemId(com.patacerta.app.R.id.nav_home);
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == com.patacerta.app.R.id.nav_home) {
                return true;
            } else if (id == com.patacerta.app.R.id.nav_map) {
                startActivity(new Intent(this, VetLocatorActivity.class));
                return true;
            } else if (id == com.patacerta.app.R.id.nav_pets || id == com.patacerta.app.R.id.nav_profile) {
                // Reaproveita a lista de pets já exibida no dashboard.
                return true;
            }
            return false;
        });
    }

    private void observeData() {
        petRepository.observePets().observe(this, pets -> {
            petAdapter.submitList(pets);
            binding.txtEmptyState.setVisibility(pets == null || pets.isEmpty() ? View.VISIBLE : View.GONE);

            Map<Long, String> names = new HashMap<>();
            if (pets != null) for (Pet p : pets) names.put(p.getId(), p.getName());
            reminderAdapter.setPetNames(names);
        });

        petRepository.observeUpcomingReminders(5).observe(this, this::bindReminders);
    }

    private void bindReminders(List<Reminder> reminders) {
        reminderAdapter.submitList(reminders);

        if (reminders != null && !reminders.isEmpty()) {
            Reminder next = reminders.get(0);
            binding.cardNextReminder.setVisibility(View.VISIBLE);
            binding.txtNextReminderTitle.setText("💉 " + next.getTitle());
            binding.txtNextReminderSubtitle.setText(
                    com.patacerta.app.util.DateUtils.relativeDueLabel(next.getDueAtMillis()));
        } else {
            binding.cardNextReminder.setVisibility(View.GONE);
        }
    }
}
