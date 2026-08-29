package com.patacerta.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.patacerta.app.data.local.entity.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Query("SELECT * FROM reminders WHERE petId = :petId ORDER BY dueAtMillis ASC")
    LiveData<List<Reminder>> observeByPet(long petId);

    @Query("SELECT * FROM reminders ORDER BY dueAtMillis ASC")
    LiveData<List<Reminder>> observeAll();

    @Query("SELECT * FROM reminders WHERE done = 0 ORDER BY dueAtMillis ASC LIMIT :limit")
    LiveData<List<Reminder>> observeUpcoming(int limit);

    @Query("SELECT * FROM reminders WHERE id = :id LIMIT 1")
    Reminder getByIdSync(long id);
}
