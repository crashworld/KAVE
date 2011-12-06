package com.leebrimelow.twitter.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Provider.KAVE_Content_Provider;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;


import android.app.TabActivity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	private Twitter twitter;
	private int account_id;
	private String display_name;
	private User current_user;
	private ContentResolver mContentResolver;
	private Button previouse_but, current_button, next_button;
	private boolean isBound;
	private Twitter_Loader_Poster_Service mService;
	
	private ServiceConnection conn = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			isBound = false;
			mService = null;
		}

		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			isBound = true;
			LocalBinder lb = (LocalBinder) arg1;
			mService = lb.getService();			
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = getIntent();		
		//account_id = intent.getIntExtra("account_id", 0);
		display_name = intent.getStringExtra("display_name");
		createTabView();	
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//bindService(new Intent(this, com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.class), conn, BIND_AUTO_CREATE);
		mContentResolver = getContentResolver();
		//setupTwitter();	
		setupContentView();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (isBound)
			unbindService(conn);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.tweet_actions, menu);
	}
	
	private void setupTwitter(){
		
		twitter = new TwitterFactory().getInstance();		
		
		String consumerKey, consumerSecretKey;
		
		// получаем необходимые ключи по id акаунта
		consumerKey = getConsumerKey();
		consumerSecretKey = getConsumerSecretKey();
	
		AccessToken accessToken = new AccessToken(consumerKey, consumerSecretKey);
		
		
	    twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
	    twitter.setOAuthAccessToken(accessToken);
	}
	
	private void createTabView(){
		
		
		    TabHost tabHost = getTabHost();  // The activity TabHost
		    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		    Intent intent;  // Reusable Intent for each tab		    
		    
		    // Create an Intent to launch an Activity for the tab (to be reused)
//		    intent = new Intent().setClass(this, Friends_Tweets_Activity.class);
		    intent = new Intent().setClass(this, Tweets_Activity.class);
		    intent.putExtra("account_id", account_id);		    
		    
		    // Initialize a TabSpec for each tab and add it to the TabHost
		    spec = tabHost.newTabSpec("last_twitts").setIndicator("Twitts").setContent(intent);
		    tabHost.addTab(spec);

		    // Do the same for the other tabs
		    intent = new Intent().setClass(this, Friends_Tweets_Activity.class);		    
		    spec = tabHost.newTabSpec("frieds").setIndicator("Friends Twitts").setContent(intent);
		    tabHost.addTab(spec);
		    

		    intent = new Intent().setClass(this, User_Timeline_Activity.class);
		    spec = tabHost.newTabSpec("user_timeline").setIndicator("User Timeline").setContent(intent);
		    tabHost.addTab(spec);
		    		    
		    tabHost.setCurrentTab(0);
	}
	
	private void setupContentView(){
		
		previouse_but = (Button) findViewById(R.id.previous_account);
		current_button = (Button) findViewById(R.id.current_account);
		next_button = (Button) findViewById(R.id.next_account);
		
		current_button.setText(display_name);
		current_button.setBackgroundColor(Color.rgb(255, 166, 33));
	}

	 private String getConsumerKey(){
	    	
		// Cursor account = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_ACCOUNTS, new String[]{"consumer_key"}, "account_id=?", new String[] {String.valueOf(account_id)}, null); 
		return  mService.getUserAccessToken(display_name); //account.getString(account.getColumnIndex("consumr_key"));
	 }

	 private String getConsumerSecretKey(){
	    	
	   	return mService.getUserAccessTokenSecret(display_name);
	 }
	 
	 private void setupCurrentUser(long account_id){
		 
		 Cursor account = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_ACCOUNTS, new String[]{"json_user_obj"}, "account_id=?", new String[] {String.valueOf(account_id)}, null);
		 if (account.moveToFirst()){
			 String json_user_obj = account.getString(account.getColumnIndex("json_user_obj"));
			 try {
				current_user = (User) new JSONObject(json_user_obj);
			 } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
		 }
	 }

}
