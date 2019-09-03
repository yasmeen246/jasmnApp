package com.example.jasmn.jasmnapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class regesterpage extends AppCompatActivity {
    private TextInputLayout name, email, password;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesterpage);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password1);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.regester_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Creat Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
    }

    public void newaccount(View view) {
        String name_ = name.getEditText().getText().toString();
        String email_ = email.getEditText().getText().toString();
        String password_ = password.getEditText().getText().toString();
        if (!TextUtils.isEmpty(name_) || !TextUtils.isEmpty(email_) || !TextUtils.isEmpty(password_)) {
            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("please wait while we creat your account!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            regester_user(name_, email_, password_);

        }
    }

    private void regester_user(final String name_, String email_, String password_) {
        mAuth.createUserWithEmailAndPassword(email_, password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    reference = database.getReference().child("users").child(uid);
                    HashMap<String, String> data = new HashMap<>();
                    data.put("name", name_);
                    data.put("status", "hello" + name_);
                    data.put("image", "default");
                    data.put("images", "default");
                    reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent in = new Intent(regesterpage.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                                finish();
                            } else {
                                progressDialog.hide();
                                Toast.makeText(regesterpage.this, "please check you information and try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


                }
            }
        });
    }


}

