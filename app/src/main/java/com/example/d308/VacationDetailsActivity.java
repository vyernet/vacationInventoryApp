package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Excursion;
import com.example.d308.database.entity.Vacation;

import java.util.List;

public class VacationDetailsActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewHotel, textViewStartDate, textViewEndDate;
    private RecyclerView recyclerViewExcursions;

    private AppDatabase appDatabase;
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        textViewTitle = findViewById(R.id.textViewVacationTitle);
        textViewHotel = findViewById(R.id.textViewVacationHotel);
        textViewStartDate = findViewById(R.id.textViewVacationStartDate);
        textViewEndDate = findViewById(R.id.textViewVacationEndDate);
        recyclerViewExcursions = findViewById(R.id.recyclerViewExcursions);

        vacationId = getIntent().getIntExtra("VACATION_ID", -1);
        if (vacationId == -1) {
            finish();
            return;
        }

        appDatabase = AppDatabase.getInstance(this);

        loadVacationDetails();

        loadExcursions();

        findViewById(R.id.buttonBack).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }

    private void loadVacationDetails() {
        new Thread(() -> {
            Vacation vacation = appDatabase.vacationDao().findById(vacationId);
            runOnUiThread(() -> {
                if (vacation != null) {
                    textViewTitle.setText(vacation.getTitle());
                    textViewHotel.setText(vacation.getHotel());
                    textViewStartDate.setText(vacation.getStartDate());
                    textViewEndDate.setText(vacation.getEndDate());
                }
            });
        }).start();
    }
    public void deleteExcursion(Excursion excursion) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this excursion?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        appDatabase.excursionDao().delete(excursion);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                            loadExcursions();
                        });
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadExcursions() {
        new Thread(() -> {
            List<Excursion> excursions = appDatabase.excursionDao().getExcursionsForVacation(vacationId);
            runOnUiThread(() -> {
                ExcursionAdapter adapter = new ExcursionAdapter(excursions, this);
                recyclerViewExcursions.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewExcursions.setAdapter(adapter);
            });
        }).start();
    }
}