package com.example.martinsalerno.wikitest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import classes.SessionHandler;

public class SocialActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabAdd, fabTake, fabUpload;
    private final float TRANSLATIONY = 100f;
    private boolean isMenuOpen = false;
    Toolbar toolbar;

    OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        initFabs();
        toolbar = (Toolbar) findViewById(R.id.toolbarSocial);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Feed");
    }

    public void initFabs() {
        fabAdd = findViewById(R.id.addPhoto);
        fabTake = findViewById(R.id.takePhoto);
        fabUpload = findViewById(R.id.uploadPhoto);

        fabAdd.setOnClickListener(this);
        fabTake.setOnClickListener(this);
        fabUpload.setOnClickListener(this);

        fabTake.setVisibility(View.VISIBLE);
        fabUpload.setVisibility(View.VISIBLE);

        fabTake.setTranslationY(TRANSLATIONY);
        fabUpload.setTranslationY(TRANSLATIONY);

        fabTake.setAlpha(0f);
        fabUpload.setAlpha(0f);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPhoto:
                toggleMenu();
                break;
            case R.id.takePhoto:
                sendToTakePhoto(view);
                break;
            case R.id.uploadPhoto:
                break;
        }
    }

    private void toggleMenu() {
        if (isMenuOpen) {
            animateButtons(0f, 0f);
        }
        else {
            animateButtons(TRANSLATIONY, 2f);
        }
        isMenuOpen = !isMenuOpen;
    }

    public void sendToTakePhoto(View view) {
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    private void animateButtons(float translationY, float alpha) {
        fabAdd.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();
        fabUpload.animate().translationY(translationY).alpha(alpha).setInterpolator(interpolator).setDuration(300).start();
        fabTake.animate().translationY(translationY).alpha(alpha).setInterpolator(interpolator).setDuration(300).start();
    }
}
