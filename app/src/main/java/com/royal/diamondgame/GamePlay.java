package com.royal.diamondgame;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GamePlay extends AppCompatActivity {

    TextView tvUserName;
    TextView tvBetAmount;

    TextView tvWiningAmount;

    Integer winningAmount = 0;
    Integer betAmount =0;

    ImageButton imgBtn[] = new ImageButton[16];

    Integer bomb[] = new Integer[4];
    ArrayList<ImageButton> bombList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String userName = intent.getStringExtra("userName");
       betAmount = intent.getIntExtra("betAmount",0);

        tvUserName = findViewById(R.id.tvGamePlayUserName);
        tvBetAmount = findViewById(R.id.tvGamePlayBetAmount);
        tvWiningAmount = findViewById(R.id.tvGamePlayWinningAmount);

        tvUserName.setText(userName);
        tvBetAmount.setText(betAmount+"");
        tvWiningAmount.setText(winningAmount+"");

        imgBtn[0] = findViewById(R.id.imgBtn0);
        imgBtn[1] = findViewById(R.id.imgBtn1);
        imgBtn[2] = findViewById(R.id.imgBtn2);
        imgBtn[3] = findViewById(R.id.imgBtn3);
        imgBtn[4] = findViewById(R.id.imgBtn4);
        imgBtn[5] = findViewById(R.id.imgBtn5);
        imgBtn[6] = findViewById(R.id.imgBtn6);
        imgBtn[7] = findViewById(R.id.imgBtn7);
        imgBtn[8] = findViewById(R.id.imgBtn8);
        imgBtn[9] = findViewById(R.id.imgBtn9);
        imgBtn[10] = findViewById(R.id.imgBtn10);
        imgBtn[11] = findViewById(R.id.imgBtn11);
        imgBtn[12] = findViewById(R.id.imgBtn12);
        imgBtn[13] = findViewById(R.id.imgBtn13);
        imgBtn[14] = findViewById(R.id.imgBtn14);
        imgBtn[15] = findViewById(R.id.imgBtn15);


        for(int i=0;i<bomb.length;i++){
            int index =  (int)(Math.random()*bomb.length);//0...15
            Log.i("gamePlay","bomb set on "+index);
            bombList.add(imgBtn[index]);
         }

         for(int i=0;i<imgBtn.length;i++){
             imgBtn[i].setOnClickListener(this::checkButton);
         }

    }



    private void checkButton(View view) {

        Log.i("gamePlay",view.getId()+"");

        ImageButton clickBtn = findViewById(view.getId());

        if(bombList.contains(clickBtn)){
            Log.i("gamePlay","Blast");
            clickBtn.setBackground(getDrawable(R.drawable.bomb));
        }else{
            clickBtn.setBackground(getDrawable(R.drawable.diamond));
            Log.i("gamePlay","Diamond");
            winningAmount = winningAmount + betAmount;
            tvWiningAmount.setText(winningAmount+"");
        }

    }


}