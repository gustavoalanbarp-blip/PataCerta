package com.patacerta.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.patacerta.app.data.local.entity.Pet;

import java.util.List;

@Dao
public interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("SELECT * FROM pets ORDER BY id DESC")
    LiveData<List<Pet>> observeAll();

    @Query("SELECT * FROM pets ORDER BY id DESC")
    List<Pet> getAllSync();

    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    LiveData<Pet> observeById(long petId);

    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    Pet getByIdSync(long petId);

    @Query("SELECT COUNT(*) FROM pets")
    int count();
}
