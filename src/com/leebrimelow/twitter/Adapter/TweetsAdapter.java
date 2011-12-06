package com.leebrimelow.twitter.Adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Provider.KAVE_Content_Provider;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TweetsAdapter extends BaseAdapter {

	private int accountId;
	
	private static Context mContext;
	private static LayoutInflater inflater;
	private Cursor oldTweets; 
	private ArrayList<Status> newTweets;
	private SQLiteOpenHelper mTwitterSQLiteOpenHelper;
	private long lastTwittId;
	
	public TweetsAdapter(Context context, int acId) {
		// TODO Auto-generated constructor stub
		mContext = context;
		accountId = acId;
		inflater = LayoutInflater.from(mContext);
		newTweets = new ArrayList<Status>();
		
		oldTweets = mContext.getContentResolver().query(KAVE_Content_Provider.CONTENT_URI_ASATUSES, new String[]{"json_status_obj"}, "account_id=?", new String[]{String.valueOf(accountId)}, null);
		
		if(oldTweets.moveToLast()){
			try {
				lastTwittId = ((Status) new JSONObject(oldTweets.getString(oldTweets.getColumnIndex("json_status")))).getId();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				lastTwittId = 0;
			}
		}
		
	}
	
	
	
	public long getLastId(){
		
		return lastTwittId;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (newTweets != null)
			count += newTweets.size();
		if (oldTweets.moveToFirst())
			count += oldTweets.getCount();
		return count;
	}
	
	public void addNewTweets(ArrayList<Status> arrayList){
		if (arrayList.size() > 0){
			lastTwittId = arrayList.get(0).getId();
			newTweets.add(0,(Status) arrayList);
			notifyDataSetChanged();
		}		
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if ((newTweets != null) && (oldTweets.moveToFirst())){
			
			if(arg0 < newTweets.size())
				return newTweets.get(arg0);
			
			else if (arg0 < getCount()){
				
				oldTweets.moveToPosition(arg0 - newTweets.size());
				try {
					Status status = (Status) new JSONObject(oldTweets.getString(oldTweets.getColumnIndex("json_string")));
					return status;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}				
				
			}else
				return null;
			
		}else if(oldTweets.moveToFirst()){
			
			if (arg0 < oldTweets.getCount()){
				
				oldTweets.moveToPosition(arg0);
				try {
					Status status = (Status) new JSONObject(oldTweets.getString(oldTweets.getColumnIndex("json_string")));
					return status;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				
			}else
				return null;
			
		}else if(arg0 < newTweets.size())			
			return newTweets.get(arg0);
			
		else
			return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = newView(position, parent);
		}
		bindView(position, convertView);
		
		return convertView;
	}
	
	private View newView(int postion, ViewGroup parent) {
		View view = inflater.inflate(R.layout.status_list_item, parent, false);
		ViewHolder holder = getHolder(view);
		view.setTag(holder);
		return view;
	}
	
	private void bindView(int position, View conveView) {
		ViewHolder holder = (ViewHolder) conveView.getTag();
		holder.setStatus((Status) getItem(position));
	}
	
	private ViewHolder getHolder(View view) {
		ViewHolder holder = new ViewHolder();
		holder.avatar = (ImageView) view.findViewById(R.id.user_avatar);
		holder.screenName = (TextView) view.findViewById(R.id.status_user_name_text);
		holder.statusText = (TextView) view.findViewById(R.id.status_text);
		return holder;
	}
	
	class ViewHolder{
		public ImageView avatar;
		public TextView statusText;
		public TextView screenName;
		
		public void setStatus(Status status) {
			final User user = status.getUser();
			screenName.setText(user.getScreenName());
			statusText.setText(status.getText());			
		} 
	}
}
