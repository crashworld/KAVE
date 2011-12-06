package com.leebrimelow.twitter;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.leebrimelow.twitter.Adapter.MainMenuAdapter;
import com.leebrimelow.twitter.Activity.AuthActivity;
import com.leebrimelow.twitter.Activity.MainActivity;
import com.leebrimelow.twitter.Activity.Post_Tweet_Activity;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;

import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class Main extends ExpandableListActivity{
    /** Called when the activity is first created. */
	
	private static MainMenuAdapter mAdapter;
	private Twitter_Loader_Poster_Service tlps;
	private Twitter twitter;
	protected boolean bound = false;
	
	private ServiceConnection conn = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			LocalBinder lb = (LocalBinder) arg1;
			tlps = lb.getService();
			bound = true;
			List<String> usr = tlps.getAuthorizedUsers();
			mAdapter = new MainMenuAdapter(Main.this, usr);
			setListAdapter(mAdapter);
		}
	};

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Intent service = new Intent(this, Twitter_Loader_Poster_Service.class);
        bindService(service, conn, BIND_AUTO_CREATE);
        
    }
    
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		switch (groupPosition){
		case 0:
			switch(childPosition){
			case 0:
			    ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
			    NetworkInfo nInfo = cm.getActiveNetworkInfo();
			    //if (nInfo != null && nInfo.isConnected()) {
				startActivity(new Intent(this, AuthActivity.class));
			    //}else
			    //	Toast.makeText(this, "OFFLINE", Toast.LENGTH_LONG).show();	
				break;
			default:
				// получаем id текущего акаунта 
				String accountId = mAdapter.getAccountId(groupPosition,childPosition);
				setupTwitter(accountId);
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("account_id", accountId);
				intent.putExtra("display_name", accountId);
				startActivity(intent);
				break;
			}
			break;
		case 1:
			switch(childPosition){
			case 0:
				startActivity(new Intent(this, Post_Tweet_Activity.class));
				break;

			default:	break;								
			}
			break;
		case 2:			break;						
			
		default: break;
		}
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (bound){
			unbindService(conn);
			bound = false;
			tlps = null;
		}
	}
	
	private String getConsumerKey(String display_name){
    	
		// Cursor account = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_ACCOUNTS, new String[]{"consumer_key"}, "account_id=?", new String[] {String.valueOf(account_id)}, null); 
		return  tlps.getUserAccessToken(display_name); //account.getString(account.getColumnIndex("consumr_key"));
	 }

	 private String getConsumerSecretKey(String display_name){
	    	
	   	return tlps.getUserAccessTokenSecret(display_name);
	 }
	 
	 private void setupTwitter(String display_name){
			
			twitter = new TwitterFactory().getInstance();		
			
			String consumerKey, consumerSecretKey;
			
			// получаем необходимые ключи по id акаунта
			consumerKey = getConsumerKey(display_name);
			consumerSecretKey = getConsumerSecretKey(display_name);
		
			AccessToken accessToken = new AccessToken(consumerKey, consumerSecretKey);
			
			
//		    twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
//		    twitter.setOAuthAccessToken(accessToken);
		}
	
	
}