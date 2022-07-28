package com.example.birdgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private boolean isMute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MainActivity.this,GameActivity.class));
            }
        });
        TextView textView=findViewById(R.id.highscore);
        final SharedPreferences shared = getSharedPreferences("Game", Context.MODE_PRIVATE);
        textView.setText( "HighScore "+shared.getInt("highScore",0));
        isMute= shared.getBoolean("isMute",false);
        final ImageView imageView=findViewById(R.id.volumecontrol);
        if(isMute)
            imageView.setImageResource(R.drawable.volume_off);
        else
            imageView.setImageResource(R.drawable.volume_up);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute= !isMute;
                if(isMute)
                    imageView.setImageResource(R.drawable.volume_off);
                else
                    imageView.setImageResource(R.drawable.volume_up);
                SharedPreferences.Editor editor=shared.edit();
                editor.putBoolean("isMute",isMute);
                editor.apply();
            }
        });
    }
}