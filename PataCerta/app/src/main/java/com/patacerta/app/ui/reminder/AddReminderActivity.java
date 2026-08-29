package com.patacerta.app.ui.reminder;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.patacerta.app.R;
import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.data.repository.PetRepository;
import com.patacerta.app.databinding.ActivityAddReminderBinding;
import com.patacerta.app.notification.ReminderScheduler;
import com.patacerta.app.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Cadastro de lembrete (RF04/RF05). Ao salvar, além de persistir no Room,
 * agenda o alarme local que dispara a notificação na data/hora escolhida.
 */
public class AddReminderActivity extends AppCompatActivity {

    private ActivityAddReminderBinding binding;
    private PetRepository petRepository;
    private long petId;
    private String selectedType = Reminder.TYPE_VACCINE;
    private Pet currentPet;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petId = getIntent().getLongExtra(Constants.EXTRA_PET_ID, -1);
        petRepository = new PetRepository(this);
        petRepository.observePet(petId).observe(this, pet -> currentPet = pet);

        binding.btnClose.setOnClickListener(v -> finish());
        binding.chipVaccine.setOnClickListener(v -> selectType(Reminder.TYPE_VACCINE));
        binding.chipMedicine.setOnClickListener(v -> selectType(Reminder.TYPE_MEDICINE));
        binding.chipWalk.setOnClickListener(v -> selectType(Reminder.TYPE_WALK));
        binding.btnSave.setOnClickListener(v -> saveReminder());
    }

    private void selectType(String type) {
        selectedType = type;
        binding.chipVaccine.setBackgroundResource(type.equals(Reminder.TYPE_VACCINE) ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
        binding.chipMedicine.setBackgroundResource(type.equals(Reminder.TYPE_MEDICINE) ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
        binding.chipWalk.setBackgroundResource(type.equals(Reminder.TYPE_WALK) ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);

        int selectedColor = getResources().getColor(com.patacerta.app.R.color.white);
        int unselectedColor = getResources().getColor(com.patacerta.app.R.color.text_primary);
        binding.chipVaccine.setTextColor(type.equals(Reminder.TYPE_VACCINE) ? selectedColor : unselectedColor);
        binding.chipMedicine.setTextColor(type.equals(Reminder.TYPE_MEDICINE) ? selectedColor : unselectedColor);
        binding.chipWalk.setTextColor(type.equals(Reminder.TYPE_WALK) ? selectedColor : unselectedColor);
    }

    private void saveReminder() {
        String title = binding.inputTitle.getText() != null ? binding.inputTitle.getText().toString().trim() : "";
        String dateStr = binding.inputDate.getText() != null ? binding.inputDate.getText().toString().trim() : "";
        String timeStr = binding.inputTime.getText() != null ? binding.inputTime.getText().toString().trim() : "";
        String notes = binding.inputNotes.getText() != null ? binding.inputNotes.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title)) {
            binding.inputTitle.setError("Informe um título");
            return;
        }

        long dueAt = parseDueDate(dateStr, timeStr);
        if (dueAt <= 0) {
            Toast.makeText(this, "Informe data e hora válidas (dd/mm/aaaa e hh:mm)", Toast.LENGTH_SHORT).show();
            return;
        }

        Reminder reminder = new Reminder();
        reminder.setPetId(petId);
        reminder.setTitle(title);
        reminder.setType(selectedType);
        reminder.setDueAtMillis(dueAt);
        reminder.setNotes(notes);

        petRepository.saveReminder(reminder, () -> runOnUiThread(() -> {
            ReminderScheduler.schedule(this, reminder, currentPet != null ? currentPet.getName() : "Seu pet");
            Toast.makeText(this, "Lembrete salvo!", Toast.LENGTH_SHORT).show();
            finish();
        }));
    }

    private long parseDueDate(String dateStr, String timeStr) {
        try {
            Date date = TextUtils.isEmpty(dateStr) ? new Date() : dateFormat.parse(dateStr);
            Date time = TextUtils.isEmpty(timeStr) ? null : timeFormat.parse(timeStr);
            if (date == null) return -1;

            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            if (time != null) {
                Calendar calTime = Calendar.getInstance();
                calTime.setTime(time);
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
            }
            calDate.set(Calendar.SECOND, 0);
            return calDate.getTimeInMillis();
        } catch (ParseException e) {
            return -1;
        }
    }
}
