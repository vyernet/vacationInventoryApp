package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
    private Button buttonAddExcursion;
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
        buttonAddExcursion = findViewById(R.id.buttonAddExcursion);

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

        buttonAddExcursion.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExcursionActivity.class);
            intent.putExtra("VACATION_ID", vacationId);
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