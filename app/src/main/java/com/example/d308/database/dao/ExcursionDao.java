package com.example.d308.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.d308.database.entity.Excursion;
import com.example.d308.database.entity.Vacation;

import java.util.List;

@Dao
public interface ExcursionDao {
    @Insert
    void insert(Excursion vacation);

    @Update
    void update(Excursion vacation);

    @Delete
    void delete(Excursion vacation);

    @Query("SELECT * FROM excursions")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    List<Excursion> getExcursionsForVacation(int vacationId);
    @Query("SELECT * FROM excursions WHERE id = :id")
    Excursion getExcursionById(int id);

    @Query("SELECT * FROM vacations WHERE id = (SELECT vacationId FROM excursions WHERE id = :excursionId)")
    Vacation getVacationForExcursion(int excursionId);

}
