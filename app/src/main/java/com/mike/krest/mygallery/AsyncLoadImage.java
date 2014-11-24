package com.mike.krest.mygallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Mikhail Krestiyaninov on 16.11.14.
 */

/**
 * Class responding for async process of loading pictures
 */
public class AsyncLoadImage extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> mImageViewWeakReference;
    public String mResID = "-1"; // id of image in the form of path
    private final int WIDTH;
    private final int HEIGHT;

    AsyncLoadImage(ImageView imageView, int width, int height) {
        WIDTH = width;
        HEIGHT = height;
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
    protected Bitmap doInBackground(String... params) {
        mResID = params[0];
        final Bitmap bitmap = decodeImageForGridView(mResID, WIDTH, HEIGHT);
        MainActivity.addToCacheMemory(mResID, bitmap);
        return bitmap;
    }

    //Method decoding original images to thumbnails
    private static Bitmap decodeImageForGridView(String imageID, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageID, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imageID, options);
    }

    // Method calculating dimensions for thumbnails and returning the ratio with original dimensions
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
