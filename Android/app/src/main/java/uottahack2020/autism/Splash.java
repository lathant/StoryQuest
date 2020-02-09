package uottahack2020.autism;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    private Button button;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = (Button) findViewById(R.id.splash_btnEnter);
        image = (ImageView) findViewById(R.id.logo);
        button.setAlpha(0);
        new Handler().postDelayed(() -> {
            Animation logo_trans = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-100);
            logo_trans.setDuration(2000);
            logo_trans.setFillAfter(true);
            button.animate().alpha(1.0f).setDuration(2000).start();
            image.startAnimation(logo_trans);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transIntent = new Intent(Splash.this, MainActivity.class);
                    startActivity(transIntent);
                    overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_top_out);
                }
            });
            Intent transIntent = new Intent(Splash.this, MainActivity.class);
            startActivity(transIntent);
            overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_top_out);
        }, SPLASH_TIME_OUT);


    }

}
