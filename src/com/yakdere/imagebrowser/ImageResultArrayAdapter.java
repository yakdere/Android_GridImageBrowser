package com.yakdere.imagebrowser;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {
    
    public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
            /**
             * android.layout.simple_list_item_1 is for text view, you have to create your own xml file 
             * as an imageView then change the view that you created
             */
            super(context, R.layout.item_image_result, images);
    }
//to convert data to view override the getView()
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ImageResult imageInfo = this.getItem(position);
            SmartImageView ivImage;
            //next time to use same view use use else function
            if (convertView == null) {
                    LayoutInflater inflator = LayoutInflater.from(getContext());
                    ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
            //existing image view
            } else {
                    ivImage = (SmartImageView) convertView;
                    ivImage.setImageResource(android.R.color.transparent);
            }
            //set View
            ivImage.setImageUrl(imageInfo.getThumbUrl());
            return ivImage;
    }
}
