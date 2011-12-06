package com.leebrimelow.twitter.Activity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Adapter.TweetsAdapter;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Tweets_Activity extends ListActivity {

	private TweetsAdapter mAdapter;
	private int accountId;
	private Twitter twitter;
	private Twitter_Loader_Poster_Service mService;
	private boolean mBound;

	private ServiceConnection mConnection = new ServiceConnection() {

	      	public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				LocalBinder binder = (LocalBinder) service;
	            mService = binder.getService();
	            mBound = true;
	            long lastId;
	            if (mAdapter != null)
	            	lastId = mAdapter.getLastId();
	            else
	            	lastId = 0;
	    		mService.setLastTwittId(lastId);
			}

			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				mBound = false;
			}		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent(); 
		accountId = intent.getIntExtra("account_id", 0);
		twitter = new TwitterFactory().getInstance();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 // Bind to LocalService        
		mAdapter = new TweetsAdapter(this, accountId);
		
       
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = new Intent(this, com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.class);
		intent.setAction("startChecking");
		getApplicationContext().bindService(intent, mConnection, BIND_AUTO_CREATE);
		loadTweets();
	}
	
	 @Override
	    protected void onPause() {
	        super.onPause();
	        // Unbind from the service
	        if (mBound) {
	            getApplicationContext().unbindService(mConnection);
	            mBound = false;
	            mService = null;
	        }
	    }
	
	private void downloadNewTweets(){
		if (mBound)
			if(mService.downloadNewTweets() != null)
				mAdapter.addNewTweets(mService.downloadNewTweets());
	}
	
	
	private void loadTweets(){
		if(mAdapter == null){
				mAdapter = new TweetsAdapter(this, accountId);
			}
			setListAdapter(mAdapter);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.tweets_activity_menu, menu);
	    return true;
	}
	
	private void addNewTweet(){
		startActivity(new Intent(this, Post_Tweet_Activity.class));
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId())
		{
		case R.id.tweets_activity_menu_item1:
			downloadNewTweets();
			return true;
		case R.id.tweets_activity_menu_item2:
			addNewTweet();
			return true;
		case R.id.tweets_activity_menu_item3:
			return true;
		case R.id.tweets_activity_menu_item4:
			return true;
		case R.id.tweets_activity_menu_item5:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	  
}
