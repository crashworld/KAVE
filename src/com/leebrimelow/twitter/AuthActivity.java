package com.leebrimelow.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends Activity{
	private static final int ALERT_CONN_UNAVAIL = 0;
	private static final int ALERT_CANT_AUTH = 1;
	private static final int ALERT_AUTH_FAILED = 2;
	private final String REQUEST_TOKEN_ENDPOINT="http://api.twitter.com/oauth/request_token";
	private final String AUTH_URL = "http://api.twitter.com/oauth/authenticate?oauth_token=";
	private final String CALLBACK_URL = "http://kave.flat32.by/";
	private final String TAG = "AuthActivity";
	private final String USER_AGENT = "Kave/1.0";
	
	private WebView authWebView;
	private WebViewClient client = new WebViewClient(){
	@Override
		public void onLoadResource(WebView view, String url) {
			Uri u = Uri.parse(url);
			
			if(u.getHost().equals("kave.flat32.by")){
				String tok = u.getQueryParameter("oauth_token");
				if (tok != null){
					authWebView.setVisibility(View.INVISIBLE);
					finish();
				}
				else{
					showDialog(ALERT_AUTH_FAILED);
				}
			}
			else {
				super.onLoadResource(view, url);
			}
		}	
	};
	
	private AndroidHttpClient ahc;
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder err = new AlertDialog.Builder(this);
		
		err.setTitle(R.string.error)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		switch(id){
		case ALERT_CANT_AUTH:
			err.setMessage(R.string.can_t_start_auth);
			break;
		case ALERT_CONN_UNAVAIL:
			err.setMessage(R.string.internet_connection_required);
			break;
		case ALERT_AUTH_FAILED:
			err.setMessage(R.string.authorization_failed);
			break;
		}
		
		return err.create();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth);
		authWebView = (WebView) findViewById(R.id.authWebView);
		authWebView.setWebViewClient(client);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ahc = AndroidHttpClient.newInstance(USER_AGENT);		
		HttpUriRequest acquireRequestToken = new HttpPost(REQUEST_TOKEN_ENDPOINT);
		OAuthHeader oauth = new OAuthHeader(REQUEST_TOKEN_ENDPOINT, CALLBACK_URL, "POST");
		Properties p = new Properties();
		
		try {
			p.load(getResources().openRawResource(R.raw.oauth));
		} catch (NotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String consumerKey = (String) p.get("auth.consumer_key");
		oauth.setConsumerKey(consumerKey);
		String consumerSecret = (String) p.get("auth.consumer_secret");
		oauth.setConsumerSecret(consumerSecret);
		acquireRequestToken.setHeader(oauth);
		AsyncTask<HttpUriRequest, Void, HttpResponse> t = new AsyncTask<HttpUriRequest, Void, HttpResponse>(){

			@Override
			protected HttpResponse doInBackground(HttpUriRequest... arg0) {
				try {
					return ahc.execute(arg0[0]);
				} catch (IOException e) {
					return null;
				}
			}
			
			
			@Override
			protected void onPostExecute(HttpResponse result) {
				super.onPostExecute(result);
				if (result == null){
					showDialog(ALERT_CONN_UNAVAIL);
					return;
				}
				
				if (result.getStatusLine().getStatusCode() != 200){
					showDialog(ALERT_CANT_AUTH);
					return;
				}
				
				HttpEntity entity = result.getEntity();
				try {
					Map <String, String> h = new HashMap<String,String>(); 
					InputStreamReader r = new InputStreamReader(entity.getContent());
					BufferedReader r1 = new BufferedReader(r);
					
					String line = r1.readLine();
					for (String param: line.split("&")){
						String[] pv = param.split("=");
						h.put(pv[0],pv[1]);
					}
					String atoken = h.get("oauth_token");
					authWebView.loadUrl(AUTH_URL.concat(atoken));
					r1.close();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.execute(acquireRequestToken);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		ahc.close();
	}
}
