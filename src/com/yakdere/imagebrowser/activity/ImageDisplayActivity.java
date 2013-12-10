package com.yakdere.imagebrowser.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.image.SmartImageView;
import com.yakdere.imagebrowser.R;
import com.yakdere.imagebrowser.model.ImageResult;

public class ImageDisplayActivity extends SherlockActivity {
        ImageResult image;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_imagedisplay);
                ActionBar bar = getSupportActionBar();
        		bar.setTitle("Selected Image");
        		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));
     
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
        public boolean onOptionsItemSelected(MenuItem item) {
    		switch (item.getItemId()) {
    		case R.id.miShare:
    			Intent shareIntent = new Intent();
    			shareIntent.setAction(Intent.ACTION_SEND);
    			shareIntent.putExtra(Intent.EXTRA_STREAM, image.getFullUrl());
    			shareIntent.setType("image/jpeg");
    			startActivity(Intent.createChooser(shareIntent, "Share the image to.."));
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    		}
    	}
}
