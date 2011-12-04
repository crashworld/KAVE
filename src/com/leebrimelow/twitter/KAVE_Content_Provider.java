package com.leebrimelow.twitter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.leebrimelow.twitter.TwitterSQLiteOpenHelper;

public class KAVE_Content_Provider extends ContentProvider {

	private static final int ACCOUNTS = 1;
	private static final int ACCOUNT_ID = 2;
	private static final int USERS = 3;
	private static final int USER_ID = 4;
	private static final int STATUSES = 5;
	private static final int STATUS_ID = 6;
	private static final int SEARCHES = 7;
	private static final int SEARCH_ID = 8;
	private static final int TRENDS = 9;
	private static final int TREND_ID = 10;
	private static final int AVATARS = 11;
	private static final int AVATAR_ID = 12;	
	private static final int AACCOUNTS = 13;  //ACCOUNTs + AVATARs
	private static final int AACCOUNT_ID = 14; // ACCOUNT + AVATAR
	private static final int AUSERS = 15; //-||-
	private static final int AUSER_ID = 16; //-||-
	private static final int ASTATUSES = 17; // -||-
	private static final int ASTATUS_ID = 18; // -||-
	
	public static final String PROVIDER_NAME = "com.leebrimelow.twitter.kaveprovider";
	public static final Uri CONTENT_URI_SATUSES = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME);
	public static final Uri CONTENT_URI_USERS = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.USERS_TABLE_NAME);
	public static final Uri CONTENT_URI_ACCOUNTS = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME);
	public static final Uri CONTENT_URI_SEARCHES = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.SEARCHES_TABLE_NAME);
	public static final Uri CONTENT_URI_TRENDS = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.TRENDS_TABLE_NAME);
	public static final Uri CONTENT_URI_AVATARS = Uri.parse("content://"+ PROVIDER_NAME + "/" + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME);
	public static final Uri CONTENT_URI_ASATUSES = Uri.parse("content://"+ PROVIDER_NAME + "/a" + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME); //STATUSES + AVATARS
	public static final Uri CONTENT_URI_AUSERS = Uri.parse("content://"+ PROVIDER_NAME + "/a" + TwitterSQLiteOpenHelper.USERS_TABLE_NAME); //USERS + AVATARS
	public static final Uri CONTENT_URI_AACCOUNTS = Uri.parse("content://"+ PROVIDER_NAME + "/a" + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME); //ACCOUNTS + AVATARS
	
	private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private SQLiteDatabase db;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count; // count of effected rows
		switch (mUriMatcher.match(uri))
		{
		case ACCOUNTS:
			count = db.delete(TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, selection, selectionArgs);			
			break;	
			
		case USERS:
			count = db.delete(TwitterSQLiteOpenHelper.USERS_TABLE_NAME, selection, selectionArgs);
			break;
			
		case STATUSES:
			count = db.delete(TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, selection, selectionArgs);				
			break;
			
		case SEARCHES:
			count = 0;
		case SEARCH_ID:
			count = 0;
		case TRENDS:
			count = 0;
		case TREND_ID:
			count = 0;
		case AVATARS:
			count = db.delete(TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, selection, selectionArgs);				
			break;
		default:
			count = 0;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		ContentValues mValues = new ContentValues(values);
		long id; // id of new row
		Uri rUri; // result uri
		switch (mUriMatcher.match(uri))
		{
		case ACCOUNTS:
			id = db.insert(TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, null, mValues);	
			if (id > 0){
				
				rUri = ContentUris.withAppendedId(CONTENT_URI_ACCOUNTS, id);
				getContext().getContentResolver().notifyChange(rUri, null);							
			}else{
				throw new SQLException("Failed to insert row into " + uri);
			}
			break;	
			
		case USERS:
			id = db.insert(TwitterSQLiteOpenHelper.USERS_TABLE_NAME, null, mValues);
			if (id > 0){
				
				rUri = ContentUris.withAppendedId(CONTENT_URI_USERS, id);
				getContext().getContentResolver().notifyChange(rUri, null);							
			}else{
				throw new SQLException("Failed to insert row into " + uri);
			}
			break;	
			
		case STATUSES:
			id = db.insert(TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, null, mValues);
			if (id > 0){
				
				rUri = ContentUris.withAppendedId(CONTENT_URI_SATUSES, id);
				getContext().getContentResolver().notifyChange(rUri, null);							
			}else{
				throw new SQLException("Failed to insert row into " + uri);
			}
			break;	
			
		case SEARCHES:
			rUri = null;
			break;
			
		case SEARCH_ID:
			rUri = null;
			break;
			
		case TRENDS:
			rUri = null;
			break;
			
		case TREND_ID:
			rUri = null;
			break;
			
		case AVATARS:
			id = db.insert(TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, null, mValues);
			if (id > 0){
				rUri = ContentUris.withAppendedId(CONTENT_URI_AVATARS, id);
				getContext().getContentResolver().notifyChange(rUri, null);							
			}else{
				throw new SQLException("Failed to insert row into " + uri);
			}
			break;	
			
		default:
			rUri = null;
			break;
		}
		
		
		return rUri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		setupUriMatcher();
		db = TwitterSQLiteOpenHelper.getInstance(getContext()).getWritableDatabase();
		return (db == null) ? false : true;
	}
	
	private void setupUriMatcher(){
		
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, ACCOUNTS);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME + "/#", ACCOUNT_ID);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.USERS_TABLE_NAME, USERS);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.USERS_TABLE_NAME + "/#", USER_ID);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, STATUSES);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME + "/#", STATUS_ID);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.SEARCHES_TABLE_NAME, SEARCHES);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.SEARCHES_TABLE_NAME + "/#", SEARCH_ID);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.TRENDS_TABLE_NAME, TRENDS);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.TRENDS_TABLE_NAME + "/#", TREND_ID);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, AVATARS);
		mUriMatcher.addURI(PROVIDER_NAME, TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + "/#", AVATAR_ID);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, AACCOUNTS);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME + "/#", AACCOUNT_ID);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.USERS_TABLE_NAME, AUSERS);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.USERS_TABLE_NAME + "/#", AUSER_ID);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, ASTATUSES);
		mUriMatcher.addURI(PROVIDER_NAME, "a" + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME + "/#", ASTATUS_ID);		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
				// TODO Auto-generated method stub
			Cursor c; // result of function
			String from; // part of SQL, join tables
			int num; // id of row in table
			num = 0;//Integer.parseInt(uri.getLastPathSegment());
			if (num > 0)
				selectionArgs[selectionArgs.length] = String.valueOf(num);
			switch (mUriMatcher.match(uri))
			{
			case ACCOUNTS:
				if(sortOrder == null){
					sortOrder = "account_id";
				}
				c = db.query(TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);			
				break;
				
			case ACCOUNT_ID:
				c = db.query(TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, projection, selection + " account_id=?", selectionArgs , null, null, null);
				break;
				
			case USERS:
				if(sortOrder == null){
					sortOrder = "user_id";
				}
				c = db.query(TwitterSQLiteOpenHelper.USERS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case USER_ID:
				c = db.query(TwitterSQLiteOpenHelper.USERS_TABLE_NAME, projection, selection + " user_id=?", selectionArgs , null, null, null);				
				break;
				
			case STATUSES:
				if(sortOrder == null){
					sortOrder = "satus_id";
				}
				c = db.query(TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);				
				break;
				
			case STATUS_ID:
				c = db.query(TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, projection, selection + " status_id=?", selectionArgs , null, null, null);
				break;
				
			case SEARCHES:
				c = null;
				
			case SEARCH_ID:
				c = null;
				
			case TRENDS:
				c = null;
				
			case TREND_ID:
				c = null;
				
			case AVATARS:
				sortOrder = "user_id";
				c = db.query(TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);				
				break;
				
			case AVATAR_ID:
				c = db.query(TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, projection, selection + " user_id=?", selectionArgs , null, null, null);
				break;
				
			case AACCOUNTS:
				if(sortOrder == null){
					sortOrder = "account_id";
				}
				from = TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME + " INNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME +".account_id="+TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME+".user_id"; 
				c = db.query(from, projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case AACCOUNT_ID:
				from = TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME + " INNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME +".account_id="+TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME+".user_id";
				c = db.query(from, projection, selection + " user_id=?", selectionArgs , null, null, null);
				c= null;
				
			case AUSERS:
				if(sortOrder == null){
					sortOrder = "user_id";
				}
				from = TwitterSQLiteOpenHelper.USERS_TABLE_NAME + " INNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.USERS_TABLE_NAME + ".user_id=" + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + ".user_id"; 
				c = db.query(from, projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case AUSER_ID:
				from = TwitterSQLiteOpenHelper.USERS_TABLE_NAME + " INNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.USERS_TABLE_NAME + ".user_id=" + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + ".user_id";
				c = db.query(from, projection, selection + " user_id=?", selectionArgs , null, null, null);
				c = null;
				
			case ASTATUSES:
				if(sortOrder == null){
					sortOrder = "satus_id";
				}
				from = TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME + "I NNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME +".status_id="+TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME+".user_id"; 
				c = db.query(from, projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case ASTATUS_ID:
				from = TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME + " INNER JOIN " + TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME + " ON " + TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME +".status_id="+TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME+".user_id";
				c = db.query(from, projection, selection + " user_id=?", selectionArgs , null, null, null);
				c = null;
				
			default:
				c = null;
			}
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count; // count of effected rows
		switch (mUriMatcher.match(uri))
		{
		case ACCOUNTS:
			count = db.update(TwitterSQLiteOpenHelper.ACCOUNTS_TABLE_NAME, values, selection, selectionArgs);			
			break;	
			
		case USERS:
			count = db.update(TwitterSQLiteOpenHelper.USERS_TABLE_NAME, values, selection, selectionArgs);
			break;
			
		case STATUSES:
			count = db.update(TwitterSQLiteOpenHelper.STATUSES_TABLE_NAME, values, selection, selectionArgs);				
			break;
			
		case SEARCHES:
			count = 0;
			
		case SEARCH_ID:
			count = 0;
			
		case TRENDS:
			count = 0;
			
		case TREND_ID:
			count = 0;
			
		case AVATARS:
			count = db.update(TwitterSQLiteOpenHelper.AVATARS_TABLE_NAME, values, selection, selectionArgs);				
			break;
		default:
			count = 0;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
