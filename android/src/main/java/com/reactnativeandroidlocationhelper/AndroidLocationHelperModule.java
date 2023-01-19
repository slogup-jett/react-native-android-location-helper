package com.reactnativeandroidlocationhelper;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.os.Build;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;

@ReactModule(name = AndroidLocationHelperModule.NAME)
public class AndroidLocationHelperModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidLocationHelper";

    public AndroidLocationHelperModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    private boolean checkPermission() {
        Activity activity = this.getCurrentActivity();

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    @ReactMethod
    public void isMock(Promise promise) {
        if (!this.checkPermission()) {
            promise.reject("location permission denied.");
            return;
        }

        Activity activity = this.getCurrentActivity();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    promise.reject("can not get location.");
                    return;
                }

                if (android.os.Build.VERSION.SDK_INT >= 31) {
                    promise.resolve(location.isMock());
                    return;
                }

                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    promise.resolve(location.isFromMockProvider());
                    return;
                }

                promise.resolve(AndroidLocationHelperModule.this.isAllowedMockLocation());
            }
        });
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public boolean isAllowedMockLocation() {
        return !Settings.Secure.getString(
            this.getCurrentActivity().getContentResolver(),
            Settings.Secure.ALLOW_MOCK_LOCATION
        ).equals("0");
    }
}
