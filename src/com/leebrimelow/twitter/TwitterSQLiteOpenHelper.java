package com.leebrimelow.twitter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TwitterSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static TwitterSQLiteOpenHelper instanceOf;
	
	public static final String DATABASE_NAME = "KAVE_DB";
	public static final int DATA_VERSION = 1;
	public static final String ACCOUNTS_TABLE_NAME = "accounts";
	public static final String STATUSES_TABLE_NAME = "statuses";
	public static final String USERS_TABLE_NAME = "users";
	public static final String SEARCHES_TABLE_NAME = "searches";
	public static final String TRENDS_TABLE_NAME = "trends";
	public static final String AVATARS_TABLE_NAME = "avatars";
		
    private static final String ACCOUNTS_TABLE_CREATE = "CREATE TABLE " + ACCOUNTS_TABLE_NAME + " (account_id INTEGER PRIMARY KEY, screen_name TEXT NOT NULL, account_token INTEGER NOT NULL, json_user_obj TEXT );";
    private static final String STATUSES_TABLE_CREATE = "CREATE TABLE " + STATUSES_TABLE_NAME +" (status_id INTEGER PRIMARY KEY, user_id NOT NULL, status_text TEXT NOT NULL, isFavorited INTEGER NOT NULL, isRetweet INTEGER NOT NULL, isRetweetedByMe INTEGER NOT NULL, create_date TEXT NOT NULL, json_status_obj TEXT NT NULL);";
    private static final String USERS_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE_NAME + " (user_id INTEGER PRIMARY KEY, screen_name TEXT NOT NULL, isFollower INTEGER NOT NULL, isFriend  INTEGER NOT NULL, json_user_obj TEXT NOT NULL );";
    private static final String SEARCHES_TABLE_CREATE = "CREATE TABLE " + SEARCHES_TABLE_NAME + " (search_id INTEGER PRIMARY KEY AUTOINCREMENT, search_title TEXT, search_url TEXT);";
    private static final String TRENDS_TABLE_CREATE = "CREATE TABLE " + TRENDS_TABLE_NAME + " (trend_id INTEGER PRIMARY KEY AUTOINCREMENT, trend_title TEXT, trend_url TEXT);";
    private static final String AVATARS_TABEL_CREATE = "CREATE TABLE " + AVATARS_TABLE_NAME + " (user_id INTEGER PRIMARY KEY, avatar BLOB);";

    private static final String ACCOUNTS_TABLE_DROP = "DROP TABLE " + ACCOUNTS_TABLE_NAME;
    private static final String STATUSES_TABLE_DROP = "DROP TABLE " + STATUSES_TABLE_NAME;
    private static final String USERS_TABLE_DROP = "DROP TABLE " + USERS_TABLE_NAME;
    private static final String SEARCHES_TABLE_DROP = "DROP TABLE " + SEARCHES_TABLE_NAME;
    private static final String TRENDS_TABLE_DROP = "DROP TABLE " + TRENDS_TABLE_NAME;
    private static final String AVATARS_TABEL_DROP = "DROP TABLE " + AVATARS_TABLE_NAME;
    
	private TwitterSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATA_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public static final TwitterSQLiteOpenHelper getInstance(Context context){
		if (instanceOf == null){
			instanceOf = new TwitterSQLiteOpenHelper(context);
			return instanceOf;
		}else
			return instanceOf;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ACCOUNTS_TABLE_CREATE);
		db.execSQL(STATUSES_TABLE_CREATE);
		db.execSQL(USERS_TABLE_CREATE);
		db.execSQL(SEARCHES_TABLE_CREATE);
		db.execSQL(TRENDS_TABLE_CREATE);
		db.execSQL(AVATARS_TABEL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//DELETE OLD TABLES
		db.execSQL(ACCOUNTS_TABLE_DROP);
		db.execSQL(STATUSES_TABLE_DROP);
		db.execSQL(USERS_TABLE_DROP);
		db.execSQL(SEARCHES_TABLE_DROP);
		db.execSQL(TRENDS_TABLE_DROP);
		db.execSQL(AVATARS_TABEL_DROP);
		
		//CREATE NEW TABLES
		db.execSQL(ACCOUNTS_TABLE_CREATE);
		db.execSQL(STATUSES_TABLE_CREATE);
		db.execSQL(USERS_TABLE_CREATE);
		db.execSQL(SEARCHES_TABLE_CREATE);
		db.execSQL(TRENDS_TABLE_CREATE);
		db.execSQL(AVATARS_TABEL_CREATE);
	}

}
