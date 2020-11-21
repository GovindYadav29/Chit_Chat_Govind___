package com.govind.chitchatgovind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter msectionsPagerAdapter;
    private TabLayout mtablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar =findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Chit Chat-Govind");
        //tabs
        mViewPager=findViewById(R.id.main_tabpager);
        msectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(msectionsPagerAdapter);
        mtablayout=findViewById(R.id.main_tabs);
        mtablayout.setupWithViewPager(mViewPager);
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
          sendToStart();
        }

    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId() == R.id.main_log_out_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
         }
         if(item.getItemId()==R.id.main_settings_btn){
             Intent settings_intent=new Intent(MainActivity.this,SettongsActivity.class);
             startActivity(settings_intent);
         }
         return true;
    }
}