package com.mike.krest.mygallery;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by Mikhail Krestiyaninov on 16.11.14.
 */

/**
 * Class for helping with async process of loading images
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<AsyncLoadImage> asyncLoadImageReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, AsyncLoadImage asyncLoadImage) {
        super(res, bitmap);
        this.asyncLoadImageReference = new WeakReference<AsyncLoadImage>(asyncLoadImage);
    }

    public AsyncLoadImage getAsyncLoadImage() {
        return asyncLoadImageReference.get();
    }
}
