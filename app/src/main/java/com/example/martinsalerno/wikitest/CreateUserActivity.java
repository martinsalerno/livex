package com.example.martinsalerno.wikitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private EditText passwordConfirmation;
    private Button buttonConfirm;
    private ProgressBar progressBar;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        progressBar = findViewById(R.id.createLoadingSpinner);
        progressBar.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.toolbarCreateUser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.createLoadingSpinner);
        username = findViewById(R.id.createUsuario);
        password = findViewById(R.id.createPassword);
        passwordConfirmation = findViewById(R.id.createPasswordConfirm);
        buttonConfirm = findViewById(R.id.createUserButton);
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        hideKeyboard();
        if (username.getText().toString() == null || password.getText().toString() == null || passwordConfirmation.getText().toString() == null ) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT);
            return;
        }
        if (username.getText().toString().length() < 4) {
            Toast.makeText(this, "El nombre de usuario debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.getText().toString().length() < 4) {
            Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            Toast.makeText(this, "La contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        new RequestHandler().registerUser(this, username.getText().toString(), password.getText().toString());
    }

    public void userNotCreated() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "El usuario no se pudo crear", Toast.LENGTH_SHORT).show();
    }

    public void userCreated() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "El usuario se creó correctamente", Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
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
