package com.mike.krest.mygallery;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Mikhail Krestiyaninov on 19.11.14.
 */

/**
 * Fragment holding data connecting with every albums
 */
public class AlbumHolderFragment extends Fragment {
    public static final String ALBUM_NAME = "com.mike.krest.mygallery.albumname";
    public static final String EXTRA_RES_ID = "com.mike.krest.mygallery.resid";
    public static final String EXTRA_POSITION = "com.mike.krest.mygallery.position";
    private ArrayList<String> mThumbNailIDs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mThumbNailIDs = args.getStringArrayList(ALBUM_NAME);
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

        gridView.setAdapter(new GridViewAdapter(getActivity(), mThumbNailIDs));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // After click on a thumbnail start the activity which will be to show images from selected album
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imageID = String.valueOf(parent.getItemAtPosition(position));

                Bundle args = new Bundle();
                args.putStringArrayList(ALBUM_NAME, mThumbNailIDs);
                args.putString(EXTRA_RES_ID, imageID);
                args.putInt(EXTRA_POSITION, position);

                Intent intentShowAlbum = new Intent(getActivity(), ImageViewActivity.class);
                intentShowAlbum.putExtras(args);

                startActivity(intentShowAlbum);
            }
        });
        return gridView;
    }
}
