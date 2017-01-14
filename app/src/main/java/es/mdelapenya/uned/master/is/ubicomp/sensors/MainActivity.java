package es.mdelapenya.uned.master.is.ubicomp.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_main);
    }

}