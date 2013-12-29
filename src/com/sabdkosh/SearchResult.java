package com.sabdkosh;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.sabdkosh.db.SabdkoshDatabase;
import com.sabdkosh.db.SearchItem;
import com.sabdkosh.db.searchResultAdapter;

public class SearchResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		getIntent().getStringExtra("mytext");
		loadSearchResult(getIntent().getStringExtra("searchString"),getIntent().getStringExtra("fromInput"),getIntent().getStringExtra("toOutput"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
	}
	private void loadSearchResult(String searchWord,String fromType,String toType){
		SabdkoshDatabase db = new SabdkoshDatabase(getApplicationContext());
		ListView lv = (ListView) findViewById(R.id.listView1);
		ArrayList<SearchItem> result = db.searchWord(searchWord, db.getLanguageId(fromType), db.getLanguageId(toType));
		searchResultAdapter adapter = new searchResultAdapter (this,R.layout.search_item, result);
		lv.setAdapter(adapter);
		Toast.makeText(getApplicationContext(),
				result.size() + " Words found", Toast.LENGTH_SHORT)
				.show();
	}
}
