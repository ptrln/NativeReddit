/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This class contains the user info loaded from reddit.
 */
package net.cs76.projects.student10792819.model;

import java.util.Date;

/**
 * The Class RedditUserInfo.
 */
public class RedditUserInfo {

	/** The has mail. */
	private final boolean hasMail;
	
	/** The name. */
	private final String name;
	
	/** The created. */
	private final String created;
		
	/** The link karma. */
	private final int linkKarma;
	
	/** The comment karma. */
	private final int commentKarma;
	
	/** The is gold. */
	private final boolean isGold;
	
	/** The is mod. */
	private final boolean isMod;
	
	/** The id. */
	private final String id;
	
	/** The has mod mail. */
	private final boolean hasModMail;
	
	/**
	 * Instantiates a new reddit user info.
	 *
	 * @param hasMail the boolean has mail
	 * @param name the name
	 * @param time the time created
	 * @param lk the link karma
	 * @param ck the comment karma
	 * @param gold the boolean is gold
	 * @param mod the boolean is mod
	 * @param id the id
	 * @param modMail the boolean has mod mail
	 */
	RedditUserInfo(boolean hasMail, String name, long time, 
			int lk, int ck, boolean gold, boolean mod, String id, boolean modMail) {
		this.hasMail = hasMail;
		this.name = name;
		this.created = new Date(time * 1000).toLocaleString();
		this.linkKarma = lk;
		this.commentKarma = ck;
		this.isGold = gold;
		this.isMod = mod;
		this.id = id;
		this.hasModMail = modMail;
	}

	/**
	 * Checks for mail.
	 *
	 * @return true, if successful
	 */
	public boolean hasMail() {
		return hasMail;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the created.
	 *
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * Gets the link karma.
	 *
	 * @return the link karma
	 */
	public int getLinkKarma() {
		return linkKarma;
	}

	/**
	 * Gets the comment karma.
	 *
	 * @return the comment karma
	 */
	public int getCommentKarma() {
		return commentKarma;
	}

	/**
	 * Checks if is gold.
	 *
	 * @return true, if is gold
	 */
	public boolean isGold() {
		return isGold;
	}

	/**
	 * Checks if is mod.
	 *
	 * @return true, if is mod
	 */
	public boolean isMod() {
		return isMod;
	}

	/**
	 * Checks if is checks for mod mail.
	 *
	 * @return true, if is checks for mod mail
	 */
	public boolean isHasModMail() {
		return hasModMail;
	}

	/**
	 * Gets the iD.
	 *
	 * @return the iD
	 */
	public String getID() {
		return id;
	}
}
