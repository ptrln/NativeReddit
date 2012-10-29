/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * The vote response handler handles the response after calling RedditClient.vote method.
 * Does nothing normally, displays a Toast message on errors, or if reddit returns some sort
 * of error message.
 */
package net.cs76.projects.student10792819;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

/**
 * The Class VoteResponseHandler.
 */
public class VoteResponseHandler extends TimeoutResponseHandler {

	/**
	 * Instantiates a new vote response handler.
	 *
	 * @param c the context
	 */
	public VoteResponseHandler(Context c) {
		super(c);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onFailureHandler(java.lang.Throwable)
	 */
	@Override
	protected void onFailureHandler(Throwable e) {
		// make toast message on error
		Toast.makeText(myContext, "vote failed. reason: " + e.getMessage(), Toast.LENGTH_SHORT);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onSuccessHandler(java.lang.Object)
	 */
	@Override
	protected void onSuccessHandler(Object object) {
		JSONObject response = (JSONObject) object;
		
		if (response.length() > 0)
			Toast.makeText(myContext, "vote failed. please login to do that", Toast.LENGTH_SHORT);
	}
}
