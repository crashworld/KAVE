package com.leebrimelow.twitter;

import java.util.List;

import com.leebrimelow.twitter.Adapter.MainMenuAdapter;
import com.leebrimelow.twitter.Activity.AuthActivity;
import com.leebrimelow.twitter.Activity.MainActivity;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;

import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ExpandableListView;

public class Main extends ExpandableListActivity{
    /** Called when the activity is first created. */
	
	private static MainMenuAdapter mAdapter;
	private Twitter_Loader_Poster_Service tlps;
	protected boolean bound = false;
	
	private ServiceConnection conn = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			bound = false;
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
				startActivity(new Intent(this, AuthActivity.class));
				break;
			default:
				// получаем id текущего акаунта 
				String accountId = mAdapter.getAccountId(groupPosition,childPosition);
					
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("account_id", accountId);
				startActivity(intent);
				break;
			}
			break;
		case 1:
			switch(childPosition){
			case 0:		break;

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
		super.onStop();
		if (bound)
			unbindService(conn);
	}
}