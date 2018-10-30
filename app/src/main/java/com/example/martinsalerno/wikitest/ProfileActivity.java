package com.example.martinsalerno.wikitest;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ImageView profilePic;
    private ImageView backgroundPic;
    private TextView username;
    private TextView postsNumber;
    private TextView eventsNumber;
    private TextView friendsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        profilePic = findViewById(R.id.userImage);
        backgroundPic = findViewById(R.id.profileBackground);
        username = findViewById(R.id.userName);
        postsNumber = findViewById(R.id.publicacionesNumber);
        eventsNumber = findViewById(R.id.espectaculosNumber);
        friendsNumber = findViewById(R.id.amigosNumber);
        loadRandomBackground();
        loadProfileUsername();
    }

    public void loadRandomBackground() {
        TypedArray profileImages = getResources().obtainTypedArray(R.array.profileImages);
        int i = new Random().nextInt(profileImages.length());
        int id = profileImages.getResourceId(i, -1);
        profileImages.recycle();
        backgroundPic.setImageResource(id);
    }

    public void loadProfileUsername() {
        String currentUsername = new SessionHandler(this).getUsername();
        username.setText(currentUsername);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                sendToProfile();
                break;
            case R.id.logout:
                sendToLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        new SessionHandler(this).logOut();
        startActivity(intent);
    }
}
