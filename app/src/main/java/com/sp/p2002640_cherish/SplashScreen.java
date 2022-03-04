package com.sp.p2002640_cherish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    Animation animation;
    TextToSpeech texttospeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        String speech="Cherish your food, save the Earth!";
        imageView = findViewById(R.id.saveurfood);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        texttospeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    texttospeech.setLanguage(Locale.UK);
                }
                texttospeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}