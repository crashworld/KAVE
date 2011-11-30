package com.leebrimelow.twitter;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class Main extends ExpandableListActivity{
    /** Called when the activity is first created. */
	
	private static MainMenuAdapter mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MainMenuAdapter(this);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	setListAdapter(mAdapter);
    }

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		
		
		switch (groupPosition){
		case 0:
			switch(childPosition){
			case 0:
				startActivity(new Intent(this, AuthActivity.class));
			case 1:

			default:
				// получаем id текущего акаунта 
				int accountId = mAdapter.getAccountId(groupPosition,childPosition);
					
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("account_id", accountId);
			}
		case 1:
			switch(childPosition){
			case 0:

			default:									
			}			
		case 2:								
			
		default:
		}
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
    
    
}