package com.mike.krest.mygallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Activity responding for display of images in selected album
 */
public class ImageViewActivity extends ActionBarActivity {

    private ArrayList<String> mImageIDs;
    private String mCurrentImageID;
    private int mPosition;
    private static LruCache<String, Bitmap> mCacheForSingleAlbum;
    private AlbumAdapter albumAdapter;
    private ViewPager albumViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
        final int cacheSize = maxMemory / 5;

        // Creating of fragment responsible for saving cache at recreating activity
        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getSupportFragmentManager());
        mCacheForSingleAlbum = retainFragment.mRetainedCache;

        // Check first time of creating cache
        if (mCacheForSingleAlbum == null) {
            mCacheForSingleAlbum = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };
            retainFragment.mRetainedCache = mCacheForSingleAlbum;
        }

        Bundle args = getIntent().getExtras();

        mImageIDs = args.getStringArrayList(AlbumHolderFragment.ALBUM_NAME);
        mCurrentImageID = args.getString(AlbumHolderFragment.EXTRA_RES_ID);
        mPosition = args.getInt(AlbumHolderFragment.EXTRA_POSITION);

        albumViewPager = (ViewPager) findViewById(R.id.album_view_pager);

        albumAdapter = new AlbumAdapter(getSupportFragmentManager(), mImageIDs, mCurrentImageID);
        albumViewPager.setAdapter(albumAdapter);

        albumViewPager.setCurrentItem(mPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            shareCurrentImage(albumViewPager.getCurrentItem());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareCurrentImage(int currentItem) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        File file = new File(mImageIDs.get(currentItem));
        if (file.exists()) {

            Uri uriOfFile = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriOfFile);

            startActivity(Intent.createChooser(shareIntent, "What app you prefer?"));
        }
    }


    public void loadBitmap(ImageView imageView, String resID) {

        final String imageKey = resID;

        final Bitmap cacheBitmap = ImageViewActivity.getBitmapFromMemCache(imageKey);

        if (cacheBitmap != null) {
            imageView.setImageBitmap(cacheBitmap);
        } else {
            if (cancelPotentialWork(imageView, resID)) {
                final AsyncAlbumImageLoad task = new AsyncAlbumImageLoad(imageView, getResources().getDisplayMetrics().widthPixels,
                        getResources().getDisplayMetrics().heightPixels);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), null, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(resID);
            }
        }
    }

    private boolean cancelPotentialWork(ImageView imageView, String resID) {
        final AsyncAlbumImageLoad task = getAsyncLoadImageTask(imageView);

        if (task != null) {
            final String loadingResID = task.mResID;
            if (!loadingResID.equals(resID) || loadingResID.equals("-1"))
                task.cancel(true);
            else
                return false;
        }
        return true;
    }

    static AsyncAlbumImageLoad getAsyncLoadImageTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getAsyncAlbumImageLoad();
            }
        }
        return null;
    }


    // Adding image to cache
    public static void addToCacheMemory(String key, Bitmap value) {
        if (getBitmapFromMemCache(key) == null) {
            mCacheForSingleAlbum.put(key, value);
        }
    }

    // Getting image from cache
    public static Bitmap getBitmapFromMemCache(String key) {
        return mCacheForSingleAlbum.get(key);
    }

    // Fragment for retaining cache with thumbnails of images
    public static class RetainFragment extends Fragment {
        private static final String TAG = "com.krest.mike.mygallery.imageviewactivity.retainfragment";
        public LruCache<String, Bitmap> mRetainedCache;


        public RetainFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public static RetainFragment findOrCreateRetainFragment(FragmentManager fragmentManager) {
            RetainFragment retainFragment = (RetainFragment) fragmentManager.findFragmentByTag(TAG);
            if (retainFragment == null) {
                retainFragment = new RetainFragment();
                fragmentManager.beginTransaction().add(retainFragment, TAG).commit();
            }
            return retainFragment;
        }
    }
}
