/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This class is responsible for setting up the views of the ListingActivity. It uses
 * the RedditListable objects as data and displays the contents appropriately.
 * 
 * Reddit up/down arrows
 * http://www.gamezworld.djmorley.com/action/reddup.html
 * They have been resized to fit the app
 * 
 * The NFSW and SELF icons are from reddit http://www.reddit.com
 * They have been resized to fit the app
 * 
 * Loading icon here
 * http://android.attemptone.com/general/loading-image/
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditListable;
import net.cs76.projects.student10792819.model.RedditStory;
import net.cs76.projects.student10792819.model.RedditVote;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The Class RedditDisplayAdapter.
 */
public class ListingAdapter extends BaseAdapter {
	
	/** The stories. */
	private RedditListable[] listing = null;
	
	/** The my context. */
	private ListingActivity listingActivity;
	
	/** The list view. */
	private ListView listView;
	
	/** The inflater. */
	private static LayoutInflater inflater;
	
	/** The vote up listener. */
	private VoteListener voteUpListener;
	
	/** The vote down listener. */
	private VoteListener voteDownListener;
	
	/** The title click handler. */
	private OnClickListener titleClick;
	
	/** The comment click handler. */
	private OnClickListener commentClick;
	
	/** The subreddit click handler. */
	private OnClickListener subredditClick;
	
	private Drawable[] thumbnailCache;
	
	/**
	 * Instantiates a new reddit display adapter.
	 *
	 * @param l the listing activity
	 */
	public ListingAdapter(ListingActivity l) {
		listingActivity = l;
		listView = (ListView) (listingActivity.findViewById(R.id.story_listing));
		inflater = (LayoutInflater) l.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// create all on click listeners
		voteUpListener = new VoteListener(RedditVote.UP);
		voteDownListener = new VoteListener(RedditVote.DOWN);
		titleClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				listingActivity.onTitleClick(listView.getPositionForView(v));
			}
		};
		commentClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				listingActivity.onCommentClick(listView.getPositionForView(v));
			}
		};
		final ListingAdapter a = this;
		subredditClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				listingActivity.changeSubReddit("r/" + ((RedditStory) a.getItem(listView.getPositionForView(v))).getSubreddit());
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (listing == null)
			return 0;
		else
			return listing.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (listing == null)
			return null;
		return listing[position];
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
		RedditListable item = listing[position];
		
		// gets item type in order to determine if it's a story listing or before/after
		switch(item.getType()) {		
		case BEFORE:
			TextView tv = new TextView(listingActivity);			// no recycling since these are rare
			tv.setText("Prev Page");
			return tv;
		case AFTER:
			TextView tv2 = new TextView(listingActivity);			// no recycling since these are rare
			tv2.setText("Next Page");
			return tv2;
		case STORY:
		default:
			RedditStory story = (RedditStory) item;
			
			RelativeLayout v = createStoryListingLayout(convertView);
			setStoryText(v, story, position);
			setStoryThumbnail(v, story, position);
			setStoryArrows(v, story);
			return v;
		}
	}
	
	/**
	 * Creates the story listing layout.
	 *
	 * @param recycled the recycled view
	 * @return the relative layout to contain story views
	 */
	private RelativeLayout createStoryListingLayout(View recycled) {
		if (recycled == null || ! (recycled instanceof RelativeLayout)) {
			RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.story_item, null);
			
			// set on click listeners
			((TextView) v.findViewById(R.id.listing_title)).setOnClickListener(titleClick);
			((ImageView) v.findViewById(R.id.listing_image)).setOnClickListener(titleClick);
			((TextView) v.findViewById(R.id.listing_misc)).setOnClickListener(commentClick);
			((TextView) v.findViewById(R.id.submit_time)).setOnClickListener(subredditClick);
			((ImageButton) v.findViewById(R.id.listing_up)).setOnClickListener(voteUpListener);
			((ImageButton) v.findViewById(R.id.listing_down)).setOnClickListener(voteDownListener);
			return v;
		} else {
			return (RelativeLayout) recycled;
		}
	}

	/**
	 * Sets the story up/down arrows appropriately
	 *
	 * @param rl the relative layout
	 * @param story the story
	 */
	private void setStoryArrows(RelativeLayout rl, RedditStory story) {
		// set vote arrow icons
		ImageButton up = (ImageButton) rl.findViewById(R.id.listing_up);
		ImageButton down = (ImageButton) rl.findViewById(R.id.listing_down);

		up.setImageResource(R.drawable.reddit_up_grey);
		down.setImageResource(R.drawable.reddit_down_grey);

		// if user voted on story, set the vote colors appropriately
		if (story.getVote() == RedditVote.UP) {
			up.setImageResource(R.drawable.reddit_up);
		} else if (story.getVote() == RedditVote.DOWN) {
			down.setImageResource(R.drawable.reddit_down);
		}
	}

	/**
	 * Sets the story thumbnail image
	 *
	 * @param rl the relative layout
	 * @param story the story
	 */
	private void setStoryThumbnail(RelativeLayout rl, RedditStory story, int position) {
		ImageView iv = (ImageView) rl.findViewById(R.id.listing_image);
		if (story.getThumbnail().length() != 0) {
			if (thumbnailCache[position] != null) {
				iv.setImageDrawable(thumbnailCache[position]);
			} else if (story.getThumbnail().equalsIgnoreCase("nsfw")) {
				iv.setImageResource(R.drawable.reddit_nsfw);					// use nsfw icon
			} else if (story.getThumbnail().equalsIgnoreCase("self")) {
				iv.setImageResource(R.drawable.reddit_self);					// use self icon
			} else if (story.getThumbnail().equalsIgnoreCase("default")) {
				iv.setImageBitmap(null);
			} else if (story.getThumbnail().contains("http")) {
				Drawable cached = DrawableManager.getCache(story.getThumbnail());
				if (cached == null) {
					iv.setImageResource(R.drawable.loading);						// load thumbnail from reddit
					((AnimationDrawable) iv.getDrawable()).start();
					DrawableManager.fetchDrawableOnThread(story.getThumbnail(), this, thumbnailCache, position);
				} else {
					thumbnailCache[position] = cached;
					iv.setImageDrawable(cached);
				}
			} else {
				Log.w("NativeReddit", "ListingAdapter got unrecognized thumbnail link: " + story.getThumbnail());
				iv.setImageBitmap(null);										// unrecognized link format
			}
		} else {
			iv.setImageBitmap(null);		// no thumbnail
		}
	}

	/**
	 * Sets the story text.
	 *
	 * @param rl the relative layout
	 * @param story the story
	 * @param position the position
	 */
	private void setStoryText(RelativeLayout rl, RedditStory story, int position) {
		// set the view to show contents of the RedditStory
		((TextView) rl.findViewById(R.id.listing_num)).setText(position + 1 + "");
		((TextView) rl.findViewById(R.id.listing_title)).setText(story.getTitle());
		((TextView) rl.findViewById(R.id.listing_misc)).setText(story.getNumComments() + " comments");
		((TextView) rl.findViewById(R.id.listing_karma)).setText("" + story.getScore());
		((TextView) rl.findViewById(R.id.submit_time)).setText(
				"submitted at " + story.getTimeCreated() + " by " + story.getAuthor() + " to " + story.getSubreddit());
		String nsfw = (story.isNsfw() ? " NSFW" : "");
		((TextView) rl.findViewById(R.id.listing_domain)).setText("(" + story.getDomain() + ")" + nsfw);
	}

	/**
	 * Update listing allows the list to be changed. The listing will be updated with the new data.
	 *
	 * @param listing the listing
	 */
	public void updateListing(RedditListable[] listing) {
		this.listing = listing;
		this.thumbnailCache = new Drawable[listing.length];
		this.notifyDataSetChanged();
		listView.setSelectionAfterHeaderView();
	}
	
	/**
	 * The listener interface for receiving vote events.
	 * The class that is interested in processing a vote
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addVoteListener<code> method. When
	 * the vote event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see VoteEvent
	 */
	private class VoteListener implements OnClickListener {

		/** The my dir. */
		private final RedditVote myDir;
		
		/**
		 * Instantiates a new vote listener.
		 *
		 * @param v the vote
		 */
		public VoteListener(RedditVote v) {
			myDir = v;
		}
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			listingActivity.onVote(myDir, listView.getPositionForView(v));
		}
		
	}
	
	/**
	 * Gets the data. This allows the activity to save data on orientation change.
	 *
	 * @return the data
	 */
	public RedditListable[] getData() {
		return listing;
	}
}
