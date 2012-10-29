/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This class represents a reddit story. 
 */
package net.cs76.projects.student10792819.model;

import java.util.Date;

/**
 * The Class RedditStory.
 */
public class RedditStory extends RedditListable {
	
	/** The title. */
	private final String title;
	
	/** The link. */
	private final String link;

	/** The number of up votes. */
	private final int ups;
	
	/** The number of down votes. */
	private final int downs;
	
	/** The author. */
	private final String author;
	
	/** The number of comments. */
	private final int numComments;
	
	/** The thumbnail. */
	private final String thumbnail;
	
	/** The url. */
	private final String url;
	
	/** The domain. */
	private final String domain;
	
	/** The subreddit. */
	private final String subreddit;
	
	/** The time created. */
	private final String timeCreated;
	
	/** The text. */
	private final String text;
	
	/** The dir. */
	private RedditVote dir;
	
	/** The nsfw. */
	private final boolean nsfw;
	
	/**
	 * Instantiates a new reddit story.
	 *
	 * @param title the title
	 * @param author the author
	 * @param text the text
	 * @param url the url
	 * @param thumbnail the thumbnail
	 * @param permalink the permalink
	 * @param domain the domain
	 * @param sr the sr
	 * @param up the up
	 * @param down the down
	 * @param ncom the ncom
	 * @param nsfw the nsfw
	 * @param likes the likes
	 * @param time the time
	 * @param id the id
	 */
	RedditStory(String title, String author, String text, String url, String thumbnail, String permalink, 
			String domain, String sr, int up, int down, int ncom, boolean nsfw, String likes, long time, String id) {
		super(RedditListable.Type.STORY, id);
		this.title = title;
		this.url = url;
		this.link = permalink;
		this.text = text;
		this.domain = domain;
		this.ups = up;
		this.downs = down;
		this.author = author;
		this.numComments = ncom;
		this.subreddit = sr;
		this.thumbnail = thumbnail;
		this.timeCreated = new Date(time * 1000).toLocaleString();
		this.nsfw = nsfw;
		
		if (likes.equals("true"))
			this.dir = RedditVote.UP;
		else if (likes.equals("false"))
			this.dir = RedditVote.DOWN;
		else
			this.dir = RedditVote.NA;
		
	}
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public int getScore() {
		return ups - downs + Integer.parseInt(dir.toString());
	}
	
	/**
	 * Sets the vote.
	 *
	 * @param v the new vote
	 */
	public void setVote(RedditVote v) {
		this.dir = v;
	}
	
	/**
	 * Gets the vote.
	 *
	 * @return the vote
	 */
	public RedditVote getVote() {
		return this.dir;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the num comments.
	 *
	 * @return the num comments
	 */
	public int getNumComments() {
		return numComments;
	}

	/**
	 * Gets the thumbnail.
	 *
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the subreddit.
	 *
	 * @return the subreddit
	 */
	public String getSubreddit() {
		return subreddit;
	}

	/**
	 * Gets the time created.
	 *
	 * @return the time created
	 */
	public String getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Checks if is nsfw.
	 *
	 * @return true, if is nsfw
	 */
	public boolean isNsfw() {
		return nsfw;
	}
}
