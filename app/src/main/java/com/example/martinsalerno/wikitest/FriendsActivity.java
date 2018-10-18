package com.example.martinsalerno.wikitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import adapters.EventAdapter;
import adapters.FriendAdapter;
import classes.Friend;
import classes.RequestHandler;
import classes.SessionHandler;

public class FriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText searchFriends;
    RecyclerView recyclerFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbarFriends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        searchFriends = findViewById(R.id.buscarAmigo);
        recyclerFriends = findViewById(R.id.recyclerBuscar);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Amigos");
    }

    public void setFriends(Friend[] friendsFound){
        FriendAdapter adapter = new FriendAdapter(friendsFound, this);
        recyclerFriends.setAdapter(adapter);
    }

    public void searchFriends(View view){
        new RequestHandler().loadUsers(this, searchFriends.getText().toString());
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