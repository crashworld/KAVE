package com.leebrimelow.twitter.Activity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.leebrimelow.twitter.R;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	private Twitter twitter;
	private int accountId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createTabView();
		setupTwitter();
		
	}
	
	private void setupTwitter(){
		
		twitter = new TwitterFactory().getInstance();
		
		Intent intent = getIntent();
		
		accountId = intent.getIntExtra("account_id", 0);
		
		String consumerKey, consumerSecretKey;
		
		// получаем необходимые ключи по id акаунта
		consumerKey = getConsumerKey(accountId);
		consumerSecretKey = getConsumerSecretKey(accountId);
	
		AccessToken accessToken = new AccessToken(consumerKey, consumerSecretKey);
		
		
	    twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
	    twitter.setOAuthAccessToken(accessToken);
	}
	
	private void createTabView(){
		
		
		    TabHost tabHost = getTabHost();  // The activity TabHost
		    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		    Intent intent;  // Reusable Intent for each tab		    
		    
		    // Create an Intent to launch an Activity for the tab (to be reused)
		    intent = new Intent().setClass(this, Tweets_Activity.class);	    
		    
		    intent.putExtra("account_id", accountId);
		    
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
	

	 private String getConsumerKey(int accountId){
	    	
	   	return null;
	 }

	 private String getConsumerSecretKey(int accountId){
	    	
	   	return null;
	 }

}
