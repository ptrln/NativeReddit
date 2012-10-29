/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the base response handler for all response handlers that wish to have
 * timeout mechanism and loading/failure display built in. This class assumes that activity
 * is using a list view as their display, and swaps out different adapters for the list
 * view depending on the state of the request.
 */
package net.cs76.projects.student10792819;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * The Class TimeoutListableResponseHandler.
 */
abstract class TimeoutListableResponseHandler extends TimeoutResponseHandler {
	
	/** The content adapter. */
	protected final BaseAdapter contentAdapter;

	/** The loading adapter. */
	protected final BaseAdapter loadingAdapter;

	/** The failure adapter. */
	protected final FailureAdapter failureAdapter;

	/** The list view. */
	protected final ListView listView;

	/**
	 * Instantiates a new timeout listable response handler.
	 *
	 * @param c the context
	 * @param lv the listview
	 * @param listing the listing
	 * @param loading the loading
	 * @param fail the fail
	 */
	protected TimeoutListableResponseHandler (Context c, ListView lv, 
			BaseAdapter listing, BaseAdapter loading, FailureAdapter fail) {
		super(c);
		this.listView = lv;
		this.contentAdapter = listing;
		this.loadingAdapter = loading;
		this.failureAdapter = fail;
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onStartHandler()
	 */
	@Override
	protected void onStartHandler() {
		if (loadingAdapter != null) {
			listView.setAdapter(loadingAdapter);
		}
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.TimeoutResponseHandler#onFailureHandler(java.lang.Throwable)
	 */
	@Override
	protected void onFailureHandler(Throwable e) {
		if (failureAdapter != null) {
			failureAdapter.updateError(e.getMessage());
			listView.setAdapter(failureAdapter);
		}
	}
}
