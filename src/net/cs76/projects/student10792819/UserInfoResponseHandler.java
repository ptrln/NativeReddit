/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This response handler handles the response returned by the http request triggered by a
 * call to the RedditClient.getMe method. 
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import net.cs76.projects.student10792819.model.RedditUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

/**
 * The Class UserInfoResponseHandler.
 */
public class UserInfoResponseHandler extends TimeoutListableResponseHandler {

	/**
	 * Instantiates a new user info response handler.
	 *
	 * @param c the context
	 * @param lv the listview in the UserInfoActivity
	 * @param adapter the adapter
	 * @param loadingAdapter the loading adapter
	 * @param failureAdapter the failure adapter
	 */
	public UserInfoResponseHandler(Context c, ListView lv, UserInfoAdapter adapter,
			LoadingAdapter loadingAdapter, FailureAdapter failureAdapter) {
		super(c, lv, adapter, loadingAdapter, failureAdapter);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#afterLoad(java.lang.Object)
	 */
	@Override
	protected void onSuccessHandler(Object object) {
		JSONObject response = (JSONObject) object;
		Log.w("me.json", response + "");
		try {
			// parse JSON response and create RedditUserInfo object
			JSONObject o = response.getJSONObject("data");
			RedditUserInfo info = RedditClient.createInfoFromJSON(o);
			
			// update adapter data
			((UserInfoAdapter) contentAdapter).setUserInfo(info);
			listView.setAdapter(contentAdapter);
		} catch (JSONException e) {
			onFailure(new Throwable("parse data from reddit failed"));
		}
	}
}
