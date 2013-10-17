package com.yakdere.imagebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterActivity extends Activity {
	Spinner sSize;
	Spinner sColor;
	Spinner sType;
	EditText etSite;
	Button bSave;

	ArrayAdapter<CharSequence> sizeAdapter;
	ArrayAdapter<CharSequence> colorAdapter;
	ArrayAdapter<CharSequence> typeAdapter;

	FilterOptions optionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		//set views
		sSize = (Spinner) findViewById(R.id.spImageSize);
		sColor = (Spinner) findViewById(R.id.spColorFilter);
		sType = (Spinner) findViewById(R.id.spImageType);
		etSite = (EditText) findViewById(R.id.etSiteFilter);
		bSave = (Button) findViewById(R.id.bSave);
        //set Adapters
		sizeAdapter = ArrayAdapter.createFromResource(this,R.array.colors, android.R.layout.simple_spinner_item);
		colorAdapter = ArrayAdapter.createFromResource(this,R.array.sizes, android.R.layout.simple_spinner_item);
		typeAdapter = ArrayAdapter.createFromResource(this,R.array.types, android.R.layout.simple_spinner_item);
	
		sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sSize.setAdapter(sizeAdapter);
		sColor.setAdapter(colorAdapter);
		sType.setAdapter(typeAdapter);
		
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
	}

	private void setSpinnerSelectionByValue(Spinner s, String v) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) s.getAdapter();
		int index = adapter.getPosition(v);
		s.setSelection(index);

	}
	public void onSendOption(View v) {
		String size = sSize.getSelectedItem().toString();
		String color = sColor.getSelectedItem().toString();
		String type = sType.getSelectedItem().toString();
		String site = etSite.getText().toString();
		FilterOptions options = new FilterOptions(size, color, type, site);

		Intent i = new Intent();
		i.putExtra("filter", options);
		setResult(RESULT_OK, i);
		this.finish();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

}
