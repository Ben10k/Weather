package com.birdisaword.birdsweather;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.app.ActivityManager;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WidgetService extends Service implements LocationListener {
    String city;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private String provider;
    boolean isActivityFound;
    private HandleXML obj;

    public WidgetService() {
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);


        Location myLocation = getLastKnownLocation();

        if (myLocation != null)
            onLocationChanged(myLocation);

        obj = new HandleXML("http://api.wunderground.com/api/f7f4c08a99ea3a7b/conditions/lang:LT/q/CA/"+city+".xml");
        obj.fetchXML();
        while(obj.parsingComplete);


        Intent updateWidget = new Intent(this, WeatherWidget.class); // Widget.class is your widget class
        updateWidget.setAction("update_widget");
        updateWidget.putExtra("T", obj.getTemperature());
        updateWidget.putExtra("V",obj.getHumidity());
        PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            pending.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        // addresses = geo.getFromLocation(latitude, longitude, 1);
        try {
            addresses = geo.getFromLocation(latitude, longitude, 1);
            city = addresses.get(0).getLocality();
        } catch (IOException e) {
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
