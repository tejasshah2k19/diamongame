package com.royal.diamondgame;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GamePlay extends AppCompatActivity {

    TextView tvUserName;
    TextView tvBetAmount;

    TextView tvWiningAmount;

    Integer winningAmount = 0;
    Integer betAmount =0;
    String userId;
    int credit;
    String token;
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


        SharedPreferences preferences = getSharedPreferences("diamond_game",MODE_PRIVATE);


         String userName = preferences.getString("firstName","Guest");
         betAmount = 500;
         credit = preferences.getInt("credit",0);
         userId = preferences.getString("userId","-1");
         token = preferences.getString("token","-1");

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
            int index =  (int)(Math.random()*imgBtn.length);//0...15
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
            //minus
            ExecutorService ex = Executors.newSingleThreadExecutor();
            ex.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    updateCreditApi(userId,-betAmount);
                    return null;
                }
            });


        }else{
            clickBtn.setBackground(getDrawable(R.drawable.diamond));
            Log.i("gamePlay","Diamond");
            winningAmount = winningAmount + betAmount;
            tvWiningAmount.setText(winningAmount+"");

//            updateCreditApi(userId,winningAmount);
        }
        //cashout
        clickBtn.setEnabled(false);
    }

    private void updateCreditApi(String userId,int betAmount){
        String apiURL = "https://diamondgame.onrender.com/api/users/credit/"+userId;
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+token);

            connection.setDoOutput(true);
            connection.setDoInput(true);



            Log.i("api","url => "+apiURL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("credit",betAmount);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes();
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            Log.i("api","response code => "+responseCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}