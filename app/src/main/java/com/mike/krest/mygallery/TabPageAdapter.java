package com.mike.krest.mygallery;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Krestiyaninov on 19.11.14.
 */

/**
 * Adapter for process data to swiping between tabs.
 */
public class TabPageAdapter extends FragmentPagerAdapter {

    private ArrayList<ArrayList<String>> mAlbumsWithFiles = new ArrayList<ArrayList<String>>(5); // Array of arrays files within every album

    private final String[] TITLES;
    
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
        TITLES = getAlbums(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int i) {
        Fragment albumFragment = new AlbumHolderFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(AlbumHolderFragment.ALBUM_NAME, mAlbumsWithFiles.get(i));
        albumFragment.setArguments(args);

        return albumFragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    // Method for receiving names of folders with images and itself images
    // TODO: add search of images in root directories and checking files with filters and them existing
    private String[] getAlbums(File rootDirectory, String... directoriesForImages){
        File checkedDirectory; // Object File for checking is it directory or isn't't
        String tmpDirectoryPath; // String representation of path for checking directory
        List<String> pathToAlbumsList = new ArrayList<String>(5); // ArrayList for album paths which will be transformed into titles of tabs
        File[] listFiles; // List of files
        ImageFilter imageFilter = new ImageFilter();

        for (String directoryForImages: directoriesForImages) {
            checkedDirectory = new File(rootDirectory, directoryForImages);

            // Check root directory
            if (checkedDirectory.isDirectory()) {
                // Give absolute path of the root and directories in the root directory for album names
                String pathToImages = checkedDirectory.getAbsolutePath();
                String[] directories = checkedDirectory.list();

                // Work flow with extracting name of files in albums and also adding ones in array as titles of directories, too.
                for (String directory : directories) {
                    tmpDirectoryPath = pathToImages + File.separator + directory;
                    checkedDirectory = new File(tmpDirectoryPath);

                    if (checkedDirectory.isDirectory()) {

                        listFiles = checkedDirectory.listFiles(imageFilter);
                        ArrayList<String> files = new ArrayList<String>();

                        for (File file : listFiles) {
                            files.add(file.getAbsolutePath());
                        }

                        if (!files.isEmpty()) {
                            pathToAlbumsList.add(directory);
                            mAlbumsWithFiles.add(files);
                        }
                    }
                }
            }
        }
        if (pathToAlbumsList.isEmpty()) {
            for (int j = 0; j < 4; j++) {
                mAlbumsWithFiles.add(new ArrayList<String>());
            }
            return new String[] {"You", "don't", "have", "albums"};
        }
        return pathToAlbumsList.toArray(new String[pathToAlbumsList.size()]);
    }

    class ImageFilter implements FileFilter {
        String[] extensions = {".jpg", ".png", ".jpeg", ".gif"};

        @Override
        public boolean accept(File pathname) {
            for (String extension: extensions){
                if (pathname.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }
}
