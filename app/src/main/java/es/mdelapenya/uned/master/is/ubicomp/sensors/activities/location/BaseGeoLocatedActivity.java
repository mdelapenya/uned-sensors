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

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.telephony.TelephonyManager;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import es.mdelapenya.uned.master.is.ubicomp.sensors.activities.BaseAndroidBusRegistrableActivity;
import es.mdelapenya.uned.master.is.ubicomp.sensors.interactors.SensorsInteractor;
import es.mdelapenya.uned.master.is.ubicomp.sensors.interactors.SensorsMetricInteractor;
import es.mdelapenya.uned.master.is.ubicomp.sensors.pojo.SensorMetric;

import java.util.Date;
import java.util.UUID;

/**
 * @author Manuel de la Peña
 */
public class BaseGeoLocatedActivity extends BaseAndroidBusRegistrableActivity
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

            this.speed = new Float(speed);

            lastLocation = currentLocation;

            SensorMetric metric = new SensorMetric(
                uniqueDeviceId, currentLocation.getLatitude(), currentLocation.getLongitude(),
                speed, "speed", "km/h", new Date().getTime());

            SensorsInteractor sensorsInteractor = new SensorsMetricInteractor(metric);

            new Thread(sensorsInteractor).start();
        }
    }

    protected float getSpeed() {
        return speed;
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

        if (uniqueDeviceId == null) {
            uniqueDeviceId = getUniqueDeviceId();
        }
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
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
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
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE
        };

        for (String permission : permissions) {
            checkRuntimePermission(permission);
        }
    }

    private String getUniqueDeviceId() {
        TelephonyManager telephonyManager =
            (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        final String deviceId = telephonyManager.getDeviceId();
        final String simSerialNumber = telephonyManager.getSimSerialNumber();
        final String androidId = android.provider.Settings.Secure.getString(
            getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(
            androidId.hashCode(), ((long)deviceId.hashCode() << 32) | simSerialNumber.hashCode());

        return deviceUuid.toString();
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
    private float speed;
    private String uniqueDeviceId;

}
