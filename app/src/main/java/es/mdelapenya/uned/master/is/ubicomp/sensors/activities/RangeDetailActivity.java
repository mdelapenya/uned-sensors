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

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.MenuItem;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.services.CRUDService;

/**
 * @author Manuel de la Peña
 */

/**
 * An activity representing a single Range detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RangeListActivity}.
 */
public class RangeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_range_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RangeDetailFragment rangeDetailFragment =
                    (RangeDetailFragment) getSupportFragmentManager().findFragmentById(
                        R.id.range_detail_container);

                if (!rangeDetailFragment.isValidationSuccess()) {
                    Snackbar.make(
                            view, getString(R.string.validation_range), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                    return;
                }

                Range range = rangeDetailFragment.getRange();

                String message = getString(R.string.range_created_ok);

                if (range.getId() > 0) {
                    message = getString(R.string.range_updated_ok);
                }

                rangeDetailFragment.setRange(saveRange(range));

                Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();

            arguments.putLong(
                RangeDetailFragment.ARG_RANGE_ID,
                getIntent().getLongExtra(RangeDetailFragment.ARG_RANGE_ID, 0));

            RangeDetailFragment fragment = new RangeDetailFragment();

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                .add(R.id.range_detail_container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RangeListActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Range saveRange(Range range) {
        CRUDService<Range> rangeService = new RangeService(this);

        if (range.getId() > 0) {
            return rangeService.update(range);
        }

        return rangeService.add(range);
    }

}