package com.example.martinsalerno.wikitest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.se.omapi.Session;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import classes.LivexExchange;
import classes.SessionHandler;

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = "LOG TEST";
    private EditText userTextEdit;
    private EditText passwordTextEdit;
    private ProgressBar loadingSpinner;
    private final String URL = "http://livexws.sa-east-1.elasticbeanstalk.com/auth";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userTextEdit = findViewById(R.id.usuario);
        passwordTextEdit = findViewById(R.id.password);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        loadingSpinner.setVisibility(View.INVISIBLE);
        askForPermissions();
    }

    public void submitLogin(View view) {
        loadingSpinner.setVisibility(View.VISIBLE);
        logUser(userTextEdit.getText().toString(), passwordTextEdit.getText().toString());
    }

    public void logUser(final String username, String password) {
        JSONObject jsonBody = buildPayload(username, password);
        Log.d("LOGIN", jsonBody.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LOGIN", response.toString());
                        loadingSpinner.setVisibility(View.INVISIBLE);
                        showToast("Bienvenido " + userTextEdit.getText().toString());
                        saveSession(username, response);
                        sendToMenu();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOGIN", error.toString());
                        loadingSpinner.setVisibility(View.INVISIBLE);
                        showToast("Usuario o contraseña incorrectos");
                        sendToMenu();

                    }
                });
        LivexExchange.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public JSONObject buildPayload(String username, String password) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", username);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public void saveSession(String username, JSONObject response) {
        SessionHandler session = new SessionHandler(this);
        try {
            session.setSession(username, response.getString("id"), response.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String textToShow) {
        Toast.makeText(getBaseContext(), textToShow, Toast.LENGTH_SHORT).show();
    }

    public void sendToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);

        }
    }
}

