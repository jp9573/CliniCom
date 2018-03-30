package cyecoders.clinicom.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cyecoders.clinicom.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private Animation animFadeIn;
    private Animation animTranslate;
    private ImageView logo;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.splashscreenImg);
        text = findViewById(R.id.splashscreenTxt);

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);

        logo.setVisibility(View.VISIBLE);
        logo.setAnimation(animFadeIn);
        text.setAnimation(animTranslate);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
