package com.example.d308;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Excursion;
import com.example.d308.utils.ExcursionAlarmReceiver;
import com.example.d308.validators.ExcursionDateValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

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
        int vacationId = getIntent().getIntExtra("VACATION_ID", -1);

        if (excursionId == -1) {
            Toast.makeText(this, "Invalid Excursion ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (excursionId == -1 || vacationId == -1) {
            Toast.makeText(this, "Invalid Excursion or Vacation ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExcursionDetails();


        findViewById(R.id.buttonBack).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

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

    private void scheduleExcursionAlert(String excursionTitle, long excursionDateMillis) {
        Intent intent = new Intent(this, ExcursionAlarmReceiver.class);
        intent.putExtra("EXCURSION_TITLE", excursionTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) excursionDateMillis, // Unique request code
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, excursionDateMillis, pendingIntent);
        }
    }

    private void saveChanges() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        String vacationStartDate = getIntent().getStringExtra("VACATION_START_DATE");
        String vacationEndDate = getIntent().getStringExtra("VACATION_END_DATE");

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int vacationId = getIntent().getIntExtra("VACATION_ID", -1);

        if (vacationId == -1) {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
            return;
        }

        ExcursionDateValidator validator = new ExcursionDateValidator("MM/dd/yyyy");
        if (!validator.isValid(vacationStartDate, vacationEndDate, date)) {
            Toast.makeText(this, validator.getErrorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        long formattedDate = 0;

        try {
            formattedDate = Objects.requireNonNull(dateFormat.parse(date)).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        scheduleExcursionAlert(title, formattedDate);

        new Thread(() -> {
            Excursion excursion = new Excursion(excursionId, title, date, vacationId);
            appDatabase.excursionDao().update(excursion);

            runOnUiThread(() -> {
                Toast.makeText(this, "Excursion updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();


    }
}