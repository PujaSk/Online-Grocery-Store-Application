package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.SqliteDatabase.DBHelper;
import com.example.grogrocery.R;

public class Signup extends AppCompatActivity {

    EditText name,email,pass,repass,phno,city;
    TextView tvclickable_login;
    Button btnRegister;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.signup_page_name);
        email = (EditText) findViewById(R.id.signup_page_email);
        pass = (EditText) findViewById(R.id.signup_page_password);
        repass = (EditText) findViewById(R.id.signup_page_confirmpass);
        phno = (EditText) findViewById(R.id.signup_page_phno);
        city = (EditText) findViewById(R.id.signup_page_city);
        btnRegister = (Button) findViewById(R.id.signup_page_btnregister);
        tvclickable_login = (TextView) findViewById(R.id.textView8);
        dbHelper = new DBHelper(this);

        tvclickable_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isignup = new Intent(Signup.this, Login.class);
                startActivity(isignup);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm, em, p1, p2, pno, c;
                nm = name.getText().toString();
                em = email.getText().toString();
                p1 = pass.getText().toString();
                p2 = repass.getText().toString();
                pno = phno.getText().toString();
                c = city.getText().toString();

              /*  if(nm.equals("") || em.equals("") || p1.equals("") || p2.equals("") || pno.equals("") || c.equals(""))
                {
                    Toast.makeText(Signup.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }  */

                if(TextUtils.isEmpty(nm)){
                    name.setError("Cannot be Empty");
                }
                if(TextUtils.isEmpty(em)){
                    email.setError("Cannot be Empty");
                }
                if(TextUtils.isEmpty(p1)){
                    pass.setError("Cannot be Empty");
                }
                if(p1.length()<4)
                {
                    pass.setError("Must be greater than or equal to 4 digits");
                }
                if(TextUtils.isEmpty(p2)){
                    repass.setError("Cannot be Empty");
                }
                if (TextUtils.isEmpty(pno)){
                    phno.setError("Cannot be Empty");
                }
                if(TextUtils.isEmpty(c)){
                    city.setError("Cannot be Empty");
                }
               else if(pno.length()!=10)
                //else if(! pno.matches("\\d{10}"))
                {
                    //Toast.makeText(getApplicationContext(), "Enter Proper Phone Number", Toast.LENGTH_SHORT).show();
                    phno.setError("Enter a valid 10 digit phone number");
                }
                else
                {
                    if(p1.equals(p2)) {
                        if(dbHelper.checkUseremail(em)){
                            Toast.makeText(getApplicationContext(),"User Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Proceed with registration
                        boolean registeredSuccess = dbHelper.addRecord(nm,em,p1,p2,pno,c);
                        if (registeredSuccess) {
                            Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                Intent isignup = new Intent(Signup.this, Login.class);
                                startActivity(isignup);
                                finish(); // Optional: Close the current activity
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Signup.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"User Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}