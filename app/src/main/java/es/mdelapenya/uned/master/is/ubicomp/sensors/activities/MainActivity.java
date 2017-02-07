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

import android.content.Intent;

import android.location.Location;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.activities.location.BaseGeoLocatedActivity;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.ResourceLocator;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.SpeedConverter;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.UIManager;

/**
 * @author Manuel de la Peña
 */
public class MainActivity extends BaseGeoLocatedActivity {

    private TextView currentSpeed;
    private TextView currentSpeedText;
    private String oldSpeed = "0.00";
    private String oldSpeedText = "";
    private List<Range> ranges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        currentSpeed = (TextView) findViewById(R.id.current_speed);
        currentSpeedText = (TextView) findViewById(R.id.current_speed_text);

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
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateUI();
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

    private String getRangeName(float currentSpeed) {
        for (Range range : ranges) {
            if (range.isInRange(currentSpeed)) {
                int resourceByName = ResourceLocator.getStringResourceByName(this, range.getName());

                if (resourceByName > 0) {
                    return this.getString(resourceByName);
                }

                return range.getName();
            }
        }

        return this.getString(R.string.status_stopped);
    }

    private String roundSpeed(float speed) {
        NumberFormat numberFormatInstance = NumberFormat.getNumberInstance(Locale.getDefault());

        DecimalFormat decimalFormat = (DecimalFormat)numberFormatInstance;

        return decimalFormat.format(speed);
    }

    private void showConfiguration() {
        Intent intent = new Intent(this, RangeListActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getApplicationContext().startActivity(intent);
    }

    private void showHelp() {
        Toast.makeText(
            MainActivity.this, "Help! I need somebody! Help!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        float speedKm = SpeedConverter.convertToKmsh(getSpeed());

        String rangeName = getRangeName(speedKm);
        String speedValue = roundSpeed(speedKm);

        if (!UIManager.syncUIRequired(speedValue, oldSpeed, rangeName, oldSpeedText)) {
            return;
        }

        currentSpeed.setText(speedValue);
        currentSpeedText.setText(rangeName);

        oldSpeed = speedValue;
        oldSpeedText = rangeName;
    }

}
