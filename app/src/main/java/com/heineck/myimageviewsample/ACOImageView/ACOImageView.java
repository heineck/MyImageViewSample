package com.heineck.myimageviewsample.ACOImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Custom ImageView that implements download image from URL.
 *
 * Created by vheineck on 28/04/15.
 */
public class ACOImageView extends ImageView {

    private static String TAG = "ACOImageView";

    Drawable mPlaceholder;

    public ACOImageView(Context context) {
        super(context);
    }

    public ACOImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACOImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidate();
    }

    /** Set image from URL. While url is not loaded, a placeholder image will be shown.
     *
     * @param placeholder
     * @param url
     */
    public void setImageURL(Drawable placeholder, String url) {

        this.mPlaceholder = placeholder;

        DownloadImageFromWebTask webTask = new DownloadImageFromWebTask();
        webTask.execute(url);

    }

    /** Async task for download image in background
     *
     */
    private class DownloadImageFromWebTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //shows placeholder on imageview
            setImageDrawable(mPlaceholder);

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap ret = null;

            try {

                String urlString = params[0];

                //try to get image from cache
                Bitmap bmp = BitmapCache.getInstance().getBitmapFromMemCache(urlString);

                if (bmp == null) { //checking image cache

                    int w = getWidth();
                    int h = getHeight();

                    Log.d(TAG, "image dimension: " + w + "x" + h);

                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) { // Connection OK

                        // get inputStream
                        InputStream is = connection.getInputStream();

                        //create Bitmap from inputStream
                        ret = BitmapFactory.decodeStream(is);
                        //ret = BitmapHelper.decodeSampledBitmapFromStream(is, w, h); //later code

                        //adding image to cache
                        BitmapCache.getInstance().addBitmapToMemoryCache(urlString, ret);
                    } else {
                        Log.d(TAG, "image request is not loaded");
                    }

                } else { //image exists in cache

                    ret = bmp;

                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "[MalformedURLException] - Invalid image: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "[IOException] - Invalid image: " + e.getMessage());
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            //Shows image loaded from url
            if (bitmap != null) {
                setImageBitmap(bitmap);
            }

        }
    }

}
