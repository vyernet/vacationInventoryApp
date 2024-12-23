package com.example.d308;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.d308.database.AppDatabase;

import com.example.d308.database.entity.Vacation;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation);

        editTextTitle = findViewById(R.id.editTextVacationTitle);
        Button savedButton = findViewById(R.id.buttonSaveVacation);
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVacation();
            }
        });
    }

    private void saveVacation() {
        String title = editTextTitle.getText().toString();
        String hotel = "";
        String startDate = "";
        String endDate = "";
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