package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Excursion;

import java.util.List;

public class ExcursionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExcursions;
    private AppDatabase appDatabase;
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);

        recyclerViewExcursions = findViewById(R.id.recyclerViewExcursions);
        appDatabase = AppDatabase.getInstance(this);

        vacationId = getIntent().getIntExtra("VACATION_ID", -1);

        findViewById(R.id.buttonAddExcursion).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExcursionActivity.class);
            intent.putExtra("VACATION_ID", vacationId);
            startActivity(intent);
        });

        recyclerViewExcursions.setLayoutManager(new LinearLayoutManager(this));
        loadExcursions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }

    private void loadExcursions() {
        new Thread(() -> {
            List<Excursion> excursions = appDatabase.excursionDao().getExcursionsForVacation(vacationId);
            runOnUiThread(() -> {
                ExcursionAdapter adapter = new ExcursionAdapter(excursions, this);
                recyclerViewExcursions.setAdapter(adapter);
            });
        }).start();
    }

    public void deleteExcursion(Excursion excursion) {
        new Thread(() -> {
            appDatabase.excursionDao().delete(excursion);
            runOnUiThread(() -> {
                Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                loadExcursions();
            });
        }).start();
    }
}
