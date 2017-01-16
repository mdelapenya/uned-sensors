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

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.adapters.DismissRangesTouchHelper;
import es.mdelapenya.uned.master.is.ubicomp.sensors.adapters.RangesAdapter;
import es.mdelapenya.uned.master.is.ubicomp.sensors.db.RangeDAO;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;

/**
 * @author Manuel de la Peña
 */
public class RangesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

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

        try (RangeDAO rangeDAO = new RangeDAO(this)){
            List<Range> ranges = rangeDAO.getAllRanges();

            recyclerView = (RecyclerView) findViewById(R.id.rangesList);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            RangesAdapter rangesAdapter = new RangesAdapter(ranges);

            setUpDismissFavoritesTouchGesture(rangesAdapter);

            recyclerView.setAdapter(rangesAdapter);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpDismissFavoritesTouchGesture(RangesAdapter rangesAdapter) {
        ItemTouchHelper.Callback callback = new DismissRangesTouchHelper(rangesAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);

        helper.attachToRecyclerView(recyclerView);
    }

}
