package com.example.d308;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Vacation;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.example.d308.utils.VacationAlarmReceiver;
import com.example.d308.validators.DateFormatValidator;
import com.example.d308.validators.DateRangeValidator;

public class CreateVacationActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;
    private final Calendar calendar = Calendar.getInstance();
    private String dateFormat = "MM/dd/yyyy";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation);

        // Request permissions for notification
        requestNotificationPermission();

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
    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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

        scheduleVacationAlerts(title, startDate, endDate);

        new Thread(() -> {
            AppDatabase.getInstance(this).vacationDao()
                    .insert(new Vacation(0, title, hotel, startDate, endDate));
            runOnUiThread(() -> {
                Toast.makeText(this, "Vacation created", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
    private void scheduleVacationAlert(String vacationTitle, String alertMessage, long alertTime) {
        Intent alertIntent = new Intent(this, VacationAlarmReceiver.class);
        alertIntent.putExtra("vacationTitle", vacationTitle);
        alertIntent.putExtra("notificationMessage", alertMessage);

        PendingIntent alertPendingIntent = PendingIntent.getBroadcast(
                this,
                (int) alertTime,
                alertIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, alertPendingIntent);
            Toast.makeText(this, "Notification set: " + alertMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleVacationAlerts(String vacationTitle, String vacationStartDate, String vacationEndDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            Date startDate = dateFormatter.parse(vacationStartDate);
            Date endDate = dateFormatter.parse(vacationEndDate);

            if (startDate != null) {
                scheduleVacationAlert(
                        vacationTitle,
                        "Your vacation '" + vacationTitle + "' starts today!",
                        startDate.getTime()
                );
            }

            if (endDate != null) {
                scheduleVacationAlert(
                        vacationTitle,
                        "Your vacation '" + vacationTitle + "' ends today!",
                        endDate.getTime()
                );
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please enter valid dates in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show();
        }
    }
}
