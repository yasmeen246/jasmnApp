package com.example.jasmn.jasmnapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userHolder> {
   private List<users>usersList;
    private Context context;

    public userAdapter(List<users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_user,viewGroup,false);
        userHolder holder=new userHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder userHolder, int i) {
          final users users=usersList.get(i);
          userHolder.name_1.setText(users.getName());
          userHolder.status_1.setText(users.getStatus());
          userHolder.setImageView(users.getImage(),context);
          userHolder.itemView.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(context,user_profile.class);
                  intent.putExtra("key",users.getKey());
                  context.startActivity(intent);
              }
          });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class userHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name_1,status_1;
        public userHolder(@NonNull View itemView) {

            super(itemView);
            name_1=itemView.findViewById(R.id.name_);
            status_1=itemView.findViewById(R.id.status_);

        }

        public void setImageView(String imageView1,Context context) {
            imageView=itemView.findViewById(R.id.imgeus);
            Picasso.with(context).load(imageView1).placeholder(R.drawable.default_image).into(imageView);

        }
    }

}
