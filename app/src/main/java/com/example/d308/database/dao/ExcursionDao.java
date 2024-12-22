package com.example.d308.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.d308.database.entity.Excursion;

import java.util.List;

public interface ExcursionDao {
    @Insert
    void insert(Excursion vacation);

    @Update
    void update(Excursion vacation);

    @Delete
    void delete(Excursion vacation);

    @Query("SELECT * FROM excursions")
    List<Excursion> getAllExcursions();
}
