package com.example.jasmn.jasmnapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class user_profile extends AppCompatActivity {
 ImageView imageView;
 TextView uname,ustatus,allfriends;
 Button request_button,declinebt;
 private DatabaseReference databaseReference;
  private DatabaseReference friend_request;
  private FirebaseUser firebaseUser;
  private DatabaseReference friends,notificationsDatabase;

  String current_frindly_state;
  Toolbar toolbar;
  String name;
  String user_uid;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar=findViewById(R.id.profile_par);
        uname=findViewById(R.id.u_name);
        ustatus=findViewById(R.id.ustats);
        allfriends=findViewById(R.id.all);
        request_button=findViewById(R.id.request);
          declinebt=findViewById(R.id.decline);

        declinebt.setVisibility(View.INVISIBLE);
        declinebt.setEnabled(false);


        current_frindly_state="not_friends";
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        friend_request=FirebaseDatabase.getInstance().getReference().child("friends_request");
        friends=FirebaseDatabase.getInstance().getReference().child("friends");
        notificationsDatabase=FirebaseDatabase.getInstance().getReference().child("notification");
        user_uid = firebaseUser.getUid();

        key = getIntent().getStringExtra("key");
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String images = dataSnapshot.child("images").getValue().toString();
                uname.setText(name);
                ustatus.setText(status);
                Picasso.with(user_profile.this).load(image).placeholder(R.drawable.default_image).into(imageView);

                friend_request.child(user_uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(key)){
                            String value = dataSnapshot.child(key).child("request").getValue().toString();
                            if (value.equals("received")){
                                current_frindly_state="request_received";
                                request_button.setEnabled(true);
                                request_button.setText("accept Friend Request");
                                declinebt.setVisibility(View.VISIBLE);
                                declinebt.setEnabled(true);
                            }else if (value.equals("sent")){
                                current_frindly_state="request_sent";
                                request_button.setEnabled(true);
                                request_button.setText("cancel Friend Request");
                                declinebt.setVisibility(View.INVISIBLE);
                                declinebt.setEnabled(false);
                            }}
                            else {
                                friends.child(user_uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(key)){
                                            current_frindly_state="friends";
                                            request_button.setText("un friend");
                                            declinebt.setVisibility(View.INVISIBLE);
                                            declinebt.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView=findViewById(R.id.imageView11);




    }

    public void request(View view) {
        //********************send request
        if (current_frindly_state.equals("not_friends")){
            friend_request.child(user_uid).child(key).child("request").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friend_request.child(key).child(user_uid).child("request").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                              HashMap<String,String> notification=new HashMap<>();
                              notification.put("from",user_uid);
                              notification.put("type","request");
                              notification.put("to",key);
                              notificationsDatabase.push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      current_frindly_state="request_sent";
                                      request_button.setEnabled(true);
                                      request_button.setText("Cancel Friend Request");
                                      declinebt.setVisibility(View.INVISIBLE);
                                      declinebt.setEnabled(false);
                                  }
                              });

                             //   Toast.makeText(user_profile.this, "successfully sending request", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else
                        Toast.makeText(user_profile.this, "failed sending request", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (current_frindly_state.equals("request_sent")){
            friend_request.child(user_uid).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    friend_request.child(key).child(user_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            current_frindly_state="not_friends";
                            request_button.setEnabled(true);
                            request_button.setText("send Friend Request");
                            declinebt.setVisibility(View.INVISIBLE);
                            declinebt.setEnabled(false);
                        }
                    });
                }
            });
        }
        if (current_frindly_state.equals("request_received")){
            final String date = DateFormat.getDateTimeInstance().format(new Date());
            friends.child(user_uid).child(key).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   friends.child(key).child(user_uid).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                           friend_request.child(user_uid).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   friend_request.child(key).child(user_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           current_frindly_state="friends";
                                           request_button.setEnabled(true);
                                           request_button.setText("un Friend");
                                           declinebt.setVisibility(View.INVISIBLE);
                                           declinebt.setEnabled(false);
                                       }
                                   });
                               }
                           });
                       }
                   });



                }
            });
        }

        if (current_frindly_state.equals("friends")){
            friends.child(user_uid).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    friends.child(key).child(user_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            current_frindly_state="not_friends";
                            request_button.setEnabled(true);
                            request_button.setText("send Friend Request");
                            declinebt.setVisibility(View.INVISIBLE);
                            declinebt.setEnabled(false);
                        }
                    });

                }
            });
        }

    }

    public void decline(View view) {
        friend_request.child(user_uid).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                friend_request.child(key).child(user_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        current_frindly_state="not_friends";
                        request_button.setEnabled(true);
                        request_button.setText("send Friend Request");
                        declinebt.setVisibility(View.INVISIBLE);
                        declinebt.setEnabled(false);
                    }
                });
            }
        });
    }
}
