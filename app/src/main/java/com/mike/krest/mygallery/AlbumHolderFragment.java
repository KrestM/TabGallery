package com.mike.krest.mygallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by Mikhail Krestiyaninov on 19.11.14.
 */
public class AlbumHolderFragment extends Fragment {
    public static final String ALBUM_NAME = "com.mike.krest.mygallery.albumname";
    private List<Integer> mThumbNailIDs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mThumbNailIDs = args.getIntegerArrayList(ALBUM_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridView gridView = new GridView(getActivity());

        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridView.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 3);
        else
            gridView.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 4);

//        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
//        layout.addView(gridView);

        gridView.setAdapter(new GridViewAdapter(getActivity(), mThumbNailIDs));
        return gridView;
    }
}
