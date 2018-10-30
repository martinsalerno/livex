package com.example.martinsalerno.wikitest.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class LocationIntentService extends IntentService {

    public static final String ACTION_END = "com.example.martinsalerno.wikitest.location.END";

    public LocationIntentService() {
        super("Location Service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d("LOCATIONINTENT", "STARTING INTENT SERVICE ON HANDLEINTENT");
        LocationTracker tracker = new LocationTracker(this);
        RequestHandler handler = new RequestHandler();
        if (intent != null) {
            while (true) {
                try {
                    Thread.sleep(40000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Location location = tracker.getLocation();
                if (location != null) {
                    String userId = intent.getStringExtra("userId");
                    Log.d("intent service", userId);
                    Log.d("intent service latitude", Double.toString(location.getLatitude()));
                    Log.d("intent service longitude", Double.toString(location.getLongitude()));
                    handler.sendLocation(getApplicationContext(), userId, location.getLatitude(), location.getLongitude());
                }
            }
        }
    }
}
