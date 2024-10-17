package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton art_gallery, WWI, space, visual_show;
    boolean isArtGallerySelected = false;
    boolean isWWISelected = false;
    boolean isSpaceSelected = false;
    boolean isVisualShowSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        art_gallery = findViewById(R.id.art_gallery_btn);
        WWI = findViewById(R.id.wwi_btn);
        space = findViewById(R.id.space_btn);
        visual_show = findViewById(R.id.visual_show_btn);

        art_gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Reset all boolean variables to false
                isArtGallerySelected = true;
                isWWISelected = false;
                isSpaceSelected = false;
                isVisualShowSelected = false;


                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("isArtGallerySelected", isArtGallerySelected);
                startActivity(intent);
            }
        });

        WWI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isArtGallerySelected = false;
                isWWISelected = true;
                isSpaceSelected = false;
                isVisualShowSelected = false;


                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("isWWISelected", isWWISelected);
                startActivity(intent);
            }
        });

        space.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isArtGallerySelected = false;
                isWWISelected = false;
                isSpaceSelected = true;
                isVisualShowSelected = false;


                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("isSpaceSelected", isSpaceSelected);
                startActivity(intent);
            }
        });

        visual_show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isArtGallerySelected = false;
                isWWISelected = false;
                isSpaceSelected = false;
                isVisualShowSelected = true;


                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("isVisualShowSelected", isVisualShowSelected);
                startActivity(intent);

            }
        });

    }

}