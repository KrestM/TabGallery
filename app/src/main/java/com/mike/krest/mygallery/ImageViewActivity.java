package com.mike.krest.mygallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Activity responding for display of images in selected album
 */
public class ImageViewActivity extends ActionBarActivity {

    private ArrayList<String> mImageIDs;
    private String mCurrentImageID;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        // Adding toolbar instead of standard action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle args = getIntent().getExtras();

        mImageIDs = args.getStringArrayList(AlbumHolderFragment.ALBUM_NAME);
        mCurrentImageID = args.getString(AlbumHolderFragment.EXTRA_RES_ID);
        mPosition = args.getInt(AlbumHolderFragment.EXTRA_POSITION);

        ViewPager albumViewPager = (ViewPager) findViewById(R.id.album_view_pager);

        AlbumAdapter albumAdapter = new AlbumAdapter(getSupportFragmentManager(), mImageIDs, mCurrentImageID);
        albumViewPager.setAdapter(albumAdapter);

        albumViewPager.setCurrentItem(mPosition); // TODO: check is it work properly everytime or not
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
