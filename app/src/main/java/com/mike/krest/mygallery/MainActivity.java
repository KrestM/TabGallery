package com.mike.krest.mygallery;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {

    private ArrayList<Integer> mImageArray = new ArrayList<Integer>(
            Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3,
                    R.drawable.image4, R.drawable.image5, R.drawable.image6,
                    R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GridView gridView = new GridView(this);

        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridView.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 2);
        else
            gridView.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 4);
        setContentView(gridView);

        gridView.setAdapter(new GridViewAdapter(this, mImageArray));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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