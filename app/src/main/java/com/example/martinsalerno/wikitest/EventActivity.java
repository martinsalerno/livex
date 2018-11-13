package com.example.martinsalerno.wikitest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.adapters.CompletePostAdapter;
import com.example.martinsalerno.wikitest.classes.Post;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;
import com.google.gson.Gson;

import java.util.List;

import com.example.martinsalerno.wikitest.adapters.CommerceAdapter;
import com.example.martinsalerno.wikitest.adapters.ShowAdapter;
import com.example.martinsalerno.wikitest.classes.Commerce;
import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.Place;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.classes.Show;

import org.json.JSONArray;


public class EventActivity extends AppCompatActivity implements PostsFragmentInterface {
    Toolbar toolbar;
    private Event event;
    private ImageView eventImg;
    private RecyclerView recyclerShows;
    private RecyclerView recyclerCommerces;
    private RecyclerView recyclerPosts;
    private ProgressBar progressBar;
    private TextView placeName;
    private TextView placeAddress;
    private TextView showsNumber;
    private TextView commerceNumber;
    private Button buttonLocate;
    private Button buttonTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = findViewById(R.id.toolbarEvent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        eventImg = findViewById(R.id.eventImg);
        showsNumber = findViewById(R.id.showsNumber);
        commerceNumber = findViewById(R.id.commercesNumber);
        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);
        buttonLocate = findViewById(R.id.locateCommerces);
        buttonLocate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocation();
            }
        });
        recyclerPosts = findViewById(R.id.recyclerCompletePostsEvent);
        recyclerPosts.setVisibility(View.INVISIBLE);
        recyclerPosts.setHasFixedSize(true);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar = findViewById(R.id.loadingPostsEvent);

        recyclerShows = findViewById(R.id.recyclerShows);
        recyclerShows.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerCommerces = findViewById(R.id.recyclerComercios);
        recyclerCommerces.setLayoutManager(new GridLayoutManager(this, 2));
        loadEvent();
        loadPosts();
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
        new RequestHandler().loadEventImageSync(this, eventImg, event.getId());
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
    public void loadPosts() {
        String path = "publicaciones/evento/" + event.getId();
        new RequestHandler().loadPosts(path,this);
    }

    @Override
    public void assignPosts(Post[] posts) {
        showRecycler();
        CompletePostAdapter adapter = new CompletePostAdapter(posts, this);
        recyclerPosts.setAdapter(adapter);
    }

    @Override
    public void showRecycler() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerPosts.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void startLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Intent intent = new Intent(this, MapActivity.class);
        Gson gson = new Gson();
        String comerciosJson = gson.toJson(event.getComercios());
        intent.putExtra("comercios", comerciosJson);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, MapActivity.class);
            Gson gson = new Gson();
            String comerciosJson = gson.toJson(event.getComercios());
            intent.putExtra("comercios", comerciosJson);
            startActivity(intent);
        }
    }
}
