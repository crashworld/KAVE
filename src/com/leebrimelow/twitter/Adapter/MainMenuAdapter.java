package com.leebrimelow.twitter.Adapter;


import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Provider.KAVE_Content_Provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainMenuAdapter implements ExpandableListAdapter {
	private static final int GROUP_COUNT = 3;
	private static String mGroupTitle[];
	private static Context mContext;
	private static LayoutInflater inflater;
	private static Cursor mAccontCursor, mSearchCursor, mTrendCursor;
	private static boolean isAccountCursor, isSearchCursor, isTrendCursor;
	private static ContentResolver mContentResolver;
	
	
	public MainMenuAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mGroupTitle = mContext.getResources().getStringArray(R.array.menu_categories);
		mContentResolver = context.getContentResolver();
		
		
		// подгрузка данных из бд
//		getData();
		
		
	}
	
		// Выборка из бд с помощью контент провайдера курсоров Акаунтов, Поисков, Трендов;
	private void getData(){
		mAccontCursor = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_AACCOUNTS, null, null, null, null);
		if(mAccontCursor.moveToFirst())
			isAccountCursor = true;
		mTrendCursor = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_TRENDS, null, null, null, null);
		if(mTrendCursor.moveToFirst())
			isTrendCursor = true;
		mSearchCursor = mContentResolver.query(KAVE_Content_Provider.CONTENT_URI_SEARCHES, null, null, null, null);
		if(mSearchCursor.moveToFirst())
			isSearchCursor = true;
	}
	public int getAccountId(int groupPosition, int childPosition){
		if((isAccountCursor) && (groupPosition == 0)){
			mAccontCursor.moveToPosition(childPosition);
			return mAccontCursor.getInt(mAccontCursor.getColumnIndex("account_id"));
		}
		else
			return 0;
	}
	
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg0){
		case 0:
			switch(arg1){
			case 0:
				return mContext.getResources().getString(R.string.add_new);
			default:
				if (isAccountCursor){
					if(arg1 < 2 + mAccontCursor.getCount())
					{
						mAccontCursor.moveToPosition(arg1);
						return mAccontCursor.getString(mAccontCursor.getColumnIndex("screen_name"));
					}else
						return null;
				}else
					return null;
				
			}
		case 1:
			switch(arg1){
			case 0:
				return mContext.getResources().getString(R.string.add_search);
			default:
				if(isSearchCursor){
					if(arg1 < 1 + mSearchCursor.getCount())
					{
						mSearchCursor.moveToPosition(arg1);
						return mSearchCursor.getString(mSearchCursor.getColumnIndex("search_title"));
					}
					else
						return null;	
				}else
					return null;
			}			
		case 2:
			if(isTrendCursor){
				if(arg1 < mTrendCursor.getCount())
				{
					mTrendCursor.moveToPosition(arg1+1);
					return mTrendCursor.getString(mTrendCursor.getColumnIndex("trend_title"));
				}
				else
					return null;
			}else
				return null;
		default:
			return null;
		}
	}

	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg0 * arg1 + arg1;
	}

	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		if (arg3 == null) {
			arg3 = newChildView(arg0, arg1, getChildId(arg0, arg1), arg4);
		}
		bindChildView(arg0, arg1, getChildId(arg0, arg1), arg3);
		
		return arg3;
	}

	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		int count = 0;
		switch(arg0){		
		case 0:
			if(isAccountCursor)
				count = mAccontCursor.getCount();
			return 1 + count;
		case 1:
			if(isSearchCursor)
				count = + mSearchCursor.getCount();
			return 1 + count;
		case 2:
			if(isTrendCursor)
				count = mTrendCursor.getCount();
			
			return count;
		default:
			return 0;
		}
	}

	public long getCombinedChildId(long arg0, long arg1) {
		// TODO Auto-generated method stub
		return arg0 * arg1 + arg1;
	}

	public long getCombinedGroupId(long arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		if ((arg0 >= 0) && (arg0 < 3))
			return mGroupTitle[arg0];
		else 
			return null;
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return GROUP_COUNT;
	}

	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_menu_item, null);		
		((TextView) view.findViewById(R.id.text)).setText((String) getGroup(arg0));
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onGroupCollapsed(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onGroupExpanded(int arg0) {
		// TODO Auto-generated method stub

	}

	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	private View newChildView(int groupPosition, int childPosition, long id, ViewGroup parent) {
		View view = inflater.inflate(R.layout.main_menu_item, parent, false);
		ViewHolder holder = getHolder(view);
		view.setTag(holder);
		return view;
	}
	
	private void bindChildView(int groupPosition, int childPosition, long id, View conveView) {
		ViewHolder holder = (ViewHolder) conveView.getTag();
		holder.textView.setText((String) getChild(groupPosition, childPosition));
		holder.imageView.setImageBitmap(getImage(id));
	}
	
	private ViewHolder getHolder(View view) {
		ViewHolder holder = new ViewHolder();
		holder.imageView = (ImageView) view.findViewById(R.id.image);
		holder.textView = (TextView) view.findViewById(R.id.text);
		return holder;
	}
		
	private Bitmap getImage(long id){
		
		return null;		
	}
	
	class ViewHolder{
		public ImageView imageView;
		public TextView textView;
	}
}
