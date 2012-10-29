/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 *
 * The FailureAdapter is used as a GUI display whenever an error is encountered. This should be passed
 * to a ListView object.
 * 
 * You broke reddit icon
 * http://tech-wonders.blogspot.com.au/2011/03/you-broke-reddit-daily-screenshot.html
 */
package net.cs76.projects.student10792819;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class RedditDisplayAdapter.
 */
public class FailureAdapter extends BaseAdapter {
		
	/** The my context. */
	private final Context myContext;
	
	/** The views. */
	private final View[] views = new View[3];
	
	/**
	 * Instantiates a new failure adapter.
	 *
	 * @param c the context
	 */
	public FailureAdapter(Context c) {
		this.myContext = c;
		
		ImageView iv = new ImageView(myContext);
		iv.setImageResource(R.drawable.broke_reddit);
		views[0] = iv;
				
		TextView tv = new TextView(myContext);
		tv.setText("oops... something's wrong!");
		views[1] = tv;
		
		TextView tv2 = new TextView(myContext);
		tv2.setText("");
		views[2] = tv2;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return views.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// not used
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// not used
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return views[position];
	}

	/**
	 * Update error. Updates the error text output.
	 *
	 * @param s the string of the error
	 */
	public void updateError(String s) {
		TextView tv = (TextView) views[2];
		tv.setText(s);
		this.notifyDataSetChanged();
	}
}
