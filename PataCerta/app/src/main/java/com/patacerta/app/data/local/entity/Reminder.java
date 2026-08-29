package com.patacerta.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Lembrete associado a um pet (vacina, remédio ou passeio).
 * Atende RF04 e RF05 do relatório de requisitos da Atividade 1.
 */
@Entity(
        tableName = "reminders",
        foreignKeys = @ForeignKey(
                entity = Pet.class,
                parentColumns = "id",
                childColumns = "petId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = { @Index("petId") }
)
public class Reminder {

    public static final String TYPE_VACCINE = "VACCINE";
    public static final String TYPE_MEDICINE = "MEDICINE";
    public static final String TYPE_WALK = "WALK";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long petId;

    @NonNull
    private String title = "";

    @NonNull
    private String type = TYPE_VACCINE;

    /** Data/hora agendada, em epoch millis (usado pelo AlarmManager/WorkManager). */
    private long dueAtMillis;

    /** "NONE" | "DAILY" | "WEEKLY" | "MONTHLY" | "YEARLY" */
    private String repeatRule = "NONE";

    private String notes;

    private boolean done;

    public Reminder() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPetId() { return petId; }
    public void setPetId(long petId) { this.petId = petId; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getType() { return type; }
    public void setType(@NonNull String type) { this.type = type; }

    public long getDueAtMillis() { return dueAtMillis; }
    public void setDueAtMillis(long dueAtMillis) { this.dueAtMillis = dueAtMillis; }

    public String getRepeatRule() { return repeatRule; }
    public void setRepeatRule(String repeatRule) { this.repeatRule = repeatRule; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}
