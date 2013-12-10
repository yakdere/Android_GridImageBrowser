package com.yakdere.imagebrowser.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.yakdere.imagebrowser.R;
import com.yakdere.imagebrowser.model.FilterOptions;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
public class FilterActivity extends SherlockActivity {
	Spinner sSize;
	Spinner sColor;
	Spinner sType;
	EditText etSite;

	ArrayAdapter<CharSequence> sizeAdapter;
	ArrayAdapter<CharSequence> colorAdapter;
	ArrayAdapter<CharSequence> typeAdapter;

	FilterOptions optionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Advanced Filter Options");
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));
		//set views
		sSize = (Spinner) findViewById(R.id.spImageSize);
		sColor = (Spinner) findViewById(R.id.spColorFilter);
		sType = (Spinner) findViewById(R.id.spImageType);
		etSite = (EditText) findViewById(R.id.etSiteFilter);
		//set Adapters
		sizeAdapter = ArrayAdapter.createFromResource(this,R.array.sizes, android.R.layout.simple_spinner_item);
		colorAdapter = ArrayAdapter.createFromResource(this,R.array.colors, android.R.layout.simple_spinner_item);
		typeAdapter = ArrayAdapter.createFromResource(this,R.array.types, android.R.layout.simple_spinner_item);

		sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sSize.setAdapter(sizeAdapter);
		sColor.setAdapter(colorAdapter);
		sType.setAdapter(typeAdapter);
		//if there is previous filter options set them first
		setCurrentOptions();


	}
	private void setCurrentOptions() {
		optionList = (FilterOptions) getIntent().getSerializableExtra("filter");
		if (optionList != null) {
			setSpinnerSelectionByValue(sSize ,optionList.getSize());
			setSpinnerSelectionByValue(sColor ,optionList.getColor());
			setSpinnerSelectionByValue(sType ,optionList.getType());
			etSite.setText(optionList.getSite());
		} 
		return;
	}

	private void setSpinnerSelectionByValue(Spinner spinner, String value) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
		int index = adapter.getPosition(value);
		spinner.setSelection(index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miFilterSet:
			String size = sSize.getSelectedItem().toString();
			String color = sColor.getSelectedItem().toString();
			String type = sType.getSelectedItem().toString();
			String site = etSite.getText().toString();
			optionList = new FilterOptions(size, color, type, site);
			Intent filter_intent = new Intent();
			filter_intent.putExtra("filter", optionList);
			setResult(RESULT_OK, filter_intent);
			finish();
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}