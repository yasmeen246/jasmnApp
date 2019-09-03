package com.example.jasmn.jasmnapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class setting extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseUser user;
    CircleImageView imageView;
    TextView name_,status_;
    private static final int request_pic=1;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imageView=findViewById(R.id.user_image);
        name_=findViewById(R.id.user_name);
        status_=findViewById(R.id.status);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        database=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        database.keepSynced(true);
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
               final String image = dataSnapshot.child("image").getValue().toString();
                String images = dataSnapshot.child("images").getValue().toString();
               name_.setText(name);
               status_.setText(status);

                Picasso.with(setting.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_image).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(setting.this).load(image).placeholder(R.drawable.default_image).into(imageView);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void change_image(View view) {
Intent photo=new Intent();
photo.setType("image/*");
photo.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(Intent.createChooser(photo,"select image"),request_pic);

    }



   @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==request_pic&&resultCode==RESULT_OK){
            Uri uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1,1)
                    .start(setting.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                String uid = user.getUid();
                 final StorageReference filepath = mStorageRef.child("profile_picture").child(uid+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(setting.this, "success", Toast.LENGTH_SHORT).show();
                            Task<Uri> downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String urii = uri.toString();
                                    Toast.makeText(setting.this, ""+urii, Toast.LENGTH_LONG).show();

                                    database.child("image").setValue(urii).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Toast.makeText(setting.this, "success", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                            });


                        }
                        else Toast.makeText(setting.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void change_status(View view) {

        Intent in=new Intent(setting.this,status.class);
        in.putExtra("st",status_.getText().toString());
        startActivity(in);
        finish();
    }
  //random image name
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
