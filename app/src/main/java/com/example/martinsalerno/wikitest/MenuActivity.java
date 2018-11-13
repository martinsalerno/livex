package com.example.martinsalerno.wikitest;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.location.LocationIntentService;
import com.example.martinsalerno.wikitest.location.LocationJob;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        toolbar = (Toolbar) findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Menu");
        startLocation();
    }

    private void startLocation() {
        Intent intent = new Intent(this, LocationIntentService.class);
        intent.putExtra("userId", new SessionHandler(this).getId());
        startService(intent);
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
    }

    public void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        new SessionHandler(this).logOut();
        startActivity(intent);
    }
}
