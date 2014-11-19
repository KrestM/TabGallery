package com.mike.krest.mygallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mikhail Krestiyaninov on 19.11.14.
 */
public class TabPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Integer> mImageArray = new ArrayList<Integer>(
            Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3,
                    R.drawable.image4, R.drawable.image5, R.drawable.image6,
                    R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10));

    private ArrayList<Integer> mImageArrayOfDogs = new ArrayList<Integer>(
            Arrays.asList(R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3,
                    R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6,
                    R.drawable.sample_7));

    private final String[] TITLES = { "Album One", "Album Two" };
    
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int i) {
        Fragment albumFragment = new AlbumHolderFragment();
        Bundle args = new Bundle();
        switch (i) {
            case 0:
                args.putIntegerArrayList(AlbumHolderFragment.ALBUM_NAME, mImageArray);
                break;
            case 2:
                args.putIntegerArrayList(AlbumHolderFragment.ALBUM_NAME, mImageArrayOfDogs);
                break;
            default:
                args.putIntegerArrayList(AlbumHolderFragment.ALBUM_NAME, mImageArrayOfDogs);
        }
        albumFragment.setArguments(args);

        return albumFragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
