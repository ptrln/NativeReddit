/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This presents a story with comment listing below it. It contains the story object,
 * and an array of comments for that story.
 */
package net.cs76.projects.student10792819.model;

/**
 * The Class RedditCommentListing.
 */
public class RedditCommentListing {

	/** The story. */
	private final RedditStory story;
	
	/** The comments. */
	private final RedditComment[] comments;
	
	/**
	 * Instantiates a new reddit comment listing.
	 *
	 * @param s the s
	 * @param comments the comments
	 */
	RedditCommentListing(RedditStory s, RedditComment[] comments) {
		this.story = s;
		this.comments = comments;
	}
	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		int len = 0;
		
		if (story != null)
			len++;
		
		if (comments != null)
			len += comments.length;
		
		return len;
	}
	
	/**
	 * Gets the story.
	 *
	 * @return the story
	 */
	public RedditStory getStory() {
		return story;
	}
	
	/**
	 * Gets the comments.
	 *
	 * @return the comments
	 */
	public RedditComment[] getComments() {
		return comments;
	}
}
