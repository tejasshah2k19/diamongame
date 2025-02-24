package com.royal.diamondgame;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button leaderBoradButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide title bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Fullscreen

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        playButton = findViewById(R.id.imgBtnMainPlayGame);
        leaderBoradButton = findViewById(R.id.imgBtnMainLeaderBoard);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator rotate = ObjectAnimator.ofFloat(playButton, "rotation", 0f, 360f);
                rotate.setDuration(1000);
                rotate.start();

              Thread t=   new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(getApplicationContext(), PlayerInputActivity.class);
                        startActivity(intent);
                    }
                };
                t.start();
            }
        });

    }
}