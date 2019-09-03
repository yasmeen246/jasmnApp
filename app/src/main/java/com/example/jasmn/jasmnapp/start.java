package com.example.jasmn.jasmnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void regester(View view) {
        Intent in=new Intent(start.this,regesterpage.class);
        startActivity(in);
    }

    public void log_in_(View view) {
        Intent in=new Intent(start.this,login.class);
        startActivity(in);
        finish();
    }
}
