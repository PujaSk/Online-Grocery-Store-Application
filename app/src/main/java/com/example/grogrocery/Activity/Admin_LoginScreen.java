package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

public class Admin_LoginScreen extends AppCompatActivity {
    EditText email,pass;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_loginscreen);

        email = (EditText) findViewById(R.id.admin_login_page_email);
        pass = (EditText) findViewById(R.id.admin_login_page_password);
        btnLogin = (Button) findViewById(R.id.btn_admin_login_page);
        dbHelper = new DBHelper(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em,pwd;
                em = email.getText().toString();
                pwd = pass.getText().toString();

                if(em.equals("")){
                    email.setError("Email is Compulsory");
                }
                if(pwd.equals("")){
                    pass.setError("Password is Must");
                }
                else {
                    boolean isLoggedId = dbHelper.checkAdmin(em, pwd);
                    if (isLoggedId) {
                        try {
                            Intent ilogin = new Intent(Admin_LoginScreen.this, Admin_HomeScreen.class);
                            startActivity(ilogin);
                            finish(); // Optional: Close the current activity
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Admin_LoginScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "\t\t\t\t\t\t\t\t\t Login Failed \nEnter Valid Email Id and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}