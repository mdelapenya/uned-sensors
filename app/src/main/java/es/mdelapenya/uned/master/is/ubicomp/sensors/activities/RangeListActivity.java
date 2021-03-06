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

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.adapters.DismissRangesTouchHelper;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.services.CRUDService;

import java.util.List;

/**
 * An activity representing a list of Ranges. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RangeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * @author Manuel de la Peña
 */
public class RangeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_range_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(setClickListener(new Range()));

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.range_list);

        assert recyclerView != null;

        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.range_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Range> ranges = new RangeService(this).list();

        RangesAdapter adapter = new RangesAdapter(ranges);

        recyclerView.setAdapter(adapter);

        setUpDismissFavoritesTouchGesture(adapter, recyclerView);
    }

    private void setUpDismissFavoritesTouchGesture(
        RangesAdapter rangesAdapter, RecyclerView recyclerView) {

        ItemTouchHelper.Callback callback = new DismissRangesTouchHelper(rangesAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);

        helper.attachToRecyclerView(recyclerView);
    }

    private View.OnClickListener setClickListener(final Range range) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();

                    arguments.putLong(RangeDetailFragment.ARG_RANGE_ID, range.getId());

                    RangeDetailFragment fragment = new RangeDetailFragment();

                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.range_detail_container, fragment)
                        .commit();
                }
                else {
                    Context context = v.getContext();

                    Intent intent = new Intent(context, RangeDetailActivity.class);

                    intent.putExtra(RangeDetailFragment.ARG_RANGE_ID, range.getId());

                    context.startActivity(intent);
                }
            }

        };
    }

    public class RangesAdapter extends RecyclerView.Adapter<RangesAdapter.RangeViewHolder> {

        private Context context;
        private final List<Range> ranges;

        public RangesAdapter(List<Range> ranges) {
            this.ranges = ranges;
        }

        @Override
        public RangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();

            View view = LayoutInflater.from(context)
                .inflate(R.layout.range_list_content, parent, false);

            return new RangeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RangeViewHolder holder, int position) {
            holder.range = ranges.get(position);
            holder.mIdView.setText(String.valueOf(holder.range.getId()));
            holder.mContentView.setText(holder.range.toString());

            holder.mView.setOnClickListener(setClickListener(holder.range));
        }

        @Override
        public int getItemCount() {
            return ranges.size();
        }

        public void remove(RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getAdapterPosition();

            CRUDService rangeService = new RangeService(context);

            Range range = ranges.get(position);

            rangeService.delete(range);

            ranges.remove(position);

            notifyItemRemoved(position);

            Snackbar.make(
                    viewHolder.itemView, context.getString(R.string.range_deleted_ok),
                    Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        }

        public class RangeViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Range range;

            public RangeViewHolder(View view) {
                super(view);

                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

    }

}