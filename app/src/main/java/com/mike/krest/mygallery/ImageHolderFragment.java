package com.mike.krest.mygallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Mikhail Krestiyaninov on 24.11.14.
 */
public class ImageHolderFragment extends Fragment {

    public static final String RES_ID = "com.mike.krest.mygallery.id";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        String mResID = args.getString(RES_ID);

        ImageView imageView = (ImageView) inflater.inflate(R.layout.album_image, container, false);


        // TODO: make async loading of images through bitmaps
        Uri image = Uri.parse(mResID);
        imageView.setImageURI(image);

        return imageView;
    }
}
