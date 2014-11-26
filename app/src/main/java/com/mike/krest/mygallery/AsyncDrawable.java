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
    private final WeakReference<AsyncAlbumImageLoad> asyncAlbumImageLoadWeakReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, AsyncLoadImage asyncLoadImage) {
        super(res, bitmap);
        this.asyncLoadImageReference = new WeakReference<AsyncLoadImage>(asyncLoadImage);
        this.asyncAlbumImageLoadWeakReference = null;
    }

    public AsyncDrawable(Resources resources, Bitmap mPlaceHolderBitmap, AsyncAlbumImageLoad asyncAlbumImageLoad) {
        super(resources, mPlaceHolderBitmap);
        this.asyncAlbumImageLoadWeakReference = new WeakReference<AsyncAlbumImageLoad>(asyncAlbumImageLoad);
        this.asyncLoadImageReference = null;
    }

    // TODO: make one method instead of two with parameter
    public AsyncLoadImage getAsyncLoadImage() {
        return asyncLoadImageReference.get();
    }

    public AsyncAlbumImageLoad getAsyncAlbumImageLoad() {
        return asyncAlbumImageLoadWeakReference.get();
    }
}
