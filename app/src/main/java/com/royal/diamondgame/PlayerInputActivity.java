package com.royal.diamondgame;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PlayerInputActivity extends AppCompatActivity {

    EditText edtFirstName;
    EditText edtLastName;
    EditText edtEmail;
    EditText edtPassword;

    Button btnPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_input);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtFirstName = findViewById(R.id.edtInputFirstName);
        edtLastName = findViewById(R.id.edtInputLastName);
        edtEmail = findViewById(R.id.edtInputEmail);
        edtPassword = findViewById(R.id.edtInputPassword);
       //
        Toast.makeText(this, "First time Player will get 5000 bonus in their account", Toast.LENGTH_SHORT).show();

        btnPlay = findViewById(R.id.btnInputPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                        btnPlay,
                        PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.1f, 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.1f, 1f)
                );
                scaleDown.setDuration(300);
                scaleDown.start();
//                String userName = edtUserName.getText().toString();
//                Integer betAmount = Integer.parseInt(edtBetAmt.getText().toString());
//
//                SharedPreferences preferences  = getSharedPreferences("diamondgame",MODE_PRIVATE);
//
//                 Integer amount = preferences.getInt(userName,0);
//
//                if(amount == 0) {
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt(userName, 5000);//ram = 500
//                }


                 Runnable r = new Runnable() {
                     @Override
                     public void run() {
                         callSignupApi();
                     }
                 };

                 Thread t = new Thread(r);
                 t.start();

                Intent intent = new Intent(getApplicationContext(), GamePlay.class);//Login
//                intent.putExtra("userName",userName);
//                intent.putExtra("betAmount",betAmount);
                startActivity(intent);
            }
        });

    }

    private void callSignupApi(){
        Log.i("api","calling start");
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
    try {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("email",email);
        jsonObject.put("password",password);
        jsonObject.put("credit",5000);

        String jsonString = jsonObject.toString();

        Log.i("api",jsonString);

        String apiURL = "https://diamondgame.onrender.com/api/auth/signup";

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonString.getBytes();
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        Log.i("api",responseCode+"");
         if (responseCode == HttpURLConnection.HTTP_CREATED) {
            Log.i("api","done");
        }else{
            Log.i("api","fail");
        }
//
//        Scanner scanner = new Scanner(connection.getInputStream());
//        StringBuilder response = new StringBuilder();
//        while (scanner.hasNext()) {
//            response.append(scanner.nextLine());
//        }
//        scanner.close();

        Log.i("api","calling end");
//        Log.i("api",response.toString());
    }catch (Exception e){

        Log.i("api","runtime error ");
        e.printStackTrace();
    }




    }

}//class