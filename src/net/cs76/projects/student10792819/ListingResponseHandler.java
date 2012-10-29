/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the response handler used by ListingActivity to handle the data returned by reddit after a call
 * to RedditClient.getListing.
 * 
 * This is a subclass of TimeoutListableResponseHandler, so by default it handles timeouts, and it deals with
 * loading and error using the listable approach, that is it uses the LoadingAdapter and FailureAdapter to
 * show these conditions.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import net.cs76.projects.student10792819.model.RedditListable;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The Class ListingResponseHandler.
 */
public class ListingResponseHandler extends TimeoutListableResponseHandler {
	
	private final OnItemClickListener clickListener;
	
	/**
	 * Instantiates a new listing response handler.
	 *
	 * @param la the listing activity
	 * @param lv the listview
	 * @param listAdapt the list adapter
	 * @param loadAdapt the load adapter
	 * @param failAdapt the fail adapter
	 */
	public ListingResponseHandler(ListingActivity la, ListView lv, 
			ListingAdapter listAdapt, LoadingAdapter loadAdapt, FailureAdapter failAdapt) {
		super(la, lv, listAdapt, loadAdapt, failAdapt);
		this.clickListener = la;
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onSuccessHandler(java.lang.Object)
	 */
	@Override
	protected void onSuccessHandler(Object obj) {
		JSONObject response = (JSONObject) obj;
		try {
			JSONObject o = response.getJSONObject("data");
			RedditListable[] stories = RedditClient.createListableFromJSON(o);
			
			// the data is passed to the listing adapter, and the list view adapter set to the listing adapter
			((ListingAdapter) contentAdapter).updateListing(stories);
			listView.setAdapter(contentAdapter);
			listView.setOnItemClickListener(clickListener);
		} catch (JSONException e) {
			onFailure(new Throwable("parse data from reddit failed"));
		}
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onStartHandler()
	 */
	@Override
	protected void onStartHandler() {
		super.onStartHandler();
		listView.setOnItemClickListener(null);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onFailureHandler(java.lang.Throwable)
	 */
	@Override
	protected void onFailureHandler(Throwable e) {
		super.onFailureHandler(e);
		listView.setOnItemClickListener(null);
	}
}
