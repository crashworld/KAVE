package com.leebrimelow.twitter.DB;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;

import twitter4j.Status;
import twitter4j.User;

public class DataSaveHelper {

	private Context mContext;
	private ContentResolver mContentResolver;
	
	private static DataSaveHelper instanceOf;
	
	private DataSaveHelper(Context context){
		mContext = context;
		mContentResolver = mContext.getContentResolver();
	}
	
	private static final DataSaveHelper getInstance(Context context){
		
		if(instanceOf == null){
			instanceOf = new DataSaveHelper(context);
		}
		return instanceOf;
	}
	
	public boolean saveStatuses(ArrayList<Status> arg0){
		
		
		return false;
	}
	
	public boolean saveStatus(Status arg0){
		return false;
	}
	
	public boolean saveUsers(ArrayList<Status> arg0){
		return false;
	}
	
	public boolean saveUser(Status arg0){
		return false;
	}
	
	public boolean saveAvatars(ArrayList<User> arg0){
		return false;
	}

	public boolean saveAvatar(User arg0){
		return false;
	}
	
	public boolean saveAccounts(ArrayList<User> arg0){
		return false;
	}

	public boolean saveAccount(User arg0){
		return false;
	}
	
	public boolean saveTrends(){
		return false;
	}
	
	public boolean saveSearches(){
		return false;
	}
}
