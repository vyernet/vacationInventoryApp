package com.example.d308;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Excursion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditExcursionActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDate;
    private Button buttonSaveChanges;
    private Calendar calendar;
    private AppDatabase appDatabase;
    private int excursionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_excursion);

        editTextTitle = findViewById(R.id.editTextExcursionTitle);
        editTextDate = findViewById(R.id.editTextExcursionDate);
        buttonSaveChanges = findViewById(R.id.buttonSaveExcursionChanges);
        calendar = Calendar.getInstance();
        appDatabase = AppDatabase.getInstance(this);

        excursionId = getIntent().getIntExtra("EXCURSION_ID", -1);

        if (excursionId == -1) {
            Toast.makeText(this, "Invalid Excursion ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExcursionDetails();

        editTextDate.setOnClickListener(v -> {
            new DatePickerDialog(EditExcursionActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextDate.setText(formattedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        buttonSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void loadExcursionDetails() {
        new Thread(() -> {
            Excursion excursion = appDatabase.excursionDao().getExcursionById(excursionId);
            if (excursion != null) {
                runOnUiThread(() -> {
                    editTextTitle.setText(excursion.getTitle());
                    editTextDate.setText(excursion.getDate());
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Excursion not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }

    private void saveChanges() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Excursion excursion = new Excursion(excursionId, title, date);
            appDatabase.excursionDao().update(excursion);

            runOnUiThread(() -> {
                Toast.makeText(this, "Excursion updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}