package com.mike.krest.mygallery;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
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
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
        } else {
            imageView.setImageDrawable(null);
        }

//        imageView.setImageBitmap(decodeImageForGridView(mThumbIDs.get(position), WIDTH, HEIGHT, mContext.getResources()));
        new LoadImage(imageView).execute(mThumbIDs.get(position));
        return imageView;
    }

    private int getSize() {
        if (mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return mResources.getDisplayMetrics().widthPixels/2;
        else
            return mResources.getDisplayMetrics().widthPixels/4;
    }

    private static Bitmap decodeImageForGridView(int imageID, int reqWidth, int reqHeight, Resources resources) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, imageID, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Bitmap reuseBitmap = Bitmap.createBitmap(reqWidth, reqHeight, Bitmap.Config.ARGB_8888);

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, imageID, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        int sampleSize = 1;

        if (outWidth > reqWidth || outHeight > reqHeight) {
            final int ratioWidth = Math.round((float) outWidth / reqWidth);
            final int ratioHeight = Math.round((float) outHeight / reqHeight);

            sampleSize = ratioWidth > ratioHeight ? ratioHeight : ratioWidth;
        }
        return sampleSize;
    }

    private class LoadImage extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private int resID = 0;

        private LoadImage(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null && imageViewWeakReference != null) {
                final ImageView imageView = imageViewWeakReference.get();

                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            resID = params[0];
            return decodeImageForGridView(resID, WIDTH, HEIGHT, mResources);
        }
    }


}
