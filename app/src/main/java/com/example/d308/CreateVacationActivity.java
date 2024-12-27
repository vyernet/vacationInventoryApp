package com.example.d308;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.d308.database.AppDatabase;
import com.example.d308.database.entity.Vacation;

public class CreateVacationActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vacation);

        editTextTitle = findViewById(R.id.editTextVacationTitleDetails);
        editTextHotel = findViewById(R.id.editTextVacationHotelDetails);
        editTextStartDate = findViewById(R.id.editTextVacationStartDateDetails);
        editTextEndDate = findViewById(R.id.editTextVacationEndDateDetails);

        findViewById(R.id.buttonSaveChangesVacation).setOnClickListener(v -> saveVacation());
    }

    private void saveVacation() {
        String title = editTextTitle.getText().toString();
        String hotel = editTextHotel.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

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
