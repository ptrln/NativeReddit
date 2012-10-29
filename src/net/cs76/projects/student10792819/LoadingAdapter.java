/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * The LoadingAdapter is used as the GUI display when loading. It is an adapter that should
 * be passed to a ListView.
 * 
 * Loading icon found here
 * http://android.attemptone.com/general/loading-image/
 */
package net.cs76.projects.student10792819;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class LoadingAdapter.
 */
public class LoadingAdapter extends BaseAdapter {

	/** The my context. */
	private final Context myContext;

	/** The views. */
	private final View[] views = new View[2];

	/** The loading icon runner. This is required or sometimes the animation doesn't run.
	 * See http://code.google.com/p/android/issues/detail?id=1818 */
	private Runnable iconRunner = new Runnable() {
		@Override
		public void run() {
			((AnimationDrawable) ((ImageView) views[0]).getDrawable()).start();
		}
	};

	/**
	 * Instantiates a new loading adapter.
	 *
	 * @param c the context
	 * @param item the description of what this loading adapter is loading
	 */
	public LoadingAdapter(Context c, String item) {
		this.myContext = c;

		ImageView iv = new ImageView(myContext);
		iv.setImageResource(R.drawable.loading);
		views[0] = iv;

		TextView tv = new TextView(myContext);
		tv.setText("loading " + item + "...");
		views[1] = tv;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			((ImageView) views[0]).post(iconRunner);
		}

		return views[position];
	}
}
