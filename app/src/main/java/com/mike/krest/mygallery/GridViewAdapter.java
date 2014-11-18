package com.mike.krest.mygallery;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
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
        ViewHolder viewHolder;

        CardView cardView = (CardView) convertView;

        if (cardView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            cardView = (CardView) layoutInflater.inflate(R.layout.item, parent, false);
            viewHolder = new ViewHolder();
            ImageView imageView = new ImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(PADDING, PADDING, PADDING, PADDING);

            cardView.addView(imageView);

            viewHolder.imageView = imageView;

            cardView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) cardView.getTag();
        }

        loadBitmap(viewHolder.imageView, mThumbIDs.get(position));
        return cardView;
    }

    private void loadBitmap(ImageView imageView, Integer resID) {
        final String imageKey = String.valueOf(resID);

        final Bitmap cacheBitmap = MainActivity.getBitmapFromMemCache(imageKey);

        if (cacheBitmap != null) {
            imageView.setImageBitmap(cacheBitmap);
        } else {
            if (cancelPotentialWork(imageView, resID)) {
                final AsyncLoadImage task = new AsyncLoadImage(imageView, mResources, WIDTH, HEIGHT);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(resID);
            }
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
            return mResources.getDisplayMetrics().widthPixels/3;
        else
            return mResources.getDisplayMetrics().widthPixels/4;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
