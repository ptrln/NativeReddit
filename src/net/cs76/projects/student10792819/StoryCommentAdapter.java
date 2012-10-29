/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the adapter that allows the story and its comments to be displayed in a list view. It is used by the
 * StoryCommentActivity.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditComment;
import net.cs76.projects.student10792819.model.RedditCommentListing;
import net.cs76.projects.student10792819.model.RedditStory;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The Class StoryCommentAdapter.
 */
public class StoryCommentAdapter extends BaseAdapter {

	/** The my context. */
	private Context myContext;
	
	/** The listing. */
	private RedditCommentListing listing;
		
	/** The inflater. */
	private static LayoutInflater inflater;

	/**
	 * Instantiates a new story comment adapter.
	 *
	 * @param c the context
	 */
	public StoryCommentAdapter(Context c) {
		this.myContext = c;
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (listing == null)
			return 0;
		return listing.size();
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
		switch(position) {
		case 0:						// this is the story
			RelativeLayout v;

			if (listing.getStory() == null) {
				TextView tv = new TextView(myContext);
				tv.setText("no story to view here!");
				return tv;
			}
			
			if (convertView instanceof RelativeLayout)
				v = (RelativeLayout) convertView;
			else
				v = (RelativeLayout) inflater.inflate(R.layout.comment_story_item, null);
			
			((TextView) v.findViewById(R.id.story_title)).setText(listing.getStory().getTitle());
			((TextView) v.findViewById(R.id.story_text)).setText(listing.getStory().getText());
			((TextView) v.findViewById(R.id.submit_time)).setText(
					"submitted at " + listing.getStory().getTimeCreated() + " by " + 
					listing.getStory().getAuthor() + " to " + listing.getStory().getSubreddit()
					);
			String nsfw = (listing.getStory().isNsfw() ? " NSFW" : "");
			((TextView) v.findViewById(R.id.listing_domain)).setText("(" + listing.getStory().getDomain() + ")" + nsfw);
			
			return v;
			
		case 1:						// this is the line which says "comments:"
			TextView tv;
			if (convertView instanceof TextView)
				tv = (TextView) convertView;
			else
				tv = new TextView(myContext);
			tv.setText("comments:");
			tv.setBackgroundColor(Color.GRAY);
			tv.setTextColor(Color.WHITE);
			return tv;
			
		default:
			if (listing.getComments() == null)
				return null;
			else
				return displayComments(listing.getComments()[position - 2], convertView, 0);
		}
	}
	
	/**
	 * Display comments creates the LinearLayout which contains the views used to display a comment.
	 * This function is designed to be capable to displaying nested comments using the left padding to
	 * indent the nested comments. Nested comments are not currently supported.
	 * 
	 * @param rc the reddit comment object to display
	 * @param recycled the recycled view
	 * @param leftPadding the left padding
	 * @return the linear layout
	 */
	private LinearLayout displayComments(RedditComment rc, View recycled, int leftPadding) {
		LinearLayout l;
		if (recycled instanceof LinearLayout)
			l = (LinearLayout) recycled;
		else
			l = (LinearLayout) inflater.inflate(R.layout.comment_item, null);
			
		((TextView) l.findViewById(R.id.comment_body)).setText(rc.getBody());
		((TextView) l.findViewById(R.id.submit_time)).setText(
				"submitted by " + rc.getAuthor() + " at " + rc.getTimeCreated());
		
		l.setPadding(leftPadding, 0, 0, 0);
		
		return l;
	}

	/**
	 * Update listing allows the listing used by the adapter to be updates.
	 *
	 * @param listing the listing
	 */
	public void updateListing(RedditCommentListing listing) {
		this.listing = listing;
		this.notifyDataSetChanged();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return listing;
	}
	
	/**
	 * Gets the story.
	 *
	 * @return the story
	 */
	public RedditStory getStory() {
		return listing.getStory();
	}

}
