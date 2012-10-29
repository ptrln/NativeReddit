/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This class represents a reddit comment. The comment themselves can have further comments, so
 * this object also contains an array of RedditComment objects.
 */
package net.cs76.projects.student10792819.model;

import java.util.Date;

/**
 * The Class RedditComment.
 */
public class RedditComment extends RedditListable {

	/** The comments. */
	private final RedditComment[] comments;
	
	/** The body. */
	private final String body;
	
	/** The author. */
	private final String author;

	/** The time created. */
	private final String timeCreated;
	
	/**
	 * Instantiates a new reddit comment.
	 *
	 * @param body the body
	 * @param author the author
	 * @param time the time
	 * @param comments the comments
	 */
	RedditComment(String body, String author, long time, RedditComment[] comments) {
		super(RedditListable.Type.COMMENT, "");
		this.body = body;
		this.author = author;
		this.timeCreated = new Date(time * 1000).toLocaleString();
		this.comments = comments;
	}

	/**
	 * Gets the comments.
	 *
	 * @return the comments
	 */
	public RedditComment[] getComments() {
		return comments;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return body;
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
	 * Gets the time created.
	 *
	 * @return the time created
	 */
	public String getTimeCreated() {
		return timeCreated;
	}

}
