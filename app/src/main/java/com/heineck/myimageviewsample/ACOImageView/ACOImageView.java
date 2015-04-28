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
 * Created by vheineck on 28/04/15.
 */
public class ACOImageView extends ImageView {

    private static String TAG = "ACOImageView";

    Drawable mPlaceholder;

    public ACOImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageURL(Drawable placeholder, String URL) {

        this.mPlaceholder = placeholder;

        DownloadImageFromWebTask webTask = new DownloadImageFromWebTask();
        webTask.execute(URL);

    }

    private class DownloadImageFromWebTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                    InputStream is = connection.getInputStream();

                    ret = BitmapFactory.decodeStream(is);
                    //ret = BitmapHelper.decodeSampledBitmapFromStream(is, w, h); //later code

                    //adding image to cache
                    BitmapCache.getInstance().addBitmapToMemoryCache(urlString, ret);

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

            if (bitmap != null) {
                setImageBitmap(bitmap);
            }

        }
    }

}
