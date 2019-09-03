package com.example.jasmn.jasmnapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class allusers extends AppCompatActivity {
 Toolbar toolbar_;
 RecyclerView view_;
 DatabaseReference databaseReference;
 FirebaseUser firebaseUser;
 StorageReference storageReference;
 ArrayList<users>usersList=new ArrayList<>();


    private FirebaseRecyclerOptions<users> options;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    List<users> users_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final userAdapter adapter=new userAdapter(usersList,this);

        //  ArrayList<users> usersList=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);
        view_=findViewById(R.id.recycle_view);
        toolbar_=findViewById(R.id.all_users_par);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
       // String uid = firebaseUser.getUid();
       // Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
//        view_.setHasFixedSize(true);
//        view_.setLayoutManager(new LinearLayoutManager(this));

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {




                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                           String key = dataSnapshot1.getRef().getKey();
                            users users=dataSnapshot1.getValue(com.example.jasmn.jasmnapp.users.class);
                            users.setKey(key);
                        usersList.add(users);
                        adapter.notifyDataSetChanged();
                      Toast.makeText(allusers.this, ""+ usersList.get(0).getKey(), Toast.LENGTH_SHORT).show();

                    }
                       }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            users list=new users();
//            list.setName("yasssssss");
//            list.setStatus("iiiiiiiii");
//        users listttt=new users();
//        listttt.setName("hhhhhhh");
//        listttt.setStatus("gggggggg");
//            ll.add(list);
//            ll.add(listttt);
             view_.setLayoutManager(new LinearLayoutManager(this));
             view_.setAdapter(adapter);



    }


//    @Override
//    protected void onStart() {
//
//        super.onStart();
//
//        options =
//                new FirebaseRecyclerOptions.Builder<users>()
//                        .setQuery(databaseReference, users.class)
//                        .build();
//
//         firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<users, userviewholder>(options)
//         {
//            @NonNull
//            @Override
//            public userviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.single_user, viewGroup, false);
//
//                return new userviewholder(view);
//
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull userviewholder holder, int position, @NonNull users model) {
//                holder.setname(model.getName());
//               Toast.makeText(allusers.this, model.getName(), Toast.LENGTH_SHORT).show();
//
//            }
//
//             @Override
//             public int getItemCount() {
//                 return super.getItemCount();
//             }
//         };
//        view_.setAdapter(firebaseRecyclerAdapter);
//
//    }
//
//
//
//
//    public static class userviewholder extends RecyclerView.ViewHolder{
//           View theview;
//        public userviewholder(View itemView) {
//            super(itemView);
//            theview=itemView;
//
//        }
//
//        public void setname(String name) {
//            TextView nameview=theview.findViewById(R.id.name_);
//            nameview.setText(name);
//            System.out.println(name+"****************************************************************");
//        }
//    }
}
