package com.mike.krest.mygallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Mikhail Krestiyaninov on 24.11.14.
 */

/**
 * Adapter for showing images from selected album
 */
public class AlbumAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mImageIDs;
    private String mCurrentImageID;

    public AlbumAdapter(FragmentManager supportFragmentManager, ArrayList<String> imageIDs, String currentImageID) {
        super(supportFragmentManager);
        this.mImageIDs = imageIDs;
        this.mCurrentImageID = currentImageID;
    }

    @Override
    public Fragment getItem(int i) {
        ImageHolderFragment imageHolderFragment = new ImageHolderFragment();

        Bundle args = new Bundle();
        args.putString(ImageHolderFragment.RES_ID, mImageIDs.get(i));
        imageHolderFragment.setArguments(args);

        return imageHolderFragment;
    }

    @Override
    public int getCount() {
        return mImageIDs.size();
    }
}
