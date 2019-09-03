package com.example.jasmn.jasmnapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
private Toolbar toolbar;
private TextInputLayout email,password;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email_login);
        password=findViewById(R.id.pasword_login);
        toolbar=findViewById(R.id.login_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }

    public void log_in(View view) {
        String email_ = email.getEditText().getText().toString();
        String password_ = password.getEditText().getText().toString();
        if (!TextUtils.isEmpty(email_)||!TextUtils.isEmpty(password_)){
            progressDialog.setTitle("logging in User");
            progressDialog.setMessage("please wait while we log in your account!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            login_user(email_,password_);
    }
}

    private void login_user(String email_, String password_) {
        mAuth.signInWithEmailAndPassword(email_,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   progressDialog.dismiss();
                   Intent in=new Intent(login.this,MainActivity.class);
                   startActivity(in);
                   finish();
               }
               else {
                   progressDialog.hide();
                   Toast.makeText(login.this, "please check you information and try again", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}
