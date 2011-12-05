package com.leebrimelow.twitter.Activity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import twitter4j.http.AccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leebrimelow.twitter.R;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service;
import com.leebrimelow.twitter.Service.Twitter_Loader_Poster_Service.LocalBinder;

public class AuthActivity extends Activity {
	private static final int ALERT_CONN_UNAVAIL = 0;
	private static final int ALERT_CANT_AUTH = 1;
	private static final int ALERT_AUTH_FAILED = 2;

	private WebView authWebView;
	private String consumerKey;
	private String consumerSecret;
	private boolean isBound = false;
	private Twitter_Loader_Poster_Service service;

	private ServiceConnection conn = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			isBound = false;
			service = null;
		}

		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			isBound = true;
			LocalBinder lb = (LocalBinder) arg1;
			service = lb.getService();
			authWebView.loadUrl(service.startAuth(consumerKey, consumerSecret)
					.toString());
		}
	};

	private WebViewClient client = new WebViewClient() {
		@Override
		public void onLoadResource(WebView view, String url) {
			Uri u = Uri.parse(url);

			if (u.getHost().equals("kave.flat32.by")) {
				String tok = u.getQueryParameter("oauth_token");
				authWebView.setVisibility(View.INVISIBLE);
				if (tok != null) {
					saveCredentials(service.receiveAccessToken());
					finish();
				} else {
					showDialog(ALERT_AUTH_FAILED);
				}
			} else {
				super.onLoadResource(view, url);
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder err = new AlertDialog.Builder(this);

		err.setTitle(R.string.error).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		switch (id) {
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
		try {
			super.onResume();
			Properties p = new Properties();
			p.load(getResources().openRawResource(R.raw.oauth));
			consumerKey = (String) p.get("auth.consumer_key");
			consumerSecret = (String) p.get("auth.consumer_secret");
			Intent i = new Intent(this, Twitter_Loader_Poster_Service.class);
			boolean b = bindService(i, conn, Context.BIND_AUTO_CREATE);
			Log.d(null, String.format("Connection made: %s", b));
		} catch (NotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void saveCredentials(AccessToken t) {
		SharedPreferences sp = getSharedPreferences("accounts", 0);
		Editor spe = sp.edit();

		String str_users_list = sp.getString("users_list", "");
		List<String> users_list = Arrays.asList(str_users_list.split(" "));
		if (!users_list.contains(t.getScreenName())) {
			str_users_list = str_users_list.concat(t.getScreenName()).concat(
					" ");
			spe.putString("users_list", str_users_list);
		}

		spe.putString(String.format("token_%s", t.getScreenName()),
				t.getToken());
		spe.putString(String.format("token_s_%s", t.getScreenName()),
				t.getTokenSecret());
		spe.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (isBound)
			unbindService(conn);
	}
}
