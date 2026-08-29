package com.patacerta.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Representa um pet cadastrado pelo tutor.
 * Persistido localmente via Room (RF02 do relatório de requisitos).
 */
@Entity(tableName = "pets")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name = "";
    private String species;      // "Cachorro" | "Gato"
    private String breed;        // preenchido via TheDogAPI quando espécie = Cachorro
    private String sex;          // "Macho" | "Fêmea"
    private String birthDate;    // ISO-8601 (yyyy-MM-dd)
    private String photoUrl;     // URL retornada pela DogAPI ou foto local
    private double weightKg;

    public Pet() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
}
