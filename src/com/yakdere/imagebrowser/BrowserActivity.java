package com.yakdere.imagebrowser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class BrowserActivity extends Activity {
	EditText etQuery;
	GridView gvResult;

	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;

	FilterOptions aFilter;
	int start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		//set Views
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResult = (GridView) findViewById(R.id.gvResult);
		//set Adapter
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResult.setAdapter(imageAdapter);

		gvResult.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// whatever code is needed to append new items to your AdapterView
				Log.d("DEBUG", "PAGE NUM IS "+page);
				loadImages(8, page);
				}
		});
		
		//show full screen the selectem item
		gvResult.setOnItemClickListener(new OnItemClickListener() {
			//all we need here position
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult ir = imageResults.get(position);
				//instead of passing url pass the image itself
				i.putExtra("result", ir);
				startActivity(i);                                
			}
		});
	}
	public void onImageSearch(View v) {
		imageResults.clear();
		loadImages(8, 0);
	}        
	private void loadImages(int i, int page) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
		
		AsyncHttpClient client = new AsyncHttpClient();
		int start =i*page;
		
		String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=" + start;
		//if filter preferences exist add them into url
		if (aFilter != null) {
			if (aFilter.getSize() != null) {
				url += "&imgsz=" + aFilter.getSize();
			}
			if (aFilter.getColor() != null) {
				url += "&imgcolor=" + aFilter.getColor();
			}
			if (aFilter.getType() != null) {
				url += "&imgtype=" + aFilter.getType();
			}
			if (aFilter.getSite() != null) {
				url += "&as_sitesearch=" + aFilter.getSite();
			}
		}                

		client.get(url += "&v=1.0&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;

				try { 
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					//we can notify adapter instead of adding objects into arraylist
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e){
					e.printStackTrace();
				}
			}
			@Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                    Log.d("DEBUG", "Error");
            }
		});
	}

	//If menu item is clicked filter activity will start with result expectation
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), FilterActivity.class);
		if (aFilter != null) {
			i.putExtra("filter", aFilter);
		}
		startActivityForResult(i, 0);
		return false;
	}
	//get result and set the filter preferences
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 ) {
			if (resultCode == RESULT_OK) {
				aFilter = (FilterOptions) data.getSerializableExtra("filter");
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}

}
