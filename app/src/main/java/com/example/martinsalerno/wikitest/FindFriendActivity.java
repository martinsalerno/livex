package com.example.martinsalerno.wikitest;

import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.martinsalerno.wikitest.classes.Commerce;
import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.Position;
import com.example.martinsalerno.wikitest.location.LocationTracker;
import com.google.gson.Gson;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.martinsalerno.wikitest.location.LocationProvider;

public class FindFriendActivity extends AppCompatActivity {
    private LocationProvider locationProvider;
    private LocationTracker tracker;
    private ArchitectView architectView;
    private String friend;
    private Position position;
    private final String TAG = "FIND_FRIEND_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gson gson = new Gson();
        friend = getIntent().getStringExtra("friend");
        position = gson.fromJson(getIntent().getStringExtra("position"), Position.class);
        Log.d("STRINGGGGG", position.toString());
        this.architectView = (ArchitectView)this.findViewById(R.id.architectView);
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey("TsTbN1sQuxf7XTEJWq+KxgA4pdjpgd3Dgi2i9mZqzOFYn/AazPuogfgDRz6w+X9LaPAzMErVGBhH2fDUbobGH8Tf/pGGFuE8eEsUcAnXhTY2fB0OOWWA8WfgExrKeMwVSZYcqkyrdR/JfxS/T6ddwZ02gzCgxWOt1b+KWbpNoIFTYWx0ZWRfX9F26p8J9zHwTK5iZCOTM6gAGHwIeJwKOffVR5b8V3y+EuieFBogW5fwNl9EDAz6aVI+YkGfcuI5Vnr0ZQWXlsblMVRrOsyx9n7mf+XMHPkk6dHgNhdZPK+4+G7sTd/UMVNl0U3hZGejzUGXTtzG44vHa5VOjT3v/Crmwn5StAEsIRpvuFfHu7ugtyTKBLu053NcBTZVh4SvCPR/kpqkf1xQUX9ePVI5at8y3jN3RBVAKPTq2UIgxhMmmqHty+zZjT7BPqSHMd3NSUFO6SDdA5ddSyZUQkoJsnLeBw5lktOd0WnwJaGKXM1CFvuBXqzw4njK8LjiEul6EFw1MVY+0Hfc2OOuCDeGp3e5NR7mccu+SJhfEDwepOhRdXd7q98B0VoCYnxu4ob52wA1X15LGAWjMCwLcipQ1TPcoY5wktC+3vrGYjv5lb4lEZv2Fl7oaODUVlK3AkqCs/wXPHg7mWy3FTDq+2VqKf2GIWXfu1y8qwkUq7Mb3wwXQYR5WQpz67fzrVttUqG0MHimgIeweaAx058UXaf3iWUhblF8lfMir8mgum+9E3SpYkiBDhEljWs5efrckeXz3vA6P6C/nManXib6BMaZ/w==");
        this.architectView.onCreate(config);
        tracker = new LocationTracker(this) {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    if (location != null && FindFriendActivity.this.architectView != null) {
                        if (location.hasAltitude() && location.hasAccuracy() && location.getAccuracy() < 7) {
                            FindFriendActivity.this.architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
                        } else {
                            FindFriendActivity.this.architectView.setLocation(location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000);
                        }
                    }
                } catch (Exception e){

                }
            }
            @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override public void onProviderEnabled(String s) {}
            @Override public void onProviderDisabled(String s) {}
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();
        try {
            this.architectView.load( "file:///android_asset/demo6/index.html" );

        } catch(Exception e) {
            Log.d(TAG, e.toString());
        }

        JSONArray array = new JSONArray();
        JSONObject poi = new JSONObject();
        try {
            poi.put("id", 1);
            poi.put("longitude", position.getLatitud());
            poi.put("latitude", position.getLongitud());
            poi.put("description", "Amigo");
            poi.put("name",  friend);
        } catch (JSONException e) {
                e.printStackTrace();
        }
        array.put(poi);
        Log.d(TAG, array.toString());
        this.architectView.callJavascript("World.loadPoisFromJsonData(" + array.toString() + ");");
    }

    @Override
    protected void onResume() {
        super.onResume();
        architectView.onResume();
        tracker.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        architectView.onDestroy();
        tracker.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        architectView.onPause();
        tracker.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}