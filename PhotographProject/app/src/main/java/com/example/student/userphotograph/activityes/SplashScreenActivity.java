package com.example.student.userphotograph.activityes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.utilityes.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setLocale();
        ImageView splashScreen = (ImageView) findViewById(R.id.splash_screen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        splashScreen.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                goToActivity();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void setLocale() {
        SharedPreferences shared = getSharedPreferences("localization", MODE_PRIVATE);
        String language = shared.getString(Constants.DEFAULT_LANGUAGE, "");
        if(TextUtils.isEmpty(language)) {
            shared.edit().putString(Constants.DEFAULT_LANGUAGE, Locale.getDefault().getLanguage()).apply();
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void goToActivity() {
        finish();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if(user != null){
            intent= new Intent(this, HomeActivity.class);
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        finish();
        startActivity(intent);
    }

}
