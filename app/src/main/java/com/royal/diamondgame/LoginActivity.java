package com.royal.diamondgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {

    EditText edtLogin;
    EditText edtPassword;
    Button btnLogin;
    TextView tvNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        edtLogin = findViewById(R.id.edtLoginEmail);
        //
        tvNewUser = findViewById(R.id.tvLoginNewUser);

        edtPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerInputActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtLogin.getText().toString();
                String password = edtPassword.getText().toString();

               ExecutorService ex =  Executors.newSingleThreadExecutor();

              Future<Integer> ft  =  ex.submit(new Callable<Integer>() {
                  @Override
                  public Integer call() throws Exception {
                      return callLoginApi(email,password);
                  }
              });

              try {
                  Integer resp = ft.get();
                  if(resp == 200){
                      Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                    //intetn -> GamePlay
                    Intent intent =new Intent(getApplicationContext(), GamePlay.class);
                    startActivity(intent);

                  }else if(resp == 400){
                      //400
                      Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                  }else if(resp == 500){
                      Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                  }
              }catch(Exception e){
                  //toast
              }
            }
        });

    }


    private Integer callLoginApi(String email,String password){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("email",email);
            jsonObject.put("password",password);

            String jsonString = jsonObject.toString();

            Log.i("api",jsonString);

            String apiURL = "https://diamondgame.onrender.com/api/auth/login";

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

            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.i("api","done");
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                 }
                scanner.close();
                Log.i("api","api response => "+response.toString());

                JSONObject jsonObjectResp = new JSONObject(response.toString());
                JSONObject user = jsonObjectResp.getJSONObject("user");


                SharedPreferences preferences = getSharedPreferences("diamond_game",MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("firstName",user.getString("firstName"));
                editor.putString("lastName",user.getString("lastName"));
                editor.putInt("credit",user.getInt("credit"));
                editor.putString("userId",user.getString("_id"));
                editor.apply(); //save

                return 200;


            }else{
                Log.i("api","fail");
            }
            Log.i("api","calling end");
        }catch (Exception e){

            Log.i("api","runtime error ");
            e.printStackTrace();
        }

    return -1;
    }
}