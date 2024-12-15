package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.SqliteDatabase.DBHelper;
import com.example.grogrocery.R;

public class Login extends AppCompatActivity {
    EditText email,pass;
    TextView tvclickable_signup,tvclickable_recoverit;
    Button btnLogin,btnAdminLogin;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // colour of green -  #E2FAE3   #DDFBDF      FFC1F6F1

        email = (EditText) findViewById(R.id.admin_login_page_email);
        pass = (EditText) findViewById(R.id.admin_login_page_password);
        btnLogin = (Button) findViewById(R.id.btn_admin_login_page);
        btnAdminLogin = (Button) findViewById(R.id.btn_admin_login);
        tvclickable_signup = (TextView) findViewById(R.id.priceTxt);
        tvclickable_recoverit = (TextView) findViewById(R.id.textView8);
        dbHelper = new DBHelper(this);

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent ilogin = new Intent(Login.this, HomeScreen.class);
            startActivity(ilogin);

            }
        });  */

        // Signup navigation
        tvclickable_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isignup = new Intent(Login.this, Signup.class);
                startActivity(isignup);
            }
        });

      /*  tvclickable_recoverit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iforgotpass = new Intent(Login.this, ForgotPassword.class);
                startActivity(iforgotpass);
            }
        });   */

        // Admin login navigation
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Login.this, Admin_LoginScreen.class);
                    startActivity(i);
                    finish(); // Optional: Close the current activity
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // User login handling
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em,pwd;
                em = email.getText().toString();
                pwd = pass.getText().toString();


               /* if(em.equals("") || pwd.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }  */
                if(em.equals("")){
                    email.setError("Email is Compulsory");
                }
                if(pwd.equals("")){
                    pass.setError("Password is Must");
                }
                else {
                    boolean isLoggedId = dbHelper.checkUser(em, pwd);
                    if (isLoggedId) {
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Retrieve the user_id
                        int userId = dbHelper.getUserIdByEmail(em);
                        Log.d("LoginActivity", "Retrieved userId: " + userId);
                        //Toast.makeText(getApplicationContext(), "User Id:  "+userId, Toast.LENGTH_SHORT).show();

                        if (userId != -1) {
                            // Store userId in SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("user_id", userId);
                            editor.apply();

                            // Proceed to the next activity
                            Intent ilogin = new Intent(Login.this, HomeScreen.class);
                            startActivity(ilogin);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "User ID not found.", Toast.LENGTH_SHORT).show();
                        }

                    }else
                        Toast.makeText(Login.this, "\t\t\t\t\t\t\t\t\t Login Failed \nEnter Valid Email Id and Password", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}