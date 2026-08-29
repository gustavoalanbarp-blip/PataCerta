package com.patacerta.app.ui.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.repository.PetRepository;
import com.patacerta.app.databinding.ActivityNotificationsBinding;
import com.patacerta.app.ui.home.ReminderAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central de notificações (RF11): lista todos os lembretes cadastrados,
 * mais recentes primeiro, reaproveitando o mesmo ReminderAdapter do
 * dashboard para manter consistência visual.
 */
public class NotificationsActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;
    private PetRepository petRepository;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petRepository = new PetRepository(this);
        binding.btnBack.setOnClickListener(v -> finish());

        adapter = new ReminderAdapter(reminder -> { /* marcar como lido futuramente */ });
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);

        petRepository.observePets().observe(this, pets -> {
            Map<Long, String> names = new HashMap<>();
            if (pets != null) for (Pet p : pets) names.put(p.getId(), p.getName());
            adapter.setPetNames(names);
        });

        petRepository.observeUpcomingReminders(50).observe(this, this::bindList);
    }

    private void bindList(List<com.patacerta.app.data.local.entity.Reminder> reminders) {
        adapter.submitList(reminders);
        binding.txtEmpty.setVisibility(reminders == null || reminders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
