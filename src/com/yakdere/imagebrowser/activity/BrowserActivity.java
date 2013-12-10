package com.yakdere.imagebrowser.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yakdere.imagebrowser.R;
import com.yakdere.imagebrowser.model.FilterOptions;
import com.yakdere.imagebrowser.model.ImageResult;
import com.yakdere.imagebrowser.utility.EndlessScrollListener;
import com.yakdere.imagebrowser.utility.ImageResultArrayAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class BrowserActivity extends SherlockActivity {
	EditText etQuery;
	GridView gvResult;

	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;

	FilterOptions aFilter;
	int start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Search for images");
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));
		setViews();
	}

	private void setViews() {

		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResult = (GridView) findViewById(R.id.gvResult);
		//set Adapter
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResult.setAdapter(imageAdapter);
		//set listeners
		gvResult.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// whatever code is needed to append new items to your AdapterView
				Toast.makeText(getApplicationContext(), "fetching..",Toast.LENGTH_SHORT).show();
				Log.d("On Load More", "PAGE NUM IS: "+page);
				loadImages(page);
			}
		});

		//show full screen the selected item
		gvResult.setOnItemClickListener(new OnItemClickListener() {
			//Get selected item position
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				Intent i = new Intent(getBaseContext(), ImageDisplayActivity.class);
				ImageResult ir = imageResults.get(position);
				//instead of passing url pass the image itself
				i.putExtra("result", ir);
				startActivity(i);                                
			}
		});
	}

	//Search button clicked
	public void onStartSearch(View v) {
		String query = etQuery.getText().toString().trim();
		Log.i("Search Button Clicked", query);
		if (query.length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter search query", Toast.LENGTH_LONG);
			return;
		} else {
			Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
			//Clear previous data from arraylist for next searches
			if (!imageResults.isEmpty()) {
				imageResults.clear();
			}
			Log.d("Search Started", "search button success");
			loadImages(0);
		}
	}        
	private void loadImages(int label) {
		start = 8 * label;
		/*
		 *  "pages": [[
                    { "start": "0", "label": 1 },
                    { "start": "4", "label": 2 },
		 */
		String append = "";		
		String base_url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start=" + start + "&v=1.0";
		//if filter preferences exist add them into url
		if (aFilter != null) {
			if (!aFilter.getSize().isEmpty() && aFilter.getSize() != "all") {
				append += "&imgsz=" + aFilter.getSize();
			}
			if (!aFilter.getColor().isEmpty() && aFilter.getColor() != "all") {
				append += "&imgcolor=" + aFilter.getColor();
			}
			if (!aFilter.getType().isEmpty() && aFilter.getType() != "all") {
				append += "&imgtype=" + aFilter.getType();
			}
			if (!aFilter.getSite().isEmpty()) {
				append += "&as_sitesearch=" + aFilter.getSite();
			}
		} 
		Log.i("Filter Options Established", "append url is: " + append);
		AsyncHttpClient client = new AsyncHttpClient();
		String full_url = base_url + append + "&q=" + Uri.encode(etQuery.getText().toString());
		Log.i("Search Url Established", "current url is: " + full_url);
		client.get(full_url , new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;

				try { 
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					//we can notify adapter instead of adding objects into arraylist
					Log.d("Client Response Success", imageResults.toString());
				} catch (JSONException e){
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("Error", "Error while requesting results");
				e.printStackTrace();
			}
		});
	}

	//If menu item is clicked filter activity will start with result expectation
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miEdit:
			Intent i = new Intent(getApplicationContext(), FilterActivity.class);
			if (aFilter != null) {
				i.putExtra("filter", aFilter);
			}
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//get result and set the filter preferences
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 ) {
			if (resultCode == RESULT_OK) {
				aFilter = (FilterOptions) data.getSerializableExtra("filter");
				Log.d("DEBUG", "Activity result success! intent data = " + aFilter);
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}

}
