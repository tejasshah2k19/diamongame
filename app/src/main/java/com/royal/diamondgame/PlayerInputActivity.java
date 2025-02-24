package com.royal.diamondgame;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class PlayerInputActivity extends AppCompatActivity {

    EditText edtUserName;
    EditText edtBetAmt;

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

        edtBetAmt = findViewById(R.id.edtInputPlayerAmount);
        edtUserName = findViewById(R.id.edtInputPlayerName);
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
                String userName = edtUserName.getText().toString();
                Integer betAmount = Integer.parseInt(edtBetAmt.getText().toString());

                SharedPreferences preferences  = getSharedPreferences("diamondgame",MODE_PRIVATE);

                 Integer amount = preferences.getInt(userName,0);

                if(amount == 0) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(userName, 5000);//ram = 500
                }

                Intent intent = new Intent(getApplicationContext(), GamePlay.class);
                intent.putExtra("userName",userName);
                intent.putExtra("betAmount",betAmount);
                startActivity(intent);
            }
        });

    }
}