package com.leebrimelow.twitter;

import java.util.ArrayList;

import com.leebrimelow.twitter.Twitter_Loader_Service.LocalBinder;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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

public class Twitts_Activity extends ListActivity {

	private TwittsAdapter mAdapter;
	private int accountId;
	private Twitter_Loader_Service mService;
	private boolean mBound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		accountId = getIntent().getIntExtra("account_id", 0);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 // Bind to LocalService
        Intent intent = new Intent(this, Twitter_Loader_Service.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadTwitts();
	}
	
	 @Override
	    protected void onStop() {
	        super.onStop();
	        // Unbind from the service
	        if (mBound) {
	            unbindService(mConnection);
	            mBound = false;
	        }
	    }
	
	private void downloadNewTwitts(){
		if (mBound)
			if(mService.downloadNewTwitts() != null)
				mAdapter.addNewTwitts(mService.downloadNewTwitts());
	}
	
	
	private void loadTwitts(){
		if(mAdapter == null){
				mAdapter = new TwittsAdapter(this, accountId);
				setListAdapter(mAdapter);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.twitts_activity_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId())
		{
		case R.id.twitts_activity_menu_item1:
			downloadNewTwitts();
			return true;
		case R.id.twitts_activity_menu_item2:
			return true;
		case R.id.twitts_activity_menu_item3:
			return true;
		case R.id.twitts_activity_menu_item4:
			return true;
		case R.id.twitts_activity_menu_item5:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	   private ServiceConnection mConnection = new ServiceConnection() {

	      	public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				LocalBinder binder = (LocalBinder) service;
	            mService = binder.getService();
	            mBound = true;
	    		long lastId = mAdapter.getLastId();
	    		mService.setLastTwittId(lastId);
			}

			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				mBound = false;
			}
	    };
	
}
