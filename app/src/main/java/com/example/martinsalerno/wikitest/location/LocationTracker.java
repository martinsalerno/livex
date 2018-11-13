package com.example.martinsalerno.wikitest.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationTracker extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location; // com.example.martinsalerno.wikitest.location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 20; // 1 minute
    protected LocationManager locationManager;

    public LocationTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (mContext.checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationTracker.this);
        }
    }


    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onResume(){
        getLocation();
    }

    public void onPause(){

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}