package com.govind.chitchatgovind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


public class UsersActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private RecyclerView mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mtoolbar=findViewById(R.id.users_appBar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(mtoolbar);
        mUserList=findViewById(R.id.users_list);
    }
}
