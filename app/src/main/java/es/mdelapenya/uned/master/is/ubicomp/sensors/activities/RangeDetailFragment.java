package es.mdelapenya.uned.master.is.ubicomp.sensors.activities;

import android.app.Activity;

import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import es.mdelapenya.uned.master.is.ubicomp.sensors.R;
import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db.RangeDAO;
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

    /**
     * The range this fragment is presenting.
     */
    private Range range;

    /**
     * The title this fragment is presenting
     */
    private String title;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RangeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_RANGE_ID)) {
            long rangeId = getArguments().getLong(ARG_RANGE_ID);

            range = new RangeDAO(getContext()).getRange(rangeId);

            Activity activity = this.getActivity();

            CollapsingToolbarLayout appBarLayout =
                (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null) {
                title = getContext().getString(
                    ResourceLocator.getStringResourceByName(getContext(), range.getName()));

                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.range_detail, container, false);

        if (range != null) {
            ((TextView) rootView.findViewById(R.id.range_detail)).setText(range.toString());
        }

        return rootView;
    }

}