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

package es.mdelapenya.uned.master.is.ubicomp.sensors.activities;

import android.content.Context;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import android.location.Location;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.activities.location.BaseGeoLocatedActivity;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.ResourceLocator;

/**
 * @author Manuel de la Peña
 */
public class MainActivity extends BaseGeoLocatedActivity {

    private TextView currentSpeed;
    private ImageView currentSpeedImage;
    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;
    private List<Range> ranges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        if (linearAccelerationSensor != null){
            // Success! There's a linear acceleration sensor.
        }
        else {
            Toast.makeText(
                MainActivity.this,
                "No Linear Acceleration Sensor detected", Toast.LENGTH_SHORT).show();
        }

        currentSpeed = (TextView) findViewById(R.id.current_speed);
        currentSpeedImage = (ImageView) findViewById(R.id.current_speed_image);

        RangeService rangeService = new RangeService(this);

        ranges = rangeService.list();

        Collections.sort(ranges);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.sensors_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sensors_config:
                showConfiguration();

                return true;
            case R.id.sensors_help:
                showHelp();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateUI();
    }

    private void showConfiguration() {
        Intent intent = new Intent(MainActivity.this, RangesActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getApplicationContext().startActivity(intent);
    }

    private void showHelp() {
        Toast.makeText(
            MainActivity.this, "Help! I need somebody! Help!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        float speed = getSpeed();

        currentSpeed.setText(String.valueOf(speed));
        currentSpeedImage.setImageDrawable(getDrawable(getRangeImage(speed)));
    }

    private int getRangeImage(float currentSpeed) {
        int id = R.mipmap.status_walking;

        for (Range range : ranges) {
            if (range.isInRange(currentSpeed)) {
                id = ResourceLocator.getMipmapResourceByName(this, range.getName());

                break;
            }
        }

        return id;
    }

}
