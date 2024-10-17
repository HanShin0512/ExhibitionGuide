package com.example.exhibitionguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Booking extends AppCompatActivity {

    Button done;
    SeekBar people_seekbar;
    TextView people_text;
    ImageView people_image, sun;
    Spinner spinner;
    CalendarView calendar;

    //schedule
    List<String> time = Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
            "18:00", "18:30", "19:00", "19:30");

    List<String> visualShowTime = Arrays.asList("15:00", "17:00", "19:00");
    List<String> timesToShow = new ArrayList<>(time);
    List<String> timesToShowVS = new ArrayList<>(visualShowTime);

    boolean isInPast = false;
    Intent intent;

    //initialise the values to send to the next layout
    int currentProgress = 1;
    Calendar currentDate = Calendar.getInstance();
    int Day = currentDate.get(Calendar.DAY_OF_MONTH);
    int Month = currentDate.get(Calendar.MONTH) + 1;
    int Year = currentDate.get(Calendar.YEAR);

    String selectedItem;

    //check today's date when no date is selected
    int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
    boolean isWeekDay = (dayOfWeek != Calendar.SATURDAY ) && (dayOfWeek != Calendar.SUNDAY);
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //assign to their ids
        people_seekbar = findViewById(R.id.people_seekbar);
        people_text = findViewById(R.id.people_text);
        people_image = findViewById(R.id.people_image);
        spinner = findViewById(R.id.spinner);
        sun = findViewById(R.id.sun);
        calendar = findViewById(R.id.calendarView2);
        done = findViewById(R.id.done);

        // Retrieve boolean values from intent extras
        boolean isArtGallerySelected = getIntent().getBooleanExtra("isArtGallerySelected", false);
        boolean isWWISelected = getIntent().getBooleanExtra("isWWISelected", false);
        boolean isSpaceSelected = getIntent().getBooleanExtra("isSpaceSelected", false);
        boolean isVisualShowSelected = getIntent().getBooleanExtra("isVisualShowSelected", false);

        //done with booking
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String localTime = LocalTime.now().toString().substring(0,5);
                if(timesToShow.contains("Unavailable") || timesToShowVS.contains("Unavailable")){
                    alertPast();
                } else if((Day == currentDate.get(Calendar.DAY_OF_MONTH)) &&
                        (localTime.compareTo(selectedItem) > 0)) {
                    new AlertDialog.Builder(Booking.this)
                            .setTitle("Invalid Time")
                            .setMessage("Sorry, the exhibition has already started while you were booking")
                            .setPositiveButton("OK", null)
                            .show();
                }else {
                    intent = new Intent(Booking.this, Ticket.class);
                    intent.putExtra("isArtGallerySelected", isArtGallerySelected);
                    intent.putExtra("isWWISelected", isWWISelected);
                    intent.putExtra("isSpaceSelected", isSpaceSelected);
                    intent.putExtra("isVisualShowSelected", isVisualShowSelected);
                    intent.putExtra("Day", Day);
                    intent.putExtra("Month", Month);
                    intent.putExtra("Year", Year);
                    intent.putExtra("currentProgress", currentProgress);
                    intent.putExtra("currentSpinner", selectedItem);
                    intent.putExtra("isWeekDay", isWeekDay);
                    startActivity(intent);
                }
            }
        });

        //calendar
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentDate = Calendar.getInstance();
                Year = year;
                Month = month + 1;
                Day = dayOfMonth;

                if (year < currentDate.get(Calendar.YEAR) ||
                        (year == currentDate.get(Calendar.YEAR) && month < currentDate.get(Calendar.MONTH)) ||
                        (year == currentDate.get(Calendar.YEAR) && month == currentDate.get(Calendar.MONTH) && dayOfMonth < currentDate.get(Calendar.DAY_OF_MONTH))) {
                    isInPast = true;
                } else {
                    isInPast = false;
                }

                // Update spinner based on selected date
                updateSpinner(isVisualShowSelected);

                //check if selected day is weekday or not
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month); // Note: month is zero-based
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                isWeekDay = (dayOfWeek != Calendar.SATURDAY) && (dayOfWeek != Calendar.SUNDAY);
            }
        });

        //seekbar
        people_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                people_text.setVisibility(View.VISIBLE);
                people_text.setText(String.valueOf(progress));
                if (progress >= 3 && progress < 6) {
                    people_image.setImageResource(R.drawable.threeppl);
                } else if (progress >= 6) {
                    people_image.setImageResource(R.drawable.group);
                } else {
                    people_image.setImageResource(R.drawable.oneppl);
                }
                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateSpinner(isVisualShowSelected);

        //spinner
        if (isVisualShowSelected) {

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem.equals("19:00")) {
                        sun.setImageResource(R.drawable.night);
                    } else {
                        sun.setImageResource(R.drawable.sun);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else { //any other exhibitions

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem.equals("18:00") || selectedItem.equals("18:30") ||
                            selectedItem.equals("19:00") || selectedItem.equals("19:30")) {
                        sun.setImageResource(R.drawable.night);
                    } else {
                        sun.setImageResource(R.drawable.sun);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    }

    private List<String> getFutureTimes(List<String> times) {
        List<String> futureTimes = new ArrayList<>();
        String currentTime = LocalTime.now().toString().substring(0, 5); // Get current time string in format HH:mm

        for (String timeString : times) {
            if (timeString.compareTo(currentTime) > 0) {
                futureTimes.add(timeString);
            }
        }

        // Check if futureTimes list is empty
        if (futureTimes.isEmpty()) {
            futureTimes.add("Unavailable");
        }

        return futureTimes;
    }

    private void updateSpinner(boolean isVisualShowSelected) {
        timesToShow = new ArrayList<>(time);
        timesToShowVS = new ArrayList<>(visualShowTime);

       if(Day == currentDate.get(Calendar.DAY_OF_MONTH)){
           timesToShow = getFutureTimes(timesToShow);
           timesToShowVS = getFutureTimes(timesToShowVS);
       }

        if (isInPast) {
            timesToShow.clear();
            timesToShow.add("Unavailable");
            timesToShowVS.clear();
            timesToShowVS.add("Unavailable");
        }

        // other exhibit
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timesToShow);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //visual exhibit
        if (isVisualShowSelected) {
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, timesToShowVS);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter2);
        }

    }

    private void alertPast(){

            new AlertDialog.Builder(Booking.this)
                    .setTitle("Invalid Time")
                    .setMessage("Exhibitions for this date is unavailable")
                    .setPositiveButton("OK", null)
                    .show();
    }


}