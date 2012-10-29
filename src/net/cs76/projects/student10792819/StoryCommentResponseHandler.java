/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * 
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import net.cs76.projects.student10792819.model.RedditCommentListing;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.widget.ListView;

/**
 * The Class StoryCommentResponseHandler.
 */
public class StoryCommentResponseHandler extends TimeoutListableResponseHandler {
	
	/**
	 * Instantiates a new story comment response handler.
	 *
	 * @param c the context
	 * @param lv the listview
	 * @param adapter the adapter
	 * @param loadAdapt the load adapt
	 * @param failAdapt the fail adapt
	 */
	public StoryCommentResponseHandler(Context c, ListView lv, 
			StoryCommentAdapter adapter, LoadingAdapter loadAdapt, FailureAdapter failAdapt) {
		super(c, lv, adapter, loadAdapt, failAdapt);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onSuccessHandler(java.lang.Object)
	 */
	@Override
	protected void onSuccessHandler(Object object) {
		JSONArray response = (JSONArray) object;
		try {			
			RedditCommentListing listing = RedditClient.createCommentListingFromJSON(response);
			((StoryCommentAdapter) contentAdapter).updateListing(listing);
			listView.setAdapter(contentAdapter);
		} catch (JSONException e) {
			onFailure(new Throwable("parse data from reddit failed"));
		}
	}
}
