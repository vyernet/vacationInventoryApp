package com.example.d308;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Vacation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.example.d308.validators.DateFormatValidator;
import com.example.d308.validators.DateRangeValidator;

public class CreateVacationActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;
    private final Calendar calendar = Calendar.getInstance();
    private String dateFormat = "MM/dd/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation);

        editTextTitle = findViewById(R.id.addTextVacationTitleDetails);
        editTextHotel = findViewById(R.id.addTextVacationHotelDetails);
        editTextStartDate = findViewById(R.id.addTextVacationStartDateDetails);
        editTextEndDate = findViewById(R.id.addTextVacationEndDateDetails);

        findViewById(R.id.buttonSaveVacation).setOnClickListener(v -> saveVacation());

        editTextStartDate.setOnClickListener(view -> {
            new DatePickerDialog(CreateVacationActivity.this, (DatePicker datePicker, int year, int month, int dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextStartDate.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editTextEndDate.setOnClickListener(view -> {
            new DatePickerDialog(CreateVacationActivity.this, (DatePicker datePicker, int year, int month, int dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
                editTextEndDate.setText(formattedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void saveVacation() {
        String title = editTextTitle.getText().toString();
        String hotel = editTextHotel.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

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

        new Thread(() -> {
            AppDatabase.getInstance(this).vacationDao()
                    .insert(new Vacation(0, title, hotel, startDate, endDate));
            runOnUiThread(() -> {
                Toast.makeText(this, "Vacation created", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
