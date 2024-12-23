package com.example.d308;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import com.example.d308.database.AppDatabase;

import com.example.d308.database.entity.Vacation;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextHotel;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation);

        editTextTitle = findViewById(R.id.editTextVacationTitle);
        editTextHotel = findViewById(R.id.editTextVacationHotel);
        editTextStartDate = findViewById(R.id.editTextVacationStartDate);
        editTextEndDate = findViewById(R.id.editTextVacationEndDate);
        Button savedButton = findViewById(R.id.buttonSaveVacation);
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));

        // Date picker for End Date
        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVacation();
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    editText.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveVacation() {
        String title = editTextTitle.getText().toString();
        String hotel = editTextHotel.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();
        new InsertTask(AppDatabase.getInstance(this), new Vacation(0, title, hotel, startDate, endDate)).execute();
    }

    private static class InsertTask extends AsyncTask<Vacation, Void, Void> {
        private AppDatabase db;
        private Vacation vacation;

        InsertTask(AppDatabase db, Vacation vacation) {
            this.db = db;
            this.vacation = vacation;
        }

        @Override
        protected Void doInBackground(Vacation... vacations) {
            db.vacationDao().insert(vacation);
            return null;
        }
    }
   
}