package com.leebrimelow.twitter;

import android.app.ListActivity;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import android.os.Bundle;
import android.widget.ListAdapter;

public class User_Timeline_Activity extends ListActivity {
	
	private Twitter twitter; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		twitter = new TwitterFactory().getInstance();
		
	}

}
