package com.leebrimelow.twitter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TwitterSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "KAVE_DB";
	private static final int DATA_VERSION = 1;
	private static final String DICTIONARY_TABLE_NAME = "dictionary";
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " ();";

	public TwitterSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATA_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
