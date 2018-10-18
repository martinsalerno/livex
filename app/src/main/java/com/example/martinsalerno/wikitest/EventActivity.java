package com.example.martinsalerno.wikitest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import adapters.CommerceAdapter;
import adapters.ShowAdapter;
import classes.Commerce;
import classes.Event;
import classes.Place;
import classes.RequestHandler;
import classes.SessionHandler;
import classes.Show;


public class EventActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Event event;
    private ImageView eventImg;
    private RecyclerView recyclerShows;
    private RecyclerView recyclerCommerces;
    private TextView placeName;
    private TextView placeAddress;
    private TextView showsNumber;
    private TextView commerceNumber;
    //private TextView placeCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbarEvent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        eventImg = findViewById(R.id.eventImg);
        showsNumber = findViewById(R.id.showsNumber);
        commerceNumber = findViewById(R.id.commercesNumber);
        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);

        recyclerShows = findViewById(R.id.recyclerShows);
        recyclerShows.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerCommerces = findViewById(R.id.recyclerComercios);
        recyclerCommerces.setLayoutManager(new GridLayoutManager(this, 2));
        loadEvent();
    }

    public void loadEvent(){
        Gson gson = new Gson();
        event = gson.fromJson(getIntent().getStringExtra("event"), Event.class);
        loadEventImg();
        loadPlace();
        loadShows();
        loadCommerces();
    }

    public void loadEventImg(){
        new RequestHandler().loadEventImage(this, eventImg, event.getId());
    }

    public void loadPlace(){
        Place place = event.getEstablecimiento();
        placeName.setText(place.getNombre());
        placeAddress.setText(place.getDireccion());
    }

    public void loadShows(){
        Gson gson = new Gson();
        Log.d("ACA Shows", gson.toJson(this.event.getFunciones()).toString());
        List<Show> functions = this.event.getFunciones();
        showsNumber.setText(Integer.toString(functions.size()));
        ShowAdapter adapter = new ShowAdapter(functions, this);
        recyclerShows.setAdapter(adapter);
    }

    public void loadCommerces(){
        Gson gson = new Gson();
        Log.d("ACA commerces", gson.toJson(this.event.getComercios()).toString());
        List<Commerce> commerces = this.event.getComercios();
        commerceNumber.setText(Integer.toString(commerces.size()));
        CommerceAdapter adapter = new CommerceAdapter(commerces, this);
        recyclerCommerces.setAdapter(adapter);
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
