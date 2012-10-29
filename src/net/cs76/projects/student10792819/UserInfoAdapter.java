/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This adapter displays the data in RedditUserInfo objects in a list view.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditUserInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The Class UserInfoAdapter.
 */
public class UserInfoAdapter extends BaseAdapter {
			
	/** The views. */
	private final TextView[] views = new TextView[8];

	/**
	 * Instantiates a new user info adapter.
	 *
	 * @param c the context
	 */
	public UserInfoAdapter(Context c) {		
		for (int i = 0; i < views.length; i++)
			views[i] = new TextView(c);
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
		return views[position];
	}
	
	/**
	 * Sets the user info.
	 *
	 * @param info the new user info
	 */
	public void setUserInfo(RedditUserInfo info) {
		views[0].setText("username: " + info.getName());
		views[1].setText("account created on: " + info.getCreated());
		views[2].setText("link karma: " + info.getLinkKarma());
		views[3].setText("comment karma: " + info.getCommentKarma());
		views[4].setText("gold member: " + info.isGold());
		views[5].setText("moderator: " + info.isMod());
		views[6].setText("has mail: " + info.hasMail());
		views[7].setText("has mod mail: " + info.isHasModMail());
		this.notifyDataSetChanged();
	}
}
