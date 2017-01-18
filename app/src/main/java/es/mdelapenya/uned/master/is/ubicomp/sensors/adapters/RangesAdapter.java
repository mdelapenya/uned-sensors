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

package es.mdelapenya.uned.master.is.ubicomp.sensors.adapters;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.services.CRUDService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.ResourceLocator;

/**
 * @author Manuel de la Peña
 */
public class RangesAdapter extends RecyclerView.Adapter<RangesAdapter.RangeViewHolder> {

    private final List<Range> ranges;

    private Context context;

    public RangesAdapter(List<Range> ranges) {
        this.ranges = ranges;
    }

    @Override
    public RangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.range_row, parent, false);

        return new RangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RangeViewHolder holder, int position) {
        holder.bind(ranges.get(position));
    }

    @Override
    public int getItemCount() {
        return ranges.size();
    }

    public void remove(int position) {
        CRUDService rangeService = new RangeService(context);

        Range range = ranges.get(position);

        rangeService.delete(range);

        ranges.remove(position);

        notifyItemRemoved(position);

        Toast.makeText(
            context, context.getString(R.string.range_deleted_ok), Toast.LENGTH_SHORT)
        .show();
    }

    public class RangeViewHolder extends RecyclerView.ViewHolder {

        private Range currentRange;
        private TextView rangeId;
        private String rangeIdText;
        private TextView rangeValues;
        private TextView rangeName;

        public RangeViewHolder(View itemView) {
            super(itemView);

            rangeId = (TextView) itemView.findViewById(R.id.rangeId);
            rangeName = (TextView) itemView.findViewById(R.id.rangeName);
            rangeValues = (TextView) itemView.findViewById(R.id.rangeValues);

            View.OnClickListener rangeClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(context, currentRange.toString(), Toast.LENGTH_SHORT).show();
                }

            };

            rangeId.setOnClickListener(rangeClickListener);
            rangeName.setOnClickListener(rangeClickListener);
            rangeValues.setOnClickListener(rangeClickListener);
        }

        public void bind(Range range) {
            currentRange = range;

            rangeIdText = String.valueOf(range.getId());

            rangeId.setText("(" + rangeIdText + ")");
            rangeName.setText(
                context.getString(
                    ResourceLocator.getStringResourceByName(context, range.getName())));
            rangeValues.setText(String.valueOf(range.toString()));
        }

    }

}
