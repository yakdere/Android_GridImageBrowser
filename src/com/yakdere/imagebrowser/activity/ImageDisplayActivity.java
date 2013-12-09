package com.yakdere.imagebrowser.activity;

import android.os.Bundle;
import android.util.Log;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.loopj.android.image.SmartImageView;
import com.yakdere.imagebrowser.R;
import com.yakdere.imagebrowser.model.ImageResult;

public class ImageDisplayActivity extends SherlockActivity {
        ImageResult image;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_imagedisplay);
                //get the image passed by first activty
                image = null;
                try {
                	image = (ImageResult) getIntent().getSerializableExtra("result");
                } catch(NullPointerException e) {
                	e.printStackTrace();
                	Log.e("Intent Error", "Data not available");
                }
                SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
                ivImage.setImageUrl(image.getFullUrl());
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        	// Inflate the menu; this adds items to the action bar if it is present.
            getSupportMenuInflater().inflate(R.menu.image_display, menu);
            return true;
        }
}
