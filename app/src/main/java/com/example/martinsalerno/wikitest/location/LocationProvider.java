package com.example.martinsalerno.wikitest.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class LocationProvider {

    private final LocationListener locationListener;
    private final LocationManager locationManager;
    private final Context context;
    /** com.example.martinsalerno.wikitest.location updates should fire approximately every second */
    private static final int LOCATION_UPDATE_MIN_TIME_GPS = 100;
    /** com.example.martinsalerno.wikitest.location updates should fire, even if last signal is same than current one (0m distance to last com.example.martinsalerno.wikitest.location is OK) */
    private static final int LOCATION_UPDATE_DISTANCE_GPS = 0;
    /** com.example.martinsalerno.wikitest.location updates should fire approximately every second */
    private static final int LOCATION_UPDATE_MIN_TIME_NW = 100;
    /** com.example.martinsalerno.wikitest.location updates should fire, even if last signal is same than current one (0m distance to last com.example.martinsalerno.wikitest.location is OK) */
    private static final int LOCATION_UPDATE_DISTANCE_NW = 0;
    /** to faster access com.example.martinsalerno.wikitest.location, even use 10 minute old locations on start-up */
    private static final int LOCATION_OUTDATED_WHEN_OLDER_MS = 1000 * 60;
    /** is gpsProvider and networkProvider enabled in system settings */
    private boolean gpsProviderEnabled, networkProviderEnabled;

    public LocationProvider(final Context context, LocationListener locationListener) {
        super();
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = locationListener;
        this.gpsProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.networkProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void onPause() {
        if (this.locationListener != null && this.locationManager != null && (this.gpsProviderEnabled || this.networkProviderEnabled)) {
            this.locationManager.removeUpdates(this.locationListener);
        }
    }

    public void onResume() {
        if (this.locationManager != null && this.locationListener != null) {

            // check which providers are available are available
            this.gpsProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.networkProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /** is GPS provider enabled? */
            if (this.gpsProviderEnabled) {
                final Location lastKnownGPSLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if ( lastKnownGPSLocation != null && lastKnownGPSLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS ) {
                    locationListener.onLocationChanged( lastKnownGPSLocation );
                }
                if (locationManager.getProvider(LocationManager.GPS_PROVIDER)!=null) {
                    this.locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME_GPS, LOCATION_UPDATE_DISTANCE_GPS, this.locationListener );
                }
            }

            /** is Network / WiFi positioning provider available? */
            if ( this.networkProviderEnabled ) {
                final Location lastKnownNWLocation = this.locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
                if ( lastKnownNWLocation != null && lastKnownNWLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS ) {
                    locationListener.onLocationChanged( lastKnownNWLocation );
                }
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER)!=null) {
                    this.locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME_NW, LOCATION_UPDATE_DISTANCE_NW, this.locationListener );
                }
            }
        }
    }
}