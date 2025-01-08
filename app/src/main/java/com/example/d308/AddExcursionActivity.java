package com.example.d308;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class AddExcursionActivity extends AppCompatActivity {

    private EditText editTextExcursionTitle, editTextExcursionDate;
    private Button buttonSaveExcursion;
    private Calendar calendar = Calendar.getInstance();
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_excursion);

        editTextExcursionTitle = findViewById(R.id.editTextExcursionTitle);
        editTextExcursionDate = findViewById(R.id.editTextExcursionDate);
        buttonSaveExcursion = findViewById(R.id.buttonSaveExcursion);

        vacationId = getIntent().getIntExtra("VACATION_ID", -1);


        findViewById(R.id.buttonBack).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        editTextExcursionDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextExcursionDate.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        buttonSaveExcursion.setOnClickListener(v -> saveExcursion());
    }

    private void saveExcursion() {
        String title = editTextExcursionTitle.getText().toString().trim();
        String date = editTextExcursionDate.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title for the excursion", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date for the excursion", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase database = AppDatabase.getInstance(this);
            Excursion excursion = new Excursion();
            excursion.setTitle(title);
            excursion.setDate(date);
            excursion.setVacationId(vacationId);

            database.excursionDao().insert(excursion);

            runOnUiThread(() -> {
                Toast.makeText(this, "Excursion added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
