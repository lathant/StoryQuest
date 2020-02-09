package com.example.autismui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class activity_splash extends AppCompatActivity {

    private Button activity_splash_btn_Here;
    private static int SPLASH_TIME_OUT = 3000;
    private ImageView imageView;
    private AnimationUtils animSlide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity_splash_btn_Here = (Button) findViewById(R.id.button);
        activity_splash_btn_Here.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent( this, activity_splash.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
        // Refer the ImageView like this
        imageView = (ImageView) findViewById(R.id.img);

// Load the animation like this
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.activity_splashslide);

// Start the animation like this
        imageView.startAnimation(animSlide);
        activity_splash_btn_Here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPuzzlePage();
            }
        });
    }

    public void openPuzzlePage(){
        Intent intent = new Intent (getBaseContext(), PuzzlePage.class);
        startActivity(intent);
    }


}
