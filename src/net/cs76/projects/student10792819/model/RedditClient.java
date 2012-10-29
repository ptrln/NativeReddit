/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * The RedditClient is the object used by all activities in NativeReddit to make http requests. All of the methods
 * in this class are static, so there is no need to create an instance of this class.
 * 
 * Uses this library:
 * Android Asynchronous Http Client from http://loopj.com/android-async-http/
 */
package net.cs76.projects.student10792819.model;

import java.util.ArrayList;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * The Class RedditClient.
 */
public class RedditClient {
	
	/** The Constant BASE_URL. */
	private static final String BASE_URL = "http://www.reddit.com/";
	
	/** The Constant COMMENT_LOAD_LIMIT. */
	private static final int COMMENT_LOAD_LIMIT = 100;
	
	/** The client. */
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	/** The cookie store. */
	private static PersistentCookieStore cookieStore = null;
	
	/** The mod hash. */
	private static String modHash = "";
	
	/**
	 * Gets the listing in a subreddit.
	 *
	 * @param c the context
	 * @param subreddit the subreddit
	 * @param responseHandler the response handler
	 * @return the listing
	 */
	public static void getListing(Context c, String subreddit, AsyncHttpResponseHandler responseHandler) {
		if (subreddit.length() > 0 && subreddit.charAt(0) == '/')
			subreddit = subreddit.substring(1);
		get(c, subreddit + ".json", null, responseHandler);
	}
	
	/**
	 * Gets the listing before/after a particular story.
	 *
	 * @param c the context
	 * @param subreddit the subreddit
	 * @param relative the relative, eg before or after
	 * @param anchorID the anchor id
	 * @param responseHandler the response handler
	 * @return the listing relative
	 */
	public static void getListingRelative(Context c, String subreddit, 
			String relative, String anchorID, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(relative, anchorID);
		get(c, subreddit + ".json", params, responseHandler);
	}
	
	/**
	 * Login.
	 *
	 * @param username the username
	 * @param password the password
	 * @param responseHandler the response handler
	 */
	public static void login(String username, String password, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("user", username);
		params.put("passwd", password);
		params.put("api_type", "json");
		// login requests are not cancellable using cancel requests
		client.post(null, "https://ssl.reddit.com/api/login/" + username, params, responseHandler);
	}
	
	/**
	 * Cancel requests.
	 *
	 * @param c the context
	 */
	public static void cancelRequests(Context c) {
		client.cancelRequests(c, true);
	}
	
	/**
	 * Clear session.
	 */
	public static void clearSession() {
		modHash = "";
		cookieStore.clear();
	}
	
	/**
	 * Sets the mod hash.
	 *
	 * @param uh the new mod hash
	 */
	public static void setModHash(String uh) {
		modHash = uh;
	}
	
	/**
	 * Gets the story comments.
	 *
	 * @param c the context
	 * @param link the link
	 * @param responseHandler the response handler
	 * @return the story comments
	 */
	public static void getStoryComments(Context c, String link, AsyncHttpResponseHandler responseHandler) {
		if (link.length() > 0 && link.charAt(0) == '/')
			link = link.substring(1);
		// only loads 100 comments
		get(c, link + ".json?limit=" + COMMENT_LOAD_LIMIT, null, responseHandler);
	}
	
	/**
	 * Vote.
	 *
	 * @param dir the vote direction
	 * @param id the id
	 * @param responseHandler the response handler
	 */
	public static void vote(RedditVote dir, String id, AsyncHttpResponseHandler responseHandler) {		
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("dir", dir.toString());
		params.put("uh", modHash);
		
		// vote posts null context, so requests are not cancellable
		post(null, "api/vote", params, responseHandler);
	}
	
	/**
	 * Gets the my info page.
	 *
	 * @param c the context
	 * @param responseHandler the response handler
	 * @return the me
	 */
	public static void getMe(Context c, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uh", modHash);
		get(c, "api/me.json", null, responseHandler);
	}
	
	/**
	 * Store session stores a user login session in this reddit client.
	 *
	 * @param cookie the cookie
	 * @param modhash the modhash
	 * @param c the c
	 */
	public static void storeSession(String cookie, String modhash, Context c) {
		if (cookieStore == null) {
			cookieStore = new PersistentCookieStore(c);
			client.setCookieStore(cookieStore);
		}
		clearSession();
		
		BasicClientCookie newCookie = new BasicClientCookie("reddit_session", cookie);
		newCookie.setVersion(1);
		newCookie.setSecure(false);
		newCookie.setDomain(".reddit.com");
		newCookie.setPath("/");
		modHash = modhash;
		cookieStore.addCookie(newCookie);
	}
	
	/**
	 * Gets request.
	 *
	 * @param c the context
	 * @param url the url
	 * @param params the params
	 * @param responseHandler the response handler
	 */
	private static void get(Context c, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(c, getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * Post request.
	 *
	 * @param c the context
	 * @param url the url
	 * @param params the params
	 * @param responseHandler the response handler
	 */
	private static void post(Context c, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(c, getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * Gets the absolute url.
	 *
	 * @param relativeUrl the relative url
	 * @return the absolute url
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}	
	
	/**
	 * Creates the info from json.
	 *
	 * @param o the o
	 * @return the reddit user info
	 * @throws JSONException the jSON exception
	 */
	public static RedditUserInfo createInfoFromJSON(JSONObject o) throws JSONException {
		return new RedditUserInfo(
				o.getBoolean("has_mail"), 
				o.getString("name"),
				o.getLong(("created")),
				o.getInt("link_karma"),
				o.getInt("comment_karma"),
				o.getBoolean("is_gold"),
				o.getBoolean("is_mod"),
				o.getString("id"),
				o.getBoolean("has_mod_mail"));			
	}
	
	/**
	 * Creates the story from json.
	 *
	 * @param json the json
	 * @return the reddit story
	 * @throws JSONException the jSON exception
	 */
	public static RedditStory createStoryFromJSON(JSONObject json) throws JSONException {
		JSONObject data = json.getJSONObject("data");
		return new RedditStory(
				data.getString("title"), 
				data.getString("author"), 
				data.getString("selftext"),
				data.getString("url"), 
				data.getString("thumbnail"), 
				data.getString("permalink"), 
				data.getString("domain"), 
				data.getString("subreddit"), 
				data.getInt("ups"), 
				data.getInt("downs"), 
				data.getInt("num_comments"), 
				data.getBoolean("over_18"),
				data.getString("likes"),
				data.getLong("created"),
				data.getString("name"));
	}

	/**
	 * Creates the comment listing from json.
	 *
	 * @param json the json
	 * @return the reddit comment listing
	 * @throws JSONException the jSON exception
	 */
	public static RedditCommentListing createCommentListingFromJSON(JSONArray json) throws JSONException {
		JSONObject data = ((JSONObject) json.get(0)).getJSONObject("data");

		RedditStory story = null;
		if (((JSONObject) data.getJSONArray("children").get(0)).getString("kind").equals("t3"))
			story = RedditClient.createStoryFromJSON((JSONObject) data.getJSONArray("children").get(0));

		JSONArray comments = ((JSONObject) json.get(1)).getJSONObject("data").getJSONArray("children");

		return new RedditCommentListing(story, createCommentFromJSON(comments));
	
	}
	
	/**
	 * Creates the comment from json.
	 *
	 * @param json the json
	 * @return the reddit comment[]
	 * @throws JSONException the jSON exception
	 */
	public static RedditComment[] createCommentFromJSON(JSONArray json) throws JSONException {
		ArrayList<RedditComment> comments = new ArrayList<RedditComment>();
		JSONObject data;
		RedditComment rcArr[] = new RedditComment[0];
		
		for (int i = 0, n = json.length(); i < n; i++) {
				if (!json.getJSONObject(i).getString("kind").equals("t1"))
					continue;
				
				data = json.getJSONObject(i).getJSONObject("data");
				
				RedditComment[] replies;
				
				JSONObject repliesJSON = data.optJSONObject("replies");
				
				if (repliesJSON == null || repliesJSON.length() == 0)
					replies = new RedditComment[0];
				else
					replies = createCommentFromJSON(repliesJSON.getJSONObject("data").getJSONArray("children"));
				
				comments.add(i, new RedditComment(
						data.getString("body"),
						data.getString("author"),
						data.getLong("created"),
						replies
						));
		}
				
		return comments.toArray(rcArr);
	}
	
	/**
	 * Creates the listable from json.
	 *
	 * @param o the o
	 * @return the reddit listable[]
	 * @throws JSONException the jSON exception
	 */
	public static RedditListable[] createListableFromJSON(JSONObject o) throws JSONException {
		// parse the JSON and create the RedditListable array. 
		JSONArray storyListing = o.getJSONArray("children");
		RedditListable [] stories;
		int storyCount = storyListing.length(), listingCount = storyListing.length();

		String beforeID = null, afterID = null;

		if (! (beforeID = o.getString("before")).equals("null")) {		// there is a before
			listingCount++;
		}

		if (! (afterID = o.getString("after")).equals("null")) {		// there is an after
			listingCount++;
		}

		stories = new RedditListable[listingCount];

		int i = 0;

		if (! beforeID.equals("null")) {
			stories[i] = new RedditListable(RedditListable.Type.BEFORE, beforeID);
			i++;
		}
		if (! afterID.equals("null") ) {
			stories[listingCount - 1] = new RedditListable(RedditListable.Type.AFTER, afterID);
		}
		for (int j = 0; j < storyCount; j++) {
			o = (JSONObject) storyListing.get(j);
			stories[i + j] = RedditClient.createStoryFromJSON(o);
		}
		
		return stories;
	}
}
