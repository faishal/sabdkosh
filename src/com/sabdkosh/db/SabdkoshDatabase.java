package com.sabdkosh.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SabdkoshDatabase extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static String DATABASE_NAME = "DBSabdkoshdb_v" + DATABASE_VERSION + ".db";
	private static String TABLE_LANGUAGES = "languages";
	private static String TABLE_WORD = "trans_words";
	public final static String DATABASE_PATH = "/data/data/com.sabdkosh/databases/";
	

	private SQLiteDatabase dataBase;
	private final Context dbContext;

	public SabdkoshDatabase(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.dbContext = context;

		// checking database and open it if exists
		if (checkDataBase()) {
			openDataBase();
		} else {
			try {
				this.getReadableDatabase();
				copyDataBase();
				this.close();
				openDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			Toast.makeText(context, "Initial database is created",
					Toast.LENGTH_LONG).show();
		}
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = dbContext.getAssets().open("sabdkosh.db");
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String dbPath = DATABASE_PATH + DATABASE_NAME;
		dataBase = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		boolean exist = false;
		try {
			String dbPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Log.v("db log", "database does't exist");
		}

		if (checkDB != null) {
			exist = true;
			checkDB.close();
		}
		return exist;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void close() {

		if (dataBase != null)
			dataBase.close();

		super.close();

	}

	/**
	 * Getting all labels returns list of labels
	 **/
	public List<String> getAllLanguages() {
		List<String> labels = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT name FROM " + TABLE_LANGUAGES;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labels.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// returning lables
		return labels;
	}
	
	public int getLanguageId (String langName){
		String selectQuery = "SELECT ID FROM " + TABLE_LANGUAGES + " where name='" + langName.trim() + "'";
		int landId = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			landId = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return landId;
	}
	public ArrayList<SearchItem> searchWord(String word, int fromLang, int toLang) {
		ArrayList<SearchItem> result = new ArrayList<SearchItem>();
		// Select All Query
		String selectQuery = "SELECT b.word, b.meaning FROM " + TABLE_WORD + " a left join " + TABLE_WORD 
				+ " b on a.uid = b.uid where a.language_id = " + fromLang + " and b.language_id = " + toLang  
				+ " and a.word like '%" +  word + "%'";
		//selectQuery = "select word, meaning from " +  TABLE_WORD ;
		Toast.makeText(dbContext, selectQuery,
				Toast.LENGTH_LONG).show();
		Log.v("query", selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				result.add( new SearchItem(cursor.getString(0),cursor.getString(1) ));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return result;
	}
	

}