package com.example.martinsalerno.wikitest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = "LOG TEST";
    private Button createUserButton;
    private EditText userTextEdit;
    private EditText passwordTextEdit;
    private ProgressBar loadingSpinner;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createUserButton = findViewById(R.id.createUserFromLogin);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateUserActivity.class);
                startActivity(intent);            }
        });
        userTextEdit = findViewById(R.id.usuario);
        passwordTextEdit = findViewById(R.id.password);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        loadingSpinner.setVisibility(View.INVISIBLE);
        askForPermissions();
    }

    public void submitLogin(View view) {
        hideKeyboard();
        loadingSpinner.setVisibility(View.VISIBLE);
        logUser(userTextEdit.getText().toString(), passwordTextEdit.getText().toString());
    }

    public void logUser(String username, String password) {
        new RequestHandler().logUser(this, username, password);
    }

    public void loginSucceeded(String username, JSONObject response) {
        loadingSpinner.setVisibility(View.INVISIBLE);
        showToast("Bienvenido " + userTextEdit.getText().toString());
        saveSession(username, response);
        sendToMenu();
    }

    public void loginFailed() {
        loadingSpinner.setVisibility(View.INVISIBLE);
        showToast("Usuario o contraseña incorrectos");
        sendToMenu();
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
        Intent intent = new Intent(this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    public void askForPermissions() {
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
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);

        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

