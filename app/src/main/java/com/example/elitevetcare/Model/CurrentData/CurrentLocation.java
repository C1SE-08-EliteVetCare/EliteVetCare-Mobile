package com.example.elitevetcare.Model.CurrentData;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CurrentLocation {
    private static CurrentLocation instance = null;
    private static Location currentLocation = null;
    private static FusedLocationProviderClient fusedLocationClient = null;

    public static void init(Location location) {
        instance = new CurrentLocation();
        currentLocation = location;
    }

    public static CurrentLocation getInstance() {
        if (instance == null)
            instance = new CurrentLocation();
        return instance;
    }

    public static Location getCurrentLocation() {
        return CurrentLocation.getInstance().currentLocation;
    }

    public static FusedLocationProviderClient getFusedLocation() {
        return CurrentLocation.getInstance().fusedLocationClient;
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    public void setFusedLocation(FusedLocationProviderClient fusedLocation) {
        fusedLocationClient = fusedLocation;
    }

    public static void GetCurrentLocationByAPI(Activity activity,OnSuccesLoad callback) {
        if (instance == null)
            instance = new CurrentLocation();
        if (CurrentLocation.getInstance().getFusedLocation() == null)
            CurrentLocation.getInstance().setFusedLocation(LocationServices.getFusedLocationProviderClient(activity.getApplicationContext()));
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Log.d("Location", "DaDen");
        CurrentLocation.getInstance().getFusedLocation().getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null)
                            CurrentLocation.getInstance().setCurrentLocation(location);
                        callback.OnSuccess();
                    }
                });
    }

    public interface OnSuccesLoad{
        void OnSuccess();
    }
}
