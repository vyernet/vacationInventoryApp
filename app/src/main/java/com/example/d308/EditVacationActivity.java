package com.example.d308;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308.database.AppDatabase;

import com.example.d308.database.entity.Vacation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.example.d308.validators.DateFormatValidator;
import com.example.d308.validators.DateRangeValidator;

public class EditVacationActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;
    private int vacationId;
    private AppDatabase appDatabase;
    private String dateFormat = "MM/dd/yyyy";

    private final Calendar calendar = Calendar.getInstance();

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


        editTextStartDate.setOnClickListener(view -> {
            new DatePickerDialog(EditVacationActivity.this, (DatePicker datePicker, int year, int month, int dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextStartDate.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editTextEndDate.setOnClickListener(view -> {
            new DatePickerDialog(EditVacationActivity.this, (DatePicker datePicker, int year, int month, int dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextEndDate.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        findViewById(R.id.buttonBack).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

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

        DateFormatValidator formatValidator = new DateFormatValidator(dateFormat);
        if (!formatValidator.isValid(startDate) || !formatValidator.isValid(endDate)) {
            Toast.makeText(this, formatValidator.getErrorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        DateRangeValidator rangeValidator = new DateRangeValidator(dateFormat);
        if (!rangeValidator.isValid(new String[]{startDate, endDate})) {
            Toast.makeText(this, rangeValidator.getErrorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

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
