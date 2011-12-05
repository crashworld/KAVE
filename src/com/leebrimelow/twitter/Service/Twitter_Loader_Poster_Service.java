package com.leebrimelow.twitter.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Activity.Tweets_Activity;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class Twitter_Loader_Poster_Service extends Service {

	private static Notification newTweetsNotification; // ����������� � �����
														// ������
	private static NotificationManager newTweetsNotificationManager; // ��������
																		// �����������
																		// �
																		// �����
																		// ������
	private static final int Tweets_NOTIFICATION_ID = 127;// ID ����������� �
															// ����� ������
	private static final int TIME_DELAY_Tweets_CHECKUP = 600000;// 10 �����
																// ������� �����
																// ���������� ��
																// ������� �����
																// ������
	private static ArrayList<Status> newTweetsList; // ������ ��������� ������
	private static final Handler mHandler = new Handler();
	private static long lastTwittId; // Id ����� ������� � �������� ����������
										// ���������� �����
	private static boolean isDownloadingTweets; // ��������� ����������� �����
												// ������ �� ���������
	private static boolean isDownloadedTweets; // ��������� ���������� ������,
												// �.�. ������� ����������
												// ���������� ������������ ���
												// ���
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

	private SharedPreferences authData;

	public void onCreate() {

		if (newTweetsNotification == null)
			createNewTweetsNotification();

		authData = getSharedPreferences("accounts", 0);
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (!isDownloadingTweets) {

			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		if (!isDownloadingTweets) {

			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
		}
		return mBinder;
	}

	public void setLastTwittId(long lastId) {

		lastTwittId = lastId;
	}

	// ����� ��������� ������� ����� ������ � ������� ����������� ���� �������
	// ����

	public void checkingNewTweets() {

		try {
			newTweetsList = twitter.getHomeTimeline(new Paging(lastTwittId));
			if (newTweetsList.size() > 0) {
				isDownloadingTweets = false;
				isDownloadedTweets = true;
				lastTwittId = newTweetsList.get(0).getId();
				newTweetsNotificationManager.notify(Tweets_NOTIFICATION_ID,
						newTweetsNotification);
			} else
				mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ����� ������ ����������� ��� ������������ � ����� ������

	private void createNewTweetsNotification() {

		String ns = Context.NOTIFICATION_SERVICE;
		newTweetsNotificationManager = (NotificationManager) getSystemService(ns);

		CharSequence tickerText = getText(R.string.app_name); // !!!!!!!!
		long when = System.currentTimeMillis();
		int icon = android.R.drawable.stat_notify_sync;

		newTweetsNotification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = getText(R.string.app_name); // !!!!!!!!!!
		CharSequence contentText = getText(R.string.app_name); // !!!!!!!!!!
		Intent notificationIntent = new Intent(this, Tweets_Activity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		newTweetsNotification.setLatestEventInfo(context, contentTitle,
				contentText, contentIntent);
	}

	// ����� ���������� ������ ���������� ��������

	public ArrayList<Status> downloadNewTweets() {

		if (isDownloadedTweets) {
			isDownloadedTweets = false;
			mHandler.postDelayed(mTweetsCheckup, TIME_DELAY_Tweets_CHECKUP);
			isDownloadingTweets = true;
			newTweetsNotificationManager.cancel(Tweets_NOTIFICATION_ID);
			return newTweetsList;
		}
		return null;
	}

	// ����� ���������� ����� ������ ������������. ���������� true � ������
	// �����.

	public boolean postingTweet(String postText) {

		boolean isPosted = false;
		try {
			if (twitter.updateStatus(postText).getText().equals(postText))
				isPosted = true;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return isPosted;
		}
		return isPosted;
	}

	/**
	 * ������ ����������� ���������� �������������
	 * 
	 * @param consumerKey
	 *            ���� "�����������" (���-�� ��������� ������ ����������)
	 * @param consumerSecret
	 *            ������ "�����������" (���-�� ��������� ������ ����������)
	 * @return ���� ����� ������� ������� �������, �� ���������� URI �������� �
	 *         ������ �����������, ����� null.
	 */
	public Uri startAuth(String consumerKey, String consumerSecret) {
		try {
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			RequestToken token = twitter.getOAuthRequestToken();
			return Uri.parse(token.getAuthenticationURL());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��������� ������ �������
	 * 
	 * @return ���� ����������� ������ ������� - ������������ ����� �������,
	 *         ����� - null
	 */
	public AccessToken receiveAccessToken() {
		try {
			return twitter.getOAuthAccessToken();
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��������� ������ �������������� �������������
	 * 
	 * @return ������ �������������, ���������������� � ����������
	 */
	public List<String> getAuthorizedUsers() {
		String str_users_list = authData.getString("users_list", "");
		return Arrays.asList(str_users_list.split(" "));
	}

	/**
	 * ��������� ��������� ����� ������ ������� �� ����� ������������
	 * 
	 * @param displayName
	 *            ��� ������������
	 * @return ��������� ����� ������ ������������
	 */
	public String getUserAccessToken(String displayName) {
		return authData.getString(String.format("token_%s", displayName), "");
	}

	/**
	 * ��������� ��������� ����� ������ ������� �� ����� ������������
	 * 
	 * @param displayName
	 *            ��� ������������
	 * @return ��������� ����� ������ �������
	 */
	public String getUserAccessTokenSecret(String displayName) {
		return authData.getString(String.format("token_s_%s", displayName), "");
	}

	/**
	 * ������� �� ������������ ������� ���������� ������������.
	 * 
	 * @param screenName
	 *            ��� ������������
	 * @return �������� ������������ � ������ ���������� ���������� �������� -
	 *         null
	 */
	public User followUser(String screenName) {
		try {
			return twitter.createFriendship(screenName);
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �������� ���������� �� ������������ ������� ���������� ������������.
	 * 
	 * @param screenName
	 *            ��� ������������
	 * @return �������� ������������, � ������ ���������� ���������� �������� -
	 *         null
	 */
	public User unfollowUser(String screenName) {
		try {
			return twitter.destroyFriendship(screenName);
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �������� ������ ��� "���������"
	 * 
	 * @param s
	 *            ���������� ������
	 * @return ���������� ������, � ������ ���������� ���������� �������� - null
	 */
	public Status markAsFavourite(Status s) {
		try {
			return twitter.createFavorite(s.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �������� ������ ��� ����
	 * 
	 * @param s
	 *            ���������� ������
	 * @return ����� ���������� ���������, � ������ ���������� ����������
	 *         �������� - null
	 */
	public User markAsSpam(Status s) {
		try {
			return twitter.reportSpam(s.getUser().getScreenName());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
       
	/**
	 * ������������� ��������� �� ������������
	 * 
	 * @param u
	 *            ����������� ������������
	 * @return ��������������� ������������, � ������ ���������� ����������
	 *         �������� - null
	 */
	public User blockUser(User u) {
		try {
			return twitter.createBlock(u.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
}
