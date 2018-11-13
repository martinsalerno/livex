package com.example.martinsalerno.wikitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.classes.Position;
import com.example.martinsalerno.wikitest.fragments.EventsFragment;
import com.example.martinsalerno.wikitest.fragments.FriendsFragment;
import com.example.martinsalerno.wikitest.fragments.HomeFragment;
import com.example.martinsalerno.wikitest.fragments.ProfileFragment;
import com.example.martinsalerno.wikitest.fragments.ScanFragment;
import com.google.gson.Gson;

import java.util.ArrayDeque;
import java.util.Deque;


public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    Deque<Integer> fragmentsStack = new ArrayDeque<>();
    boolean isBackPressed  = false;
    private static final String SAVE_INSTANCE_FRAGMENT_KEY = "fragment";
    private int fragment_id;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (fragmentsStack.peek() != null) {
                if (item.getItemId() == fragmentsStack.peek()) {
                    return true;
                }
            }
            return switchItemId(item.getItemId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setSelectedItemId(R.id.navigation_home);
        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(SAVE_INSTANCE_FRAGMENT_KEY);
            Log.d("BOTTOM EN EL IF", Integer.toString(id));
            switchItemId(id);
        } else {
            pushFragmentIntoStack(R.id.navigation_home);
            fragment_id = R.id.navigation_home;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pushFragmentIntoStack(int id) {
        if(fragmentsStack.size() < 5) {
            fragmentsStack.push(id);
        }
        else {
            fragmentsStack.removeLast();
            fragmentsStack.push(id);
        }
    }

    private void setFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(fragmentsStack.size() > 1)
        {
            isBackPressed = true;
            fragmentsStack.pop();
            navigationView.setSelectedItemId(fragmentsStack.peek());
            switchItemId(fragmentsStack.peek());
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("BOTTOM", Integer.toString(fragment_id));
        outState.putInt(SAVE_INSTANCE_FRAGMENT_KEY, fragment_id);
    }

    public boolean switchItemId(int id) {
        fragment_id = id;
        switch (id) {
            case R.id.navigation_home:
                if (!isBackPressed) {
                    pushFragmentIntoStack(R.id.navigation_home);
                }
                isBackPressed = false;
                setFragment(new HomeFragment(), "HOME_FRAGMENT");
                return true;
            case R.id.navigation_shows:
                if (!isBackPressed) {
                    pushFragmentIntoStack(R.id.navigation_shows);
                }
                isBackPressed = false;
                setFragment(new EventsFragment(), "EVENTS_FRAGMENT");
                return true;
            case R.id.navigation_scan:
                if (!isBackPressed) {
                    pushFragmentIntoStack(R.id.navigation_scan);
                }
                isBackPressed = false;
                setFragment(new ScanFragment(), "SCAN_FRAGMENT");
                return true;
            case R.id.navigation_friends:
                if (!isBackPressed) {
                    pushFragmentIntoStack(R.id.navigation_friends);
                }
                isBackPressed = false;
                setFragment(new FriendsFragment(), "FRIENDS_FRAGMENT");
                return true;
            case R.id.navigation_profile:
                if (!isBackPressed) {
                    pushFragmentIntoStack(R.id.navigation_profile);
                }
                isBackPressed = false;
                setFragment(new ProfileFragment(), "PROFILE_FRAGMENT");
                return true;
            default:
                return false;
        }
    }

    public void setFriendPosition(Position position, String userId, String username) {
        Toast.makeText(this, "Iniciando localización", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FindFriendActivity.class);
        Gson gson = new Gson();
        String positionJSON = gson.toJson(position);
        intent.putExtra("position", positionJSON);
        intent.putExtra("friend", username);
        startActivity(intent);

    }

    public void setFriendPositionNotOk() {
        Toast.makeText(this, "No se pudo obtener la ubicación del usuario", Toast.LENGTH_SHORT).show();
    }
}
