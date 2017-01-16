/**
 *    Copyright 2017-today Manuel de la Peña
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.mdelapenya.uned.master.is.ubicomp.sensors.activities.location;

import android.Manifest;
import android.app.Dialog;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * @author Manuel de la Peña
 */
public class BaseGeoLocatedActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient, GPS_REQUEST, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
            googleApiClient);

        double speed = 0;

        if (lastLocation != null) {
            speed = Math.sqrt(
                Math.pow(
                    currentLocation.getLongitude() - lastLocation.getLongitude(), 2) +
                    Math.pow(currentLocation.getLatitude() - lastLocation.getLatitude(), 2)
                ) / (currentLocation.getTime() - lastLocation.getTime());

            if (currentLocation.hasSpeed()) {
                speed = currentLocation.getSpeed();
            }

            lastLocation = currentLocation;
        }
    }

    protected float getSpeed() {
        return lastLocation.getSpeed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            return;
        }

        if (resultCode == RESULT_OK) {
            if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                googleApiClient.connect();
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(
                this, "Google Play Services must be installed.", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGooglePlayServices();
    }

    @Override
    protected void onPause() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private void checkRuntimePermission(String permission) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else {
                ActivityCompat.requestPermissions(
                    this, new String[]{ permission }, PERMISSION_REQUEST_READ_FINE_LOCATION);

                // PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void checkRuntimePermissions() {
        String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        };

        for (String permission : permissions) {
            checkRuntimePermission(permission);
        }
    }

    private void initializeGooglePlayServices() {
        checkRuntimePermissions();

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int checkGooglePlayServices = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            /*
            * google play services is missing or update is required
            *  return code could be
            * SUCCESS,
            * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
            * SERVICE_DISABLED, SERVICE_INVALID.
            */
            Dialog errorDialog = googleApiAvailability.getErrorDialog(this,
                checkGooglePlayServices, REQUEST_CODE_RECOVER_PLAY_SERVICES);

            errorDialog.show();

            return;
        }

        googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    private static final LocationRequest GPS_REQUEST = LocationRequest.create()
        .setInterval(10000)
        .setFastestInterval(3000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private static final int PERMISSION_REQUEST_READ_FINE_LOCATION = 1111;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

    private GoogleApiClient googleApiClient;
    private Location lastLocation;

}
