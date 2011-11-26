package com.leebrimelow.twitter;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderElement;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class OAuthHeader implements Header {
	private static final String TAG = null;
	private String httpMeth;
	private String url;
	private final String SIG_ALGO = "HMAC-SHA1";
	private final String OAUTH_VER = "1.0";
	private Map<String, String> hdr = new TreeMap<String, String>();
	private String consumerSecret = "";
	
	public void setConsumerKey(String consumerKey) {
		hdr.put("oauth_consumer_key", consumerKey);
		if (!consumerSecret.equals(""))
			hdr.put("oauth_signature", makeSignature(makeSignatureBaseString()));
	}
	
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
		hdr.put("oauth_signature", makeSignature(makeSignatureBaseString()));
	}
	
	public OAuthHeader(String url, String callbackUrl, String http_meth) {
		try {
			this.httpMeth = http_meth;
			this.url = url;
			hdr.put("oauth_callback", callbackUrl);
			hdr.put("oauth_signature_method", SIG_ALGO);
			hdr.put("oauth_version", OAUTH_VER);

			DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			byte[] digsrc = f.format(new Date()).getBytes();
			byte[] digest = MessageDigest.getInstance("MD5").digest(digsrc);
			String nonce = Base64.encodeToString(digest, Base64.NO_WRAP|Base64.NO_PADDING)
					.replaceAll("[\\+\\\\\\/]", "");
			hdr.put("oauth_nonce", nonce);
			hdr.put("oauth_timestamp",
					String.valueOf(System.currentTimeMillis() / 1000));
		} catch (Exception e) {
			return;
		}
	}

	private String makeParamString() {
		StringBuilder b = new StringBuilder();
		for (String s : hdr.keySet()) {
			b.append(Uri.encode(s));
			b.append("=");
			b.append(Uri.encode(hdr.get(s)));
			b.append("&");
		}
		b.deleteCharAt(b.length() - 1); // trim leftover '&'
		return b.toString();
	}

	private String makeSignatureBaseString() {
		StringBuilder b = new StringBuilder(httpMeth.toUpperCase());
		b.append("&");
		b.append(Uri.encode(url));
		b.append("&");
		b.append(Uri.encode(makeParamString()));
		return b.toString();
	}

	private String makeSignature(String base) {
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec sks = new SecretKeySpec(
					consumerSecret.concat("&").getBytes(), "HmacSHA1");
			mac.init(sks);
			byte[] digest = mac.doFinal(base.getBytes());
			String sig = Base64.encodeToString(digest, Base64.NO_WRAP);
			Log.d(TAG, "The signature is: " + sig);
			return sig;
		} catch (InvalidKeyException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		}
	}

	public HeaderElement[] getElements() throws ParseException {
		// TODO Auto-generated method stub
		List<HeaderElement> he = new ArrayList<HeaderElement>();
		for (String name : hdr.keySet()) {
			he.add(new BasicHeaderElement(name, hdr.get(name)));
		}
		return (HeaderElement[]) he.toArray();
	}

	public String getName() {
		return "Authorization";
	}

	public String getValue() {
		StringBuilder b = new StringBuilder("OAuth ");
		for (String s : hdr.keySet()) {
			b.append(Uri.encode(s));
			b.append("=\"");

			b.append(Uri.encode(hdr.get(s)));
			b.append("\", ");
		}
		b.setLength(b.length() - 2);
		return b.toString();
	}

}
