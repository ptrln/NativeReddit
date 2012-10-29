/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This enum represents the possible vote directions on reddit, and toString
 * returns String match what the reddit API expects for each vote direciton.
 */
package net.cs76.projects.student10792819.model;

/**
 * The Enum RedditVote.
 */
public enum RedditVote {

	/** The UP. */
	UP, 
	/** The DOWN. */
	DOWN, 
	/** The NA. */
	NA;

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		switch (this) {
		case UP:
			return "1";
		case DOWN:
			return "-1";
		default:
			return "0";
		}
	}
}
