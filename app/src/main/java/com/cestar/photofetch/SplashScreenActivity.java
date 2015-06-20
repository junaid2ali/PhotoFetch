package com.cestar.photofetch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    Handler splashHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashHandler.postDelayed(waitRunnable, 3000);
    }

    Runnable waitRunnable = new Runnable() {
        @Override
        public void run() {
                Intent in = new Intent(SplashScreenActivity.this, MainSwipeActivity.class);
                startActivity(in);
        }
    };



}
