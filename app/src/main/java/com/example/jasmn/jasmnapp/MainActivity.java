package com.example.jasmn.jasmnapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private sectionsPagerAdapter adapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_page_par);
        viewPager=findViewById(R.id.tap_pager);
        tabLayout=findViewById(R.id.main_tabs);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jasmn App");
        adapter=new sectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser==null){
           sendTostart();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
  if (item.getItemId()==R.id.log_out){
      FirebaseAuth.getInstance().signOut();
      sendTostart();
  }
  else if (item.getItemId()==R.id.account_setting){
      Intent in=new Intent(MainActivity.this,setting.class);
      startActivity(in);
  }
  else if (item.getItemId()==R.id.all_users){
      Intent in=new Intent(MainActivity.this,allusers.class);
      startActivity(in);
  }
        return true;
    }

    private void sendTostart() {
        Intent in=new Intent(MainActivity.this,start.class);
        startActivity(in);
        finish();
    }
}
