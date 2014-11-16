package com.mike.krest.mygallery;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Mikhail Krestiyaninov on 12.11.14.
 */
public class GridViewAdapter extends BaseAdapter {

    private static final int PADDING = 0;
    private static int WIDTH;
    private static int HEIGHT;

    private List<Integer> mThumbIDs;
    private Context mContext;
    private Resources mResources;
    private Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(mResources, R.drawable.ic_launcher);

    public GridViewAdapter(Context context, List<Integer> mImageArray) {
        mContext = context;
        mResources = mContext.getResources();
        this.mThumbIDs = mImageArray;

        WIDTH = getSize();
        HEIGHT = getSize();
    }

    @Override
    public int getCount() {
        return mThumbIDs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if (imageView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
        }

        loadBitmap(imageView, mResources, mThumbIDs.get(position));
        return imageView;
    }

    private void loadBitmap(ImageView imageView, Resources mResources, Integer resID) {
        if (cancelPotentialWork(imageView, resID)) {
            final AsyncLoadImage task = new AsyncLoadImage(imageView, mResources, WIDTH, HEIGHT);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resID);
        }
    }

    private boolean cancelPotentialWork(ImageView imageView, Integer resID) {
        final AsyncLoadImage task = getAsyncLoadImageTask(imageView);

        if (task != null) {
            final int loadingResID = task.mResID;
            if (loadingResID != resID || loadingResID == 0)
                task.cancel(true);
            else
                return false;
        }
        return true;
    }

    static AsyncLoadImage getAsyncLoadImageTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getAsyncLoadImage();
            }
        }
        return null;
    }

    private int getSize() {
        if (mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return mResources.getDisplayMetrics().widthPixels/2;
        else
            return mResources.getDisplayMetrics().widthPixels/4;
    }
}
