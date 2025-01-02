package com.example.d308;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.database.AppDatabase;

import com.example.d308.database.entity.Vacation;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVacations;
    private AppDatabase appDatabase;

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "vacation_notifications";
            CharSequence channelName = "Vacation Notifications";
            String channelDescription = "Notifications for vacation alerts.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        recyclerViewVacations = findViewById(R.id.recyclerViewVacations);
        appDatabase = AppDatabase.getInstance(this);

        findViewById(R.id.buttonCreateVacation).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateVacationActivity.class);
            startActivity(intent);
        });

        recyclerViewVacations.setLayoutManager(new LinearLayoutManager(this));
        loadVacations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }

    private void loadVacations() {
        new Thread(() -> {
            List<Vacation> vacations = appDatabase.vacationDao().getAllVacations();
            runOnUiThread(() -> {
                VacationAdapter vacationAdapter = new VacationAdapter(vacations, this);
                recyclerViewVacations.setAdapter(vacationAdapter);
            });
        }).start();
    }

    public void deleteVacation(Vacation vacation) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the vacation?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        appDatabase.vacationDao().delete(vacation);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Vacation deleted", Toast.LENGTH_SHORT).show();
                            loadVacations();
                        });
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }
   
}