package com.leebrimelow.twitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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


public class TwittsAdapter extends BaseAdapter {

	private int accountId;
	
	private static Context mContext;
	private static LayoutInflater inflater;
	private Cursor oldTwitts; 
	private ArrayList<Status> newTwitts;
	private SQLiteOpenHelper mTwitterSQLiteOpenHelper;
	private long lastTwittId;
	
	public TwittsAdapter(Context context, int acId) {
		// TODO Auto-generated constructor stub
		mContext = context;
		accountId = acId;
		inflater = LayoutInflater.from(mContext);
		newTwitts = new ArrayList<Status>();
		//mTwitterSQLiteOpenHelper = SQLiteOpenHelper().getInstace();
		oldTwitts = mTwitterSQLiteOpenHelper.getReadableDatabase().rawQuery("SELECT json_status FROM json_status WHERE id=? ORDER BY date", new String[]{String.valueOf(accountId)});
		oldTwitts.moveToLast();
		try {
			lastTwittId = ((Status) new JSONObject(oldTwitts.getString(oldTwitts.getColumnIndex("json_status")))).getId();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lastTwittId = 0;
		}
	}
	
	
	
	public long getLastId(){
		
		return lastTwittId;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (newTwitts != null)
			count += newTwitts.size();
		if (oldTwitts.moveToFirst())
			count += oldTwitts.getCount();
		return count;
	}
	
	public void addNewTwitts(ArrayList<Status> arrayList){
		if (arrayList.size() > 0){
			lastTwittId = arrayList.get(0).getId();
			newTwitts.add(0,(Status) arrayList);
			notifyDataSetChanged();
		}		
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if ((newTwitts != null) && (oldTwitts.moveToFirst())){
			
			if(arg0 < newTwitts.size())
				return newTwitts.get(arg0);
			
			else if (arg0 < getCount()){
				
				oldTwitts.moveToPosition(arg0 - newTwitts.size());
				try {
					Status status = (Status) new JSONObject(oldTwitts.getString(oldTwitts.getColumnIndex("json_string")));
					return status;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}				
				
			}else
				return null;
			
		}else if(oldTwitts.moveToFirst()){
			
			if (arg0 < oldTwitts.getCount()){
				
				oldTwitts.moveToPosition(arg0);
				try {
					Status status = (Status) new JSONObject(oldTwitts.getString(oldTwitts.getColumnIndex("json_string")));
					return status;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				
			}else
				return null;
			
		}else if(arg0 < newTwitts.size())			
			return newTwitts.get(arg0);
			
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
