package com.example.jasmn.jasmnapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class status extends AppCompatActivity {
Toolbar toolbar;
TextInputLayout stat;
private DatabaseReference database;
private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        toolbar=findViewById(R.id.status_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stat=findViewById(R.id.status_edittxt);
        user=FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        database=FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        Intent intent = getIntent();
        String stats = intent.getStringExtra("st");
        stat.getEditText().setText(stats);
    }

    public void change(View view) {
        String stats = stat.getEditText().getText().toString();
        database.child("status").setValue(stats);

    }

    public void cancle(View view) {
        Intent in=new Intent(this,setting.class);
        startActivity(in);
    }
}
