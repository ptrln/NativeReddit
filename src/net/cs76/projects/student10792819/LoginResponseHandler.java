/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This response handlers handles the response returned by the http request done by
 * RedditClient.login method. ListingActivity is required to refresh the page once log in is successful.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * The Class LoginResponseHandler.
 */
public class LoginResponseHandler extends TimeoutResponseHandler {

	/** The activity. */
	private final ListingActivity activity;
	
	/** The boolean user is logged in. */
	private boolean isLoggedIn = false;

	/**
	 * Instantiates a new login response handler.
	 *
	 * @param a the activity
	 */
	public LoginResponseHandler(ListingActivity a) {
		super(a);
		this.activity = a;
		
		SharedPreferences prefs = a.getPreferences(Context.MODE_PRIVATE);
		String cookie = prefs.getString("my_reddit_session", "");
		String modhash = prefs.getString("my_modhash", "");
		
		if (cookie.length() > 0 && modhash.length() > 0) {
			isLoggedIn = true;
			RedditClient.storeSession(cookie, modhash, a);
		}
	}
	
	/**
	 * Logged in.
	 *
	 * @return true, if successful
	 */
	public boolean loggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * Logout.
	 */
	public void logout() {
		SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
		
		RedditClient.clearSession();
		this.isLoggedIn = false;
		activity.refresh();
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onStartHandler()
	 */
	@Override
	protected void onStartHandler() {
		Toast.makeText(activity, "logging in...", Toast.LENGTH_SHORT).show();	
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onSuccessHandler(java.lang.Object)
	 */
	@Override
	protected void onSuccessHandler(Object object) {
		JSONObject response = (JSONObject) object;
		try {
			JSONArray error = response.getJSONObject("json").getJSONArray("errors");

			if (error.length() == 0) {
				JSONObject data = response.getJSONObject("json").getJSONObject("data");
				String cookie = data.getString("cookie");
				String modhash = data.getString("modhash");
				RedditClient.storeSession(cookie, modhash, activity);
				SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("my_reddit_session", cookie);
				editor.putString("my_modhash", modhash);
				editor.commit();
				this.isLoggedIn = true;
				Toast.makeText(activity, "logged in!", Toast.LENGTH_SHORT).show();
				activity.refresh();
			} else {
				Toast.makeText(
						activity, 
						"login failed! reason: " + error.getJSONArray(0).getString(1), 
						Toast.LENGTH_LONG
						).show();
			}

		} catch (JSONException e) {
			Toast.makeText(activity, "login failed for unknown reasons!", Toast.LENGTH_LONG).show();
		}
		
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onFailureHandler(java.lang.Throwable)
	 */
	@Override
	protected void onFailureHandler(Throwable e) {
		Toast.makeText(activity, "login failed! reason: " + e.getMessage(), Toast.LENGTH_LONG).show();
		
	}
}
