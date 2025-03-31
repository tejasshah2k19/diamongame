package com.royal.diamondgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.royal.diamondgame.adapter.UserItemAdapter;
import com.royal.diamondgame.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class LeaderboardActivity extends AppCompatActivity {

    private ListView listView;
    private UserItemAdapter adapter;
    private List<UserModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listView);
        itemList = new ArrayList<>();


        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Callable<ArrayList<UserModel>>() {
       @Override
            public ArrayList<UserModel> call() throws Exception {
                return leaderBoardApi();
            }
        });

        adapter = new UserItemAdapter(this, itemList);
        listView.setAdapter(adapter);

    }//onCreate


    private ArrayList<UserModel> leaderBoardApi(){

        String apiURL = "https://diamondgame.onrender.com/api/users/leaderboard";

        SharedPreferences preferences = getSharedPreferences("diamond_game",MODE_PRIVATE);

           String token =  preferences.getString("token","");

           Log.i("api","token => "+token);
    try{
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization",token);
        connection.setDoOutput(true);
        connection.setDoInput(true);



        int responseCode = connection.getResponseCode();

        Log.i("api",responseCode+"");

            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.i("api", "done");
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                Log.i("api", "api response => " + response.toString());

                JSONObject jsonObjectResp = new JSONObject(response.toString());
                JSONArray user = jsonObjectResp.getJSONArray("user");

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<UserModel>>() {}.getType();

                ArrayList<UserModel> list = gson.fromJson(response.toString(),listType);
                return list;
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.i("api","error = > "+e.getMessage());
        }
            return null;
    }


}//class