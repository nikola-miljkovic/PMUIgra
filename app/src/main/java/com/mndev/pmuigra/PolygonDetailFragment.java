package com.mndev.pmuigra;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mndev.pmuigra.controller.StatisticsController;
import com.mndev.pmuigra.model.StatisticsEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Polygon detail screen.
 * This fragment is either contained in a {@link PolygonListActivity}
 * in two-pane mode (on tablets) or a {@link PolygonDetailActivity}
 * on handsets.
 */
public class PolygonDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private String polygonName;

    List<StatisticsEntry> statisticsEntries;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PolygonDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            polygonName = getArguments().getString(ARG_ITEM_ID);
        }

        try {
            statisticsEntries = StatisticsController.getInstance()
                    .loadContext(getActivity().getApplicationContext())
                    .getStatisticsForPolygon(polygonName);
        } catch (Exception exc) {
            exc.printStackTrace();

            statisticsEntries = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.polygon_detail, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.stats_list);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, statisticsEntries) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tvUsername = (TextView) view.findViewById(android.R.id.text1);
                TextView tvScore = (TextView) view.findViewById(android.R.id.text2);

                StatisticsEntry entry = statisticsEntries.get(position);
                tvUsername.setText(entry.getUsername());
                tvScore.setText(String.valueOf(entry.getScore()));

                return view;
            }
        };
        listView.setAdapter(adapter);

        return rootView;
    }
}
