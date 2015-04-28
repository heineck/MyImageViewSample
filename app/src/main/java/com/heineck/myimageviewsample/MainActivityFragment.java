package com.heineck.myimageviewsample;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.heineck.myimageviewsample.ACOImageView.ACOImageView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static String TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnLoad = (Button)rootView.findViewById(R.id.button);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACOImageView imageView = (ACOImageView)rootView.findViewById(R.id.imageView);

                Drawable placeholder = getActivity().getDrawable(R.drawable.news_placeholder);
                String url = "http://upload.wikimedia.org/wikipedia/commons/9/90/Tam.a330-200.pt-mvl.arp.jpg";

                imageView.setImageURL(placeholder, url);

            }
        });

        return rootView;
    }
}
