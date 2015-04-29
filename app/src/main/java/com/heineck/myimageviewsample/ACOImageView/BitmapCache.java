package com.heineck.myimageviewsample.ACOImageView;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Singleton class that implements bitmap cache in memory
 *
 * Created by vheineck on 28/04/15.
 */
public class BitmapCache {

    //Singleton instance
    private static BitmapCache instance;

    //memory cache
    private LruCache<String, Bitmap> mMemoryCache;

    //Gets singleton instance
    public static BitmapCache getInstance() {

        if (instance == null) {
            instance = new BitmapCache();
        }

        return instance;

    }


    private BitmapCache() {

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    /** Adds bitmap to cache
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /** Gets bitmap from cache
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
