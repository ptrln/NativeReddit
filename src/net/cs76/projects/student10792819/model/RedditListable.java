/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the base class of all listable objects used in reddit. 
 */
package net.cs76.projects.student10792819.model;

/**
 * The Class RedditListable.
 */
public class RedditListable {

	/**
	 * The Enum Type.
	 */
	public enum Type {

		/** The STORY. */
		STORY, 
		/** The BEFORE. */
		BEFORE, 
		/** The AFTER. */
		AFTER, 
		/** The COMMENT. */
		COMMENT;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			switch (this) {
			case BEFORE:
				return "before";
			case AFTER:
				return "after";
			default:
				return "";
			}
		}
	};

	/** The id. */
	private String id;

	/** The type. */
	private Type type;

	/**
	 * Instantiates a new reddit listable.
	 *
	 * @param type the type
	 * @param id the id
	 */
	public RedditListable(Type type, String id) {
		this.id = id;
		this.type = type;
	}

	/**
	 * Gets the ID.
	 *
	 * @return the iD
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
}
