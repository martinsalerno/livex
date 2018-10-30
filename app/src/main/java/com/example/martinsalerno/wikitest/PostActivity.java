package com.example.martinsalerno.wikitest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.tasks.LoadEventTask;
import com.example.martinsalerno.wikitest.tasks.LoadPostTask;
import com.example.martinsalerno.wikitest.tasks.SavePostTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String uri;
    private Button publicateButton;
    private ImageView image;
    private Toolbar toolbar;
    private Spinner dropDown;
    private ProgressBar progressBar;
    private EditText editText;
    private TextView textView;
    private final String noEvent = "Sin espectáculo";
    private ArrayMap<String, String> hashEvents = new ArrayMap<>();
    private ArrayAdapter<String> adapter;

    public PostActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        hashEvents.put(noEvent, "0");
        ArrayList<String> eventStrings = new ArrayList<>();
        eventStrings.add(noEvent);
        dropDown = findViewById(R.id.dropDownEvents);
        progressBar = findViewById(R.id.loadingPost);
        editText = findViewById(R.id.postDescription);
        textView = findViewById(R.id.postTextView);
        uri = getIntent().getStringExtra("imageUri");
        adapter = new ArrayAdapter<>(PostActivity.this, R.layout.spinner_item, eventStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        publicateButton = findViewById(R.id.postCommit);
        publicateButton.setOnClickListener(this);
        image = findViewById(R.id.postPhoto);

        image.setVisibility(View.INVISIBLE);
        publicateButton.setVisibility(View.INVISIBLE);
        dropDown.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new RequestHandler().loadEventsFromPost(this);
        File file = new File(Environment.getExternalStorageDirectory(), uri.toString());
        if (file.exists()) {
            Log.d("el archivo EXISTE", file.getAbsolutePath());
        }
        if (hasPermissions()) {
            new LoadPostTask(this, image).execute(uri);
        }
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        new SavePostTask(this).execute(uri, hashEvents.get(dropDown.getSelectedItem().toString()), editText.getText().toString());
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    public void setEvents(Event[] events) {
        for (int i = 0; i < events.length; i++){
            if (events[i].getNombre() == null) { continue; }
            adapter.add(events[i].getNombre());
            hashEvents.put(events[i].getNombre(), events[i].getId());
        }
        adapter.notifyDataSetChanged();
    }

    public void notifyImageLoadFinished() {
        image.setVisibility(View.VISIBLE);
        publicateButton.setVisibility(View.VISIBLE);
        dropDown.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void notifyImagePostFinished() {
        Toast.makeText(this, "Publicación creada", Toast.LENGTH_LONG).show();
        finish();
    }

    public boolean hasPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            new LoadPostTask(this, image).execute(uri);
        }
    }
}

