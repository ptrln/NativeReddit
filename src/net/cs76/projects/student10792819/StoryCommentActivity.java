/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the activity that displays the story and its comments. It uses the StoryCommentAdapter to
 * display the comments.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import net.cs76.projects.student10792819.model.RedditCommentListing;
import android.os.Bundle;
import android.widget.ListView;

/**
 * The Class StoryCommentActivity.
 */
public class StoryCommentActivity extends RequireInternetActivity {	
	
	/** The permanent link of the story + comments. */
	private String permalink;
	
	/** The adapter. */
	private StoryCommentAdapter adapter = null;
	
	/** The comment handler. */
	private StoryCommentResponseHandler commentHandler = null;
	
	/** The restored data. */
	private RedditCommentListing restoredData = null;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_comment_listing);
        permalink = getIntent().getStringExtra("storyLink");    
        
		restoredData = (RedditCommentListing) getLastNonConfigurationInstance();
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#onResume()
	 */
	public void onResume() {
		super.onResume();
		if (super.hasConnectivity) {
			if (adapter == null || commentHandler == null) {
				adapter = new StoryCommentAdapter(this);
				ListView lv = (ListView) this.findViewById(R.id.story_comment_listing);
				lv.setClickable(false);

				commentHandler = new StoryCommentResponseHandler(
						this, lv, adapter, new LoadingAdapter(this, "comments"), new FailureAdapter(this)
						);

				if (restoredData == null)
					RedditClient.getStoryComments(this, permalink, commentHandler);
				else {
					adapter.updateListing(restoredData);
					lv.setAdapter(adapter);
				}
			} else if (super.justGotConnectivity) {
				RedditClient.getStoryComments(this, permalink, commentHandler);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	public Object onRetainNonConfigurationInstance() {
		if (adapter != null)
			return adapter.getData();
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#noNetworkHandler()
	 */
	protected void noNetworkHandler() {
		FailureAdapter failAdapt = new FailureAdapter(this);
		failAdapt.updateError("there is no internet connectivity");
        ListView lv = (ListView) this.findViewById(R.id.story_comment_listing);
		lv.setAdapter(failAdapt);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	public void onStop() {
		super.onStop();
		RedditClient.cancelRequests(this);
	}
}
