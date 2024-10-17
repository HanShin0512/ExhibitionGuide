package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Ticket extends AppCompatActivity {

    ImageView ticket1, ticket2;
    TextView ppl_amount1, ppl_amount2, price1, price2, dateTextView;
    Button edit, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // Retrieve boolean values from intent extras
        boolean isArtGallerySelected = getIntent().getBooleanExtra("isArtGallerySelected", false);
        boolean isWWISelected = getIntent().getBooleanExtra("isWWISelected", false);
        boolean isSpaceSelected = getIntent().getBooleanExtra("isSpaceSelected", false);
        boolean isVisualShowSelected = getIntent().getBooleanExtra("isVisualShowSelected", false);

        //assign ids
        ticket1 = findViewById(R.id.Ticket1);
        ticket2 = findViewById(R.id.Ticket2);
        ppl_amount1 = findViewById(R.id.ppl_amount);
        ppl_amount2 = findViewById(R.id.ppl_amount2);
        price1 = findViewById(R.id.price);
        price2 = findViewById(R.id.price2);
        dateTextView = findViewById(R.id.date);
        edit = findViewById(R.id.edit);
        done = findViewById(R.id.done);

        //change ticket img
        if(isArtGallerySelected){
            ticket1.setImageResource(R.drawable.artticket1);
            ticket2.setImageResource(R.drawable.artticket2);
        } else if(isWWISelected){
            ticket1.setImageResource(R.drawable.wwiticket1);
            ticket2.setImageResource(R.drawable.wwiticket2);
        } else if(isSpaceSelected){
            ticket1.setImageResource(R.drawable.spaceticket1);
            ticket2.setImageResource(R.drawable.spaceticket2);
        } else if(isVisualShowSelected){
            ticket1.setImageResource(R.drawable.visualshowticket1);
            ticket2.setImageResource(R.drawable.visualshowticket2);
        }

        //change amount of people
        int ppl = getIntent().getIntExtra("currentProgress", 1);
        ppl_amount1.setText("      " + ppl + "\nVISITORS");
        ppl_amount2.setText("      " + ppl + "\nVISITORS");

        // Change date and time
        int currentDay = getIntent().getIntExtra("Day", -1);
        int currentMonth = getIntent().getIntExtra("Month", -1);
        int currentYear = getIntent().getIntExtra("Year", -1);

        // Create a LocalDate object with the given day, month, and year
        LocalDate date = LocalDate.of(currentYear, currentMonth, currentDay);

        // Format the date to display the day of the week
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH);
        String dayOfWeek = date.format(formatter);

        //get start time and end time
        String selectedTime = getIntent().getStringExtra("currentSpinner");
        String[] selectedTimeHour = selectedTime.split(":");
        String hour = selectedTimeHour[0];
        int selectedHour = Integer.parseInt(hour);
        int endTime = selectedHour + 2;

        dateTextView.setText(dayOfWeek + " " + currentDay + ", " + currentMonth + ", " +
                currentYear + "\n" + selectedTime + " - " + endTime + ":00");

        //check day
        boolean isWeekDay = getIntent().getBooleanExtra("isWeekDay", true);
        //calculate the price
        double price = 0;
        if(isArtGallerySelected){
            if(isWeekDay){
                price = 25;
            } else {
                price = 30;
            }
        } else if(isWWISelected){
            if(isWeekDay){
                price = 20;
            } else {
                price = 25;
            }
        } else if(isSpaceSelected){
            if(isWeekDay){
                price = 30;
            } else {
                price = 35;
            }
        } else if(isVisualShowSelected){
            if(isWeekDay){
                price = 40;
            } else {
                price = 45;
            }
        }

        if(ppl >= 4) {
            double total = price * ppl;
            total = total - (total * 0.1);
            price1.setText(total + "$");
            price2.setText(total + "$");
        } else {
            double total = price * ppl;
            price1.setText(total + "$");
            price2.setText(total + "$");
        }

        //edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ticket.this, Booking.class);
                intent.putExtra("isArtGallerySelected", isArtGallerySelected);
                intent.putExtra("isWWISelected", isWWISelected);
                intent.putExtra("isSpaceSelected", isSpaceSelected);
                intent.putExtra("isVisualShowSelected", isVisualShowSelected);
                startActivity(intent);
            }
        });

        //done button
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Ticket.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}