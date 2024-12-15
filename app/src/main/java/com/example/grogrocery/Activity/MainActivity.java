package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grogrocery.R;

public class MainActivity extends AppCompatActivity {

    Button login,signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button) findViewById(R.id.btn_login);
        signup=(Button) findViewById(R.id.btn_signup);


        Intent ilogin = new Intent(MainActivity.this, Login.class);
        Intent isignup = new Intent(MainActivity.this, Signup.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ilogin);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(isignup);
            }
        });
    }
}