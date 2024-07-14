package com.hamedmajdi.drugreminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewDrug extends AppCompatActivity {

    private EditText edDrugName;
    private TextView edDrugTime;
    private EditText edDrugQuantity;
    private String selectedTime = "";

    Button saveButton;
    AppDatabase appDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_drug);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "drug-database").build();


        LocalDate today = LocalDate.now();
        Log.d("Alarm", "today: " + today);

        edDrugQuantity = findViewById(R.id.editTextQuantity);
        edDrugName = findViewById(R.id.editTextDrugName);
        edDrugTime = findViewById(R.id.editTextTime);
        saveButton = findViewById(R.id.button);

        edDrugTime.setOnClickListener(v -> {
            //write a code to open time picker dialog
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewDrug.this, (view, hourOfDay, minute1) -> {

                String min = String.valueOf(minute1);
                if (min.length() == 1) {
                    min = "0" + min;
                }
                String hour1 = String.valueOf(hourOfDay);
                if (hour1.length() == 1) {
                    hour1 = "0" + hour1;
                }

                String time = hour1 + ":" + min;
                selectedTime = time;
                edDrugTime.setText(time);
            }, hour, minute, true);

            timePickerDialog.show();
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDrug(getApplicationContext());
            }
        });

    }

    private void saveDrug(Context context) {
        //first check if the fields are empty
        if (edDrugName.getText().toString().isEmpty() || edDrugTime.getText().toString().isEmpty() || edDrugQuantity.getText().toString().isEmpty()) {
            Toast.makeText(this, "لطفا تمام موارد را وارد کنید", Toast.LENGTH_SHORT).show();

            //        ############################################################
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, DrugReminderReceiver.class);
            alarmIntent.putExtra("drugName", "test"); // Pass drug name as an extra
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



            long timesss = System.currentTimeMillis() + (1000*10);

            // Set the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timesss, pendingIntent);

            Log.d("Alarm", "Alarm set for drug: " + " at " + 10);
//        ############################################################



        } else {
            String drugName = edDrugName.getText().toString();
            String drugTime = edDrugTime.getText().toString();
            Integer drugQuantity = Integer.parseInt(edDrugQuantity.getText().toString());

            DrugDataClass drug = new DrugDataClass(drugName, drugTime, drugQuantity);


//        ############################################################
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, DrugReminderReceiver.class);
            alarmIntent.putExtra("drugName", drug.drugName); // Pass drug name as an extra
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) drug.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // Parse the drug time (e.g., "10:00") to milliseconds
            long drugTimeMillis = parseDrugTimeToMillis(drug.drugTime);
            long timesss = System.currentTimeMillis() + (1000*10);

            Log.d("Alarm", "Alarm set for drug: " + " at " + System.currentTimeMillis());
            Log.d("Alarm", "Alarm set for drug: " + " at " + drugTimeMillis);
            Log.d("Alarm", "Alarm set for drug: " + " at " + timesss);

            // Set the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, drugTimeMillis, pendingIntent);

            Log.d("Alarm", "Alarm set for drug: " + drug.drugName + " at " + drug.drugTime);
//        ############################################################
            new InsertDrugTask().execute(drug);
            finish();
        }
    }

    private class InsertDrugTask extends AsyncTask<DrugDataClass, Void, Void> {
        @Override
        protected Void doInBackground(DrugDataClass... drugs) {
            appDatabase.drugDao().insert(drugs[0]);
            return null;
        }
    }


    private long parseDrugTimeToMillis(String drugTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
//        try {
//
//            Date date = sdf.parse(drugTime);
//            return date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return 0; // Handle parsing error
//        }

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateString = dateFormat.format(calendar.getTime());

        // Combine today's date with the time string
        String dateTimeString = todayDateString + " " + drugTime;

        // Define the full date and time format
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            // Parse the combined date and time string into a Date object
            Date dateTime = dateTimeFormat.parse(dateTimeString);

            // Get the milliseconds from the Date object
            long milliseconds = dateTime.getTime();

            return milliseconds;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle parsing error
        }


    }
}