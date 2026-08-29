package com.patacerta.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.patacerta.app.data.local.entity.WeightEntry;

import java.util.List;

@Dao
public interface WeightEntryDao {

    @Insert
    long insert(WeightEntry entry);

    @Query("SELECT * FROM weight_entries WHERE petId = :petId ORDER BY recordedAtMillis ASC")
    LiveData<List<WeightEntry>> observeByPet(long petId);
}
