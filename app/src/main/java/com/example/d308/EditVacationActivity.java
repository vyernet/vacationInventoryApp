package com.example.d308;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308.database.AppDatabase;

import com.example.d308.database.entity.Vacation;

public class EditVacationActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;
    private int vacationId;
    private AppDatabase appDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vacation);

        editTextTitle = findViewById(R.id.editTextVacationTitleDetails);
        editTextHotel = findViewById(R.id.editTextVacationHotelDetails);
        editTextStartDate = findViewById(R.id.editTextVacationStartDateDetails);
        editTextEndDate = findViewById(R.id.editTextVacationEndDateDetails);
        Button buttonSaveChanges = findViewById(R.id.buttonSaveChangesVacation);
        appDatabase = AppDatabase.getInstance(this);

        vacationId = getIntent().getIntExtra("VACATION_ID", -1);

        loadVacationData();

        buttonSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void loadVacationData() {
        new Thread(() -> {
            Vacation vacation = appDatabase.vacationDao().findById(vacationId);
            if (vacation != null) {
                runOnUiThread(() -> {
                    editTextTitle.setText(vacation.getTitle());
                    editTextHotel.setText(vacation.getHotel());
                    editTextStartDate.setText(vacation.getStartDate());
                    editTextEndDate.setText(vacation.getEndDate());
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Vacation not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }

    private void saveChanges() {
        String title = editTextTitle.getText().toString().trim();
        String hotel = editTextHotel.getText().toString().trim();
        String startDate = editTextStartDate.getText().toString().trim();
        String endDate = editTextEndDate.getText().toString().trim();

        if (title.isEmpty() || hotel.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Vacation vacation = new Vacation(vacationId, title, hotel, startDate, endDate);
            appDatabase.vacationDao().update(vacation);
            runOnUiThread(() -> {
                Toast.makeText(this, "Vacation updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
