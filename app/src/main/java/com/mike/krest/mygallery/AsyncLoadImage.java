package com.mike.krest.mygallery;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Mikhail Krestiyaninov on 16.11.14.
 */
public class AsyncLoadImage extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> mImageViewWeakReference;
    public int mResID = 0;
    private final int WIDTH;
    private final int HEIGHT;
    private final Resources mResources;

    AsyncLoadImage(ImageView imageView, Resources resources, int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        this.mResources = resources;
        this.mImageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (bitmap != null && mImageViewWeakReference != null) {
            final ImageView imageView = mImageViewWeakReference.get();
            final AsyncLoadImage asyncLoadImage = GridViewAdapter.getAsyncLoadImageTask(imageView);

            if (this == asyncLoadImage && imageView != null)
                imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        mResID = params[0];
        return decodeImageForGridView(mResID, WIDTH, HEIGHT, mResources);
    }

    private static Bitmap decodeImageForGridView(int imageID, int reqWidth, int reqHeight, Resources resources) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, imageID, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

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
}
