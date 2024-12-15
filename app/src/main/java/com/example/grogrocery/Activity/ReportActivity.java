package com.example.grogrocery.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grogrocery.R;

public class ReportActivity extends AppCompatActivity {

    private Button btnBillReport, btnItemReport, btnOrderReport;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btnBillReport = findViewById(R.id.btnBillReport);
        btnItemReport = findViewById(R.id.btnItemReport);
        btnOrderReport = findViewById(R.id.btnOrderReport);

        // Set up button click listeners
        btnBillReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open BillReportActivity
                Intent intent = new Intent(ReportActivity.this, BillReportActivity.class);
                startActivity(intent);
            }
        });

        btnItemReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ItemReportActivity (create this activity if it doesn't exist)
                //Intent intent = new Intent(ReportActivity.this, ItemReportActivity.class);
                //startActivity(intent);
            }
        });

        btnItemReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, ItemReportActivity.class);
                startActivity(intent);
            }
        });

        btnOrderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, OrderReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
