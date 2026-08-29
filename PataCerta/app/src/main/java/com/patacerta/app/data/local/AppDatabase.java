package com.patacerta.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.patacerta.app.data.local.entity.Pet;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.data.local.entity.WeightEntry;

/**
 * Banco de dados local (SQLite via Room). Garante que o app funcione mesmo
 * offline (RF12) — pets e lembretes ficam disponíveis sem conexão, e a
 * sincronização com serviços remotos acontece apenas nos pontos que
 * dependem de API (login, busca de raças e localizador de clínicas).
 */
@Database(
        entities = { Pet.class, Reminder.class, WeightEntry.class },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract PetDao petDao();
    public abstract ReminderDao reminderDao();
    public abstract WeightEntryDao weightEntryDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "patacerta.db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
