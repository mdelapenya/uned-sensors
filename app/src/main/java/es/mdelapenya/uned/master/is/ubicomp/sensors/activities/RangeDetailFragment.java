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

import android.app.Activity;

import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db.CriterionImpl;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db.RangeDBHelper;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services.RangeService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.ResourceLocator;

/**
 * A fragment representing a single Range detail screen.
 * This fragment is either contained in a {@link RangeListActivity}
 * in two-pane mode (on tablets) or a {@link RangeDetailActivity}
 * on handsets.
 *
 * @author Manuel de la Peña
 */
public class RangeDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RANGE_ID = "range_id";

    private TextView lbDetail;
    private EditText txtName;
    private EditText txtMin;
    private EditText txtMax;

    /**
     * The range this fragment is presenting.
     */
    private Range range;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RangeDetailFragment() {
    }

    /**
     * @return the Range this fragment is representing
     */
    public Range getRange() {
        return range;
    }

    /**
     * @return the status of the validation process, based on the existence of ranges with same min
     *         or max values.
     */
    public boolean isValidationSuccess() {
        RangeService rangeService = new RangeService(getContext());

        List<Range> minRanges = rangeService.findBy(
            new CriterionImpl(RangeDBHelper.Range.COLUMN_NAME_MIN, range.getMin()));

        if (checkDuplicates(minRanges)) {
            txtMin.requestFocus();

            return false;
        }

        List<Range> maxRanges = rangeService.findBy(
            new CriterionImpl(RangeDBHelper.Range.COLUMN_NAME_MAX, range.getMax()));

        if (checkDuplicates(maxRanges)) {
            txtMax.requestFocus();

            return false;
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_RANGE_ID)) {
            long rangeId = getArguments().getLong(ARG_RANGE_ID);

            if (rangeId > 0) {
                range = new RangeService(getContext()).get(rangeId);
            }
            else {
                range = new Range();
            }
        }
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.range_detail, container, false);

        lbDetail = (TextView) rootView.findViewById(R.id.range_detail);
        txtName = (EditText) rootView.findViewById(R.id.range_name);
        txtMin = (EditText) rootView.findViewById(R.id.range_min);
        txtMax = (EditText) rootView.findViewById(R.id.range_max);

        String title = getContext().getString(R.string.new_range);

        if (range.getId() > 0) {
            title = range.getName();

            int resourceByName = ResourceLocator.getStringResourceByName(
                getContext(), range.getName());

            if (resourceByName != 0) {
                title = getContext().getString(resourceByName);
            }
        }

        range.setName(title);

        lbDetail.setText(range.toString());
        txtName.setText(title);
        txtMin.setText(String.valueOf(range.getMin()));
        txtMax.setText(String.valueOf(range.getMax()));

        Activity activity = this.getActivity();

        final CollapsingToolbarLayout appBarLayout =
            (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }

        txtName.addTextChangedListener(createNameTextWatcher(appBarLayout));
        txtMin.addTextChangedListener(createMinTextWatcher(lbDetail));
        txtMax.addTextChangedListener(createMaxTextWatcher(lbDetail));

        return rootView;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    /**
     * Checks if a range is duplicated, being possible to exist only one range with MIN or MAX field.
     *
     * It also checks if the range this fragment is representing is the one in the list, which means
     * that the user is updating that range, so no min/max validation is required.
     *
     * @param ranges a filtered collection of ranges
     * @return if there is a duplicate range in the collection
     */
    private boolean checkDuplicates(List<Range> ranges) {
        if (ranges.size() == 0) {
            return false;
        }

        if (ranges.get(0).getId() == range.getId()) {
            return false;
        }

        return true;
    }

    private TextWatcher createMaxTextWatcher(final TextView lbDetail) {
        return new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.length() > 0) {
                    range.setMax(Integer.parseInt(c.toString()));
                }
                else {
                    range.setMax(0);
                }

                lbDetail.setText(range.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // NOOP
            }

            public void afterTextChanged(Editable c) {
                // NOOP
            }

        };
    }

    private TextWatcher createMinTextWatcher(final TextView lbDetail) {
        return new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.length() > 0) {
                    range.setMin(Integer.parseInt(c.toString()));
                }
                else {
                    range.setMin(0);
                }

                lbDetail.setText(range.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // NOOP
            }

            public void afterTextChanged(Editable c) {
                // NOOP
            }

        };
    }

    private TextWatcher createNameTextWatcher(final CollapsingToolbarLayout appBarLayout) {
        return new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                appBarLayout.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // NOOP
            }

            public void afterTextChanged(Editable c) {
                // NOOP
            }

        };
    }

}