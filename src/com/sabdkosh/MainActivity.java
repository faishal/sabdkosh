package com.sabdkosh;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sabdkosh.db.SabdkoshDatabase;
/**
 * MainActivity class
 * First activity that will load at application start
 * @author faishal
 *
 */
public class MainActivity extends Activity implements OnItemSelectedListener {
	//Spinner to take inputType and output type
	Spinner spnInputType, spnOutputType;
	//Button search
	Button btnSearch;
	//AutoComplete for search word (Suggestion is pending)
	AutoCompleteTextView atcSearchWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Spinner element
		spnInputType = (Spinner) findViewById(R.id.spnInputType);
		spnOutputType = (Spinner) findViewById(R.id.spnOutputType);
		//Button search
		btnSearch = (Button) findViewById(R.id.btnSearch);
		//AutoComplete search word
		atcSearchWord = (AutoCompleteTextView) findViewById(R.id.actSearch);
		//Array adapter give all all languages
		ArrayAdapter<String> dataAdapter = loadLanguage();
		//Set all language adapter to both spinner
		spnInputType.setAdapter(dataAdapter);
		spnOutputType.setAdapter(dataAdapter);
		//Bind search event to button
		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Search word
				String searchWord = atcSearchWord.getText().toString().trim();
				//Checking search word is empty or not
				if (searchWord.length() > 0) {
					//Take input Language type
					String  fromInput = spnInputType.getItemAtPosition(spnInputType.getSelectedItemPosition()).toString();
					//Take output Language type
					String  toOutput = spnOutputType.getItemAtPosition(spnOutputType.getSelectedItemPosition()).toString();
					//Create Intent to pass data to another activity
					Intent myIntent = new Intent(v.getContext(),
							SearchResult.class);
					//set intent data to access on searchResult activity
					myIntent.putExtra("fromInput", fromInput);
					myIntent.putExtra("toOutput", toOutput);
					myIntent.putExtra("searchString", searchWord);
					//Start search result activity
					startActivity(myIntent);
				} else {
					//Throw error if it is empty
					Toast.makeText(getApplicationContext(),
							"Please enter search keyword", Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * This function will load all languages from database
	 */
	private ArrayAdapter<String> loadLanguage() {
		SabdkoshDatabase db = new SabdkoshDatabase(getApplicationContext());
		List<String> lables = db.getAllLanguages();
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		return dataAdapter;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// On selecting a spinner item
		String label = parent.getItemAtPosition(position).toString();

		// Showing selected spinner item
		Toast.makeText(parent.getContext(), "You selected: " + label,
				Toast.LENGTH_LONG).show();

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
