package com.leebrimelow.twitter.Activity;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Post_Tweet_Activity extends Activity {

	private TextView charCounter;
	private Button postButton;
	private EditText tweetContent;
	private Twitter_Loader_Poster_Service mService;
	private boolean mBound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_tweet);
		setupView();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		  // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
	}
	
	// получение ссылок на виджеты, установка Listenerов 
	
	private void setupView(){
		charCounter = (TextView) findViewById(R.id.char_counter);
		postButton = (Button) findViewById(R.id.post_button);
		tweetContent = (EditText) findViewById(R.id.tweet_content);
	 
		// лисенер поглощает событие если количество символов в сообщение = 140 символам и выводит Тоаst
		tweetContent.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (tweetContent.getText().length() >= 140){
					Toast.makeText(v.getContext(), R.string.toast_too_many_chars, Toast.LENGTH_SHORT).show();
					return true;
				}
				else
					return false;
			}
		});
		
		// проверяет количество оставшихся возможных символов
		tweetContent.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 int charsLeft = 140 - s.length();
			        charCounter.setText(String.valueOf(charsLeft));
			        if (charsLeft <= 10) {
			          charCounter.setTextColor(Color.RED);
			        } else {
			          charCounter.setTextColor(Color.WHITE);
			        }
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// проверяет текс твита на корректность длины прежде чем добавить его
		postButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String postText = tweetContent.getText().toString();
			    int postLength = postText.length();
			    if (140 < postLength) {
			    	Toast.makeText(v.getContext(), R.string.toast_too_many_chars, Toast.LENGTH_SHORT).show(); 
			    } else if (0 == postLength) {
			      Toast.makeText(v.getContext(), R.string.toast_empty_status_text, Toast.LENGTH_SHORT).show();
			    } else {
			    	postingTweet(postText);
			    }
				
			}
		});
		
	}
	
	private void postingTweet(String postText){
		
		if (mService.postingTweet(postText)){
			
			Toast.makeText(this, R.string.toast_posted_tweet_successfully, Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
		}else{
		
			Toast.makeText(this, R.string.toast_posted_tweet_unsuccessful, Toast.LENGTH_SHORT).show();
			setResult(RESULT_CANCELED);			
		}
	}
	
	 private ServiceConnection mConnection = new ServiceConnection() {

	      	public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				LocalBinder binder = (LocalBinder) service;
	            mService = binder.getService();
	            mBound = true;
	           
			}

			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				mBound = false;
			}
	    };
}
