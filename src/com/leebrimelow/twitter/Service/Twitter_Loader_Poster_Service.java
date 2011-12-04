package com.leebrimelow.twitter.Service;

import java.util.ArrayList;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Activity.Tweets_Activity;

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

public class Twitter_Loader_Poster_Service extends Service {

	
	private static Notification newTweetsNotification; //уведомление о новых твитах
	private static NotificationManager newTweetsNotificationManager; // манеджер уведомление о новых твитах
	private static final int Tweets_NOTIFICATION_ID = 127;// ID уведомление о новых твитах
	private static final int TIME_DELAY_Tweets_CHECKUP = 600000;// 10 минут перерыв между проверками на наличие новых твитов
	private static ArrayList<Status> newTweetsList; // массив закачаных твитов
	private static final Handler mHandler = new Handler(); 
	private static long lastTwittId; // Id твита начиная с которого необходимо закачивать твиты
	private static boolean isDownloadingTweets; // состаяние закачивания новых твитов из интернета
	private static boolean isDownloadedTweets; // состояние закачанных твитов, т.е. обновил закачанную информацию пользователь или нет
	private final LocalBinder mBinder = new LocalBinder(); 
	private final Twitter twitter = new TwitterFactory().getInstance();
	public class LocalBinder extends Binder {
	      
		public Twitter_Loader_Poster_Service getService() {
			 return Twitter_Loader_Poster_Service.this;
		 }
	}
	
	private Runnable mTweetsCheckup = new Runnable() {
		
		public final void run() {
			// TODO Auto-generated method stub
			if (!isDownloadedTweets)
				checkingNewTweets();
		}
	};
	
	public void onCreate() {
		
		if(newTweetsNotification == null)
			createNewTweetsNotification();
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(!isDownloadingTweets){
			
			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		if(!isDownloadingTweets){
			
			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
		}
		return mBinder;
	}
	
	public void setLastTwittId(long lastId){
		
		lastTwittId = lastId;
	}
	
	
	// метод проверяет наличие новых твитов и выводит уведомление если таковые есть
	
	public void checkingNewTweets(){
		
		try {
			newTweetsList = twitter.getHomeTimeline(new Paging(lastTwittId));
			if( newTweetsList.size() > 0){
				isDownloadingTweets = false;
				isDownloadedTweets = true;
				lastTwittId = newTweetsList.get(0).getId();
				newTweetsNotificationManager.notify(Tweets_NOTIFICATION_ID, newTweetsNotification);
			}
			else
				mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);	
				
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//метод создаёт уведомление для пользователя о новых твитах
	
	private void createNewTweetsNotification(){
		
		String ns = Context.NOTIFICATION_SERVICE;
		newTweetsNotificationManager = (NotificationManager) getSystemService(ns);
		
		
		CharSequence tickerText = getText(R.string.app_name); //!!!!!!!! 
		long when = System.currentTimeMillis();
		int icon = android.R.drawable.stat_notify_sync;
		
		newTweetsNotification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = getText(R.string.app_name); //!!!!!!!!!!
		CharSequence contentText = getText(R.string.app_name); //!!!!!!!!!!
		Intent notificationIntent = new Intent(this, Tweets_Activity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		newTweetsNotification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	}

	//метод возвращает массив закачанных статусов 
	
	public ArrayList<Status> downloadNewTweets(){
		
		if (isDownloadedTweets){
			isDownloadedTweets = false;
			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
			newTweetsNotificationManager.cancel(Tweets_NOTIFICATION_ID);
			return newTweetsList;
		}
		return null;
	}
	
	// метод посылающий новый статус пользователя. Возвращает true в случае удачи.
	
	public boolean postingTweet(String postText){
		
		boolean isPosted = false;
		try {
			if(twitter.updateStatus(postText).getText().equals(postText))
				isPosted = true;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return isPosted;
		}
		return isPosted;
	}
}
