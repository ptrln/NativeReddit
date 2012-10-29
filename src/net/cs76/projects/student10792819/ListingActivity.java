/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the activity used to display a listing of all of the stories on a subreddit page. It
 * is the main activity used in NativeReddit. It also has a menu that allows changing subreddits,
 * user login/logout, and user info display.
 * 
 * It uses the ListingAdapter to display the story listing in a list view
 * 
 * Menu icons from menu icons by http://glyphish.com
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import net.cs76.projects.student10792819.model.RedditListable;
import net.cs76.projects.student10792819.model.RedditStory;
import net.cs76.projects.student10792819.model.RedditVote;
import net.cs76.projects.student10792819.model.RedditListable.Type;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The Class ListingActivity.
 */
public class ListingActivity extends RequireInternetActivity implements OnItemClickListener {

	/** The adapter. */
	private ListingAdapter adapter = null;

	/** The dialog. */
	private Dialog dialog = null;
	
	/** The listing handler. */
	private ListingResponseHandler listingHandler = null;
	
	/** The login handler. */
	private LoginResponseHandler loginHandler = null;

	/** The subreddit. */
	private String subreddit = "";
	
	/** The Constant INPUT_PATTERN. */
	private static final String INPUT_PATTERN = "[a-zA-Z0-9-_/]+";
	
	/** The restored data. */
	private RedditListable[] restoredData = null;
	
	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listing);
		
		Object[] data = (Object[]) getLastNonConfigurationInstance();
		
		if (data != null && data.length == 2) {
			restoredData = (RedditListable[]) data[0];
			if (data[1] != null)
				this.subreddit = (String) data[1];
		}
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#onResume()
	 */
	public void onResume() {
		super.onResume();
		
		if (super.hasConnectivity) {
			if (dialog == null)
				dialog = new Dialog(this);
			
			if (loginHandler == null)
				loginHandler = new LoginResponseHandler(this);
			
			if (super.justGotConnectivity || adapter == null || listingHandler == null) {
				adapter = new ListingAdapter(this);
				
				ListView lv = (ListView) this.findViewById(R.id.story_listing);
				lv.setOnItemClickListener(this);
				
				listingHandler = new ListingResponseHandler(
						this, 
						lv, 
						adapter, 
						new LoadingAdapter(this, "reddit"), 
						new FailureAdapter(this));
				
				if (restoredData == null) {
					this.changeSubReddit(this.subreddit);
				} else {
					adapter.updateListing(restoredData);
					this.setTitle(("NativeReddit/" + subreddit));
					lv.setAdapter(adapter);
					restoredData = null;
				}
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#noNetworkHandler()
	 */
	protected void noNetworkHandler() {
		FailureAdapter failAdapt = new FailureAdapter(this);
		failAdapt.updateError("there is no internet connectivity");
		ListView lv = (ListView) this.findViewById(R.id.story_listing);
		lv.setAdapter(failAdapt);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!super.hasConnectivity)
			return false;
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (loginHandler.loggedIn()) {
			menu.findItem(R.id.menu_login).setEnabled(false);
			menu.findItem(R.id.menu_logout).setEnabled(true);
			menu.findItem(R.id.menu_me).setEnabled(true);
		} else {
			menu.findItem(R.id.menu_login).setEnabled(true);
			menu.findItem(R.id.menu_logout).setEnabled(false);
			menu.findItem(R.id.menu_me).setEnabled(false);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			changeSubReddit("");
			break;
		case R.id.menu_subreddit:
			showSubredditDialog();
			break;
		case R.id.menu_login:
			if (loginHandler.currentlyLoading())
				Toast.makeText(
						this, 
						"currently trying to login. please wait for current request to finish", 
						Toast.LENGTH_SHORT
						).show();
			else
				showLoginDialog();
			break;
		case R.id.menu_logout:
			loginHandler.logout();
			break;
		case R.id.menu_me:
			Intent i = new Intent(this, UserInfoActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}

	/**
	 * Show login dialog.
	 */
	private void showLoginDialog() {
		showDialog(R.layout.login_dialogue, "user login", R.id.login_button, new OnClickListener() {
			public void onClick(View v) {
				EditText name = (EditText) dialog.findViewById(R.id.login_username);
				EditText pass = (EditText) dialog.findViewById(R.id.login_password);
				userLogin(name.getText().toString(), pass.getText().toString());
				dialog.dismiss();
			}
		});
	}

	/**
	 * Show subreddit dialog.
	 */
	private void showSubredditDialog() {
		final Context c = this;
		showDialog(R.layout.subreddit_dialogue, "change subreddit", R.id.subreddit_button, new OnClickListener() {
			public void onClick(View v) {
				EditText e = (EditText) dialog.findViewById(R.id.subreddit_name);
				String subreddit = e.getText().toString();
				if (! validInput(subreddit))
					Toast.makeText(c, "invalid input", Toast.LENGTH_LONG).show();
				else
					changeSubReddit(subreddit);
				dialog.dismiss();
			}
		});
	}

	/**
	 * Show dialog. This is a helper function to help display a dialog and set its listeners.
	 *
	 * @param layout the layout
	 * @param string the string
	 * @param submit the submit
	 * @param onClickListener the on click listener
	 */
	private void showDialog(int layout, String string, int submit, OnClickListener onClickListener) {
		dialog.setContentView(layout);
		dialog.setTitle(string);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		Button button = (Button) dialog.findViewById(submit);
		button.setOnClickListener(onClickListener);
		dialog.show();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	public Object onRetainNonConfigurationInstance() {
		Object[] data = new Object[2];
		if (!listingHandler.currentlyLoading() && adapter != null) {
			data[0] = adapter.getData();
		} 
		data[1] = this.subreddit;
		return data;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		RedditListable listable = (RedditListable) adapter.getItem(position);

		if (listable == null)
			return;
		
		switch (listable.getType()) {
		case AFTER:
		case BEFORE:
			changeSubRedditPage(this.subreddit, listable.getType(), listable.getID());
		default:
			// do nothing as story clicks are handled in separate handlers depending on view clicked
		}
	}

	/**
	 * User login. The user input is validated using validInput.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public void userLogin(String username, String password) {
		if (username.trim().length() == 0 || password.trim().length() == 0)
			return;
		
		if (! validInput(username) || ! validInput(password)) {
			Toast.makeText(this, "invalid inputs", Toast.LENGTH_LONG).show();
			return;
		}
		
		RedditClient.login(username, password, this.loginHandler);
	}

	/**
	 * Change sub reddit. The user input is validated using validInput. The subreddit cannot be changed
	 * while one is loading.
	 *
	 * @param subreddit the subreddit
	 */
	public void changeSubReddit(String subreddit) {
		if (listingHandler.currentlyLoading()) {
			Toast.makeText(
					this, 
					"currently loading a subreddit. please wait for current request to finish", 
					Toast.LENGTH_SHORT
					).show();
		} else {
			this.subreddit = subreddit;
			this.setTitle(("NativeReddit/" + subreddit));
			RedditClient.getListing(this, subreddit, this.listingHandler);
		}
	}

	/**
	 * Change sub reddit page.
	 *
	 * @param subreddit the subreddit
	 * @param type the type
	 * @param id the id
	 */
	public void changeSubRedditPage(String subreddit, Type type, String id) {
		RedditClient.getListingRelative(this, subreddit, type.toString(), id, this.listingHandler);
	}
	
	/**
	 * On vote.
	 *
	 * @param v the vote
	 * @param position the position
	 */
	void onVote(RedditVote v, int position) {
		if (!loginHandler.loggedIn()) {
			Toast.makeText(this, "you'll need to login to do that", Toast.LENGTH_SHORT).show();
			return;
		}
		
		RedditStory s = (RedditStory) adapter.getItem(position);
		
		if (s.getVote() == v) {
			s.setVote(RedditVote.NA);
		} else {
			s.setVote(v);
		}
		adapter.notifyDataSetChanged();
		
		RedditClient.vote(s.getVote(), s.getID(), null);
	}
	
	/**
	 * Refreshes the current listing.
	 */
	void refresh() {
		RedditClient.cancelRequests(this);
		RedditClient.getListing(this, subreddit, this.listingHandler);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	public void finish() {
		if (!super.hasConnectivity)
			super.finish();
		else
			changeSubReddit("");				// back goes back to front page instead of exiting app
	}
	
	/**
	 * Valid input.
	 *
	 * @param s the user input string to validate
	 * @return true, if successful
	 */
	private static boolean validInput(String s) {
		if (s.matches(INPUT_PATTERN))
			return true;
		return false;
	}
	
	/**
	 * On title click opens the story's link in a StoryViewActivity.
	 *
	 * @param position the position clicked
	 */
	void onTitleClick(int position) {
		// some links are to self (reddit), needs to check that
		RedditStory story = (RedditStory) adapter.getItem(position);
		if (story.getDomain().equals("self." + story.getSubreddit())) {
			onCommentClick(position);
			return;
		}
		
		Intent i = new Intent(this, StoryViewActivity.class);
		i.putExtra("url", ((RedditStory) adapter.getItem(position)).getUrl());
		startActivity(i);
	}
	
	/**
	 * On comment click opens the story and link in a StoryCommentActivity.
	 *
	 * @param position the position clicked
	 */
	void onCommentClick(int position) {
		Intent i = new Intent(this, StoryCommentActivity.class);
		i.putExtra("storyLink", ((RedditStory) adapter.getItem(position)).getLink());
		startActivity(i);
	}
}