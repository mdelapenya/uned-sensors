package es.mdelapenya.uned.master.is.ubicomp.sensors.activities;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;

/**
 * @author Manuel de la Peña
 */
public class RangesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ranges);

        Toolbar toolbar = (Toolbar) findViewById(R.id.rangesToolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton newRangeloatingActionButton = (FloatingActionButton) findViewById(
            R.id.newRangeFloatingActionButton);

        newRangeloatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
