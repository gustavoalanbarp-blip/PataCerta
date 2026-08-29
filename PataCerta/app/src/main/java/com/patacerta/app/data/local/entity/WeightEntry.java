package com.patacerta.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Registro histórico de peso de um pet, usado para desenhar o gráfico de evolução (RF06).
 */
@Entity(
        tableName = "weight_entries",
        foreignKeys = @ForeignKey(
                entity = Pet.class,
                parentColumns = "id",
                childColumns = "petId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = { @Index("petId") }
)
public class WeightEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long petId;
    private double weightKg;
    private long recordedAtMillis;

    public WeightEntry() {}

    public WeightEntry(long petId, double weightKg, long recordedAtMillis) {
        this.petId = petId;
        this.weightKg = weightKg;
        this.recordedAtMillis = recordedAtMillis;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPetId() { return petId; }
    public void setPetId(long petId) { this.petId = petId; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public long getRecordedAtMillis() { return recordedAtMillis; }
    public void setRecordedAtMillis(long recordedAtMillis) { this.recordedAtMillis = recordedAtMillis; }
}
