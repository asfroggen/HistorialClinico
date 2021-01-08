package com.example.historialclinico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN=4500;

    Animation top,bot;
    ImageView ivLogo,ivBottom;
    TextView tvTitulo,tvDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        top= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        bot=AnimationUtils.loadAnimation(this,R.anim.fade_in);

        ivLogo=findViewById(R.id.ivLogo);
        ivBottom=findViewById(R.id.ivBottom);
        tvTitulo=findViewById(R.id.tvTitulo);
        tvDescripcion=findViewById(R.id.tvDescripcion);

        ivLogo.setAnimation(top);
        ivBottom.setAnimation(bot);
        tvTitulo.setAnimation(top);
        tvDescripcion.setAnimation(top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}