package com.example.d308.database.repository;
import com.example.d308.database.AppDatabase;
import com.example.d308.database.dao.VacationDao;
import com.example.d308.database.entity.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;


public class VacationRepository {
    private final VacationDao vacationDao;
    private final ExecutorService executorService;

    public VacationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        vacationDao = db.vacationDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Vacation vacation) {
        executorService.execute(() -> vacationDao.insert(vacation));
    }

    public void update(Vacation vacation) {
        executorService.execute(() -> vacationDao.update(vacation));
    }

    public void delete(Vacation vacation) {
        executorService.execute(() -> vacationDao.delete(vacation));
    }

    public List<Vacation> getAllVacations() {
        return vacationDao.getAllVacations();
    }
}
