package com.leebrimelow.twitter.Service;

import java.util.ArrayList;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Activity.Twitts_Activity;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class Twitter_Loader_Service extends Service {

	
	private static Notification newTwittsNotification;
	private static NotificationManager newTwittsNotificationManager;	
	private static final int TWITTS_NOTIFICATION_ID = 127;
	private static final int TIME_DELAY_TWITTS_CHECKUP = 600000;// 10 minutes time delay
	private static ArrayList<Status> newTwittsList;
	private static final Handler mHandler = new Handler();
	private static long lastTwittId;
	private static boolean isDownloadTwitts;
	private final LocalBinder mBinder = new LocalBinder();
	private final Twitter twitter = new TwitterFactory().getInstance();
	public class LocalBinder extends Binder {
	      
		public Twitter_Loader_Service getService() {
			 return Twitter_Loader_Service.this;
		 }
	}
	
	private Runnable mTwittsCheckup = new Runnable() {
		
		public final void run() {
			// TODO Auto-generated method stub
			if (!isDownloadTwitts)
				checkingNewTwitts();
		}
	};
	
	public void onCreate() {
		
		if(newTwittsNotification == null)
			createNewTwittsNotification();
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mHandler.postDelayed(mTwittsCheckup, TIME_DELAY_TWITTS_CHECKUP);
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public void setLastTwittId(long lastId){
		
		lastTwittId = lastId;
	}
	
	public void checkingNewTwitts(){
		
		try {
			newTwittsList = twitter.getHomeTimeline(new Paging(lastTwittId));
			if( newTwittsList.size() > 0){
				isDownloadTwitts = true;
				lastTwittId = newTwittsList.get(0).getId();
				newTwittsNotificationManager.notify(TWITTS_NOTIFICATION_ID, newTwittsNotification);
			}
			else
				mHandler.postDelayed(mTwittsCheckup, TIME_DELAY_TWITTS_CHECKUP);	
				
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createNewTwittsNotification(){
		
		String ns = Context.NOTIFICATION_SERVICE;
		newTwittsNotificationManager = (NotificationManager) getSystemService(ns);
		
		
		CharSequence tickerText = getText(R.string.app_name); //!!!!!!!! 
		long when = System.currentTimeMillis();
		int icon = android.R.drawable.stat_notify_sync;
		
		newTwittsNotification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = getText(R.string.app_name); //!!!!!!!!!!
		CharSequence contentText = getText(R.string.app_name); //!!!!!!!!!!
		Intent notificationIntent = new Intent(this, Twitts_Activity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		newTwittsNotification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	}

	public ArrayList<Status> downloadNewTwitts(){
		
		if (isDownloadTwitts){
			isDownloadTwitts = false;
			mHandler.postDelayed(mTwittsCheckup, TIME_DELAY_TWITTS_CHECKUP);
			newTwittsNotificationManager.cancel(TWITTS_NOTIFICATION_ID);
			return newTwittsList;
		}
		return null;
	}
}
