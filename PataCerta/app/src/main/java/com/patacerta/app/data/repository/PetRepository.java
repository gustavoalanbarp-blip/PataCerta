package com.patacerta.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.patacerta.app.data.local.AppDatabase;
import com.patacerta.app.data.local.PetDao;
import com.patacerta.app.data.local.ReminderDao;
import com.patacerta.app.data.local.WeightEntryDao;
import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.data.local.entity.WeightEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fonte única de verdade para dados de pets/lembretes/peso, isolando as
 * telas do acesso direto ao Room (padrão Repository).
 */
public class PetRepository {

    private final PetDao petDao;
    private final ReminderDao reminderDao;
    private final WeightEntryDao weightEntryDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PetRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        petDao = db.petDao();
        reminderDao = db.reminderDao();
        weightEntryDao = db.weightEntryDao();
    }

    // ---------- Pets ----------
    public LiveData<List<Pet>> observePets() { return petDao.observeAll(); }
    public LiveData<Pet> observePet(long id) { return petDao.observeById(id); }

    public void savePet(Pet pet, double initialWeight, Runnable onSaved) {
        executor.execute(() -> {
            long id = petDao.insert(pet);
            if (initialWeight > 0) {
                long petId = pet.getId() != 0 ? pet.getId() : id;
                weightEntryDao.insert(new WeightEntry(petId, initialWeight, System.currentTimeMillis()));
            }
            if (onSaved != null) onSaved.run();
        });
    }

    public void deletePet(Pet pet) {
        executor.execute(() -> petDao.delete(pet));
    }

    // ---------- Reminders ----------
    public LiveData<List<Reminder>> observeReminders(long petId) { return reminderDao.observeByPet(petId); }
    public LiveData<List<Reminder>> observeUpcomingReminders(int limit) { return reminderDao.observeUpcoming(limit); }

    public void saveReminder(Reminder reminder, Runnable onSaved) {
        executor.execute(() -> {
            reminderDao.insert(reminder);
            if (onSaved != null) onSaved.run();
        });
    }

    public void markReminderDone(Reminder reminder) {
        executor.execute(() -> {
            reminder.setDone(true);
            reminderDao.update(reminder);
        });
    }

    // ---------- Weight history ----------
    public LiveData<List<WeightEntry>> observeWeightHistory(long petId) {
        return weightEntryDao.observeByPet(petId);
    }

    public void addWeightEntry(long petId, double weightKg) {
        executor.execute(() ->
                weightEntryDao.insert(new WeightEntry(petId, weightKg, System.currentTimeMillis())));
    }
}
