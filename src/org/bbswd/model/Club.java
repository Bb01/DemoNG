package org.bbswd.model;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.bbswd.model.AgeCategory;
import org.bbswd.model.GenderCategory;

/**
 * The Club class is used to store a club's name/details. A Club is
 * defined as a combination of:
 * <ul>
 * <li>Name</li>
 * <li>Address</li>
 * <li>Logo</li>
 * </ul>
 * 
 * </ul>
 * 
 * @author Bb
 * @version v1.0a
 * 
 */

public class Club {
	private static final Logger clubLog = LogManager.getLogger(Club.class.getName());

	// Members of the Name class:
	private int id = 0;
	private StringBuffer firstName = new StringBuffer();
	private StringBuffer lastName = new StringBuffer();
	private StringBuffer middleName = new StringBuffer();
	private StringBuffer alias = new StringBuffer();
	private StringBuffer title = new StringBuffer();
	private AgeCategory ageCat = AgeCategory.UNKNOWN;
	private GenderCategory genderCat = GenderCategory.UNKNOWN;

	/**
	 * Constructor for a Club object with values provided for 5 of 5 members
	 * by the caller. Each parameter is expected to have been constraint tested
	 * at the UI level.
	 * 
	 * @param fn
	 *            The First Name value
	 * @param ln
	 *            The Last Name value
	 * @param mn
	 *            The Middle Name value
	 * @param a
	 *            The Alias value
	 * @param t
	 *            The Title value
	 *
	 */
	public Club(String fn, String ln, String mn, String a, String t) {
		clubLog.trace("Name(String, String, String, String, String) Entering.");

		firstName.setLength(0);
		firstName.append(fn);

		lastName.setLength(0);
		lastName.append(ln);

		middleName.setLength(0);
		middleName.append(mn);

		alias.setLength(0);
		alias.append(a);

		title.setLength(0);
		title.append(t);

		id = 0;

		ageCat = AgeCategory.UNKNOWN;
		genderCat = GenderCategory.UNKNOWN;

		clubLog.trace("Name(String, String, String, String, String) Leaving normally.");
	}

	/**
	 * Return to the caller the ID member of the Club object.
	 * 
	 * @return String The contents of the "ID" member of the Club object.
	 */
	public int getID() {
		clubLog.trace("getID() Entering.");
		return id;
	}

	/**
	 * Return to the caller the First Name member of the Club object.
	 * 
	 * @return String The contents of the "First Name" member of the Club
	 *         object.
	 */
	public String getFirstName() {
		clubLog.trace("getFirstName() Entering.");
		return firstName.toString();
	}

	/**
	 * Return to the caller the Last Name member of the Club object.
	 * 
	 * @return String The contents of the "Last Name" member of the Club
	 *         object.
	 */
	public String getLastName() {
		clubLog.trace("getLastName() Entering.");
		return lastName.toString();
	}

	/**
	 * Return to the caller the Middle Name member of the Club object.
	 * 
	 * @return String The contents of the "Middle Name" member of the Club
	 *         object.
	 */
	public String getMiddleName() {
		clubLog.trace("getMiddleName() Entering.");
		return middleName.toString();
	}

	/**
	 * Return to the caller the "Alias" member of the Club object.
	 * 
	 * @return String The contents of the Alias member of the Club object.
	 */
	public String getAlias() {
		clubLog.trace("getAlias() Entering.");
		return alias.toString();
	}

	/**
	 * Return to the caller the Title member of the Club object.
	 * 
	 * @return String The contents of the "Title" member of the Club object.
	 */
	public String getTitle() {
		clubLog.trace("getTitle() Entering.");
		return title.toString();
	}

	/**
	 * This "toString" method returns a string representation of a "Club"
	 * object. Each member is separated by a ":" (colons are not allowed as part
	 * of value for a Club object). The members are ordered:
	 * <ol>
	 * <li>Title</li>
	 * <li>FirstName</li>
	 * <li>MiddleName</li>
	 * <li>LastName</li>
	 * <li>Alias</li>
	 * </ol>
	 * 
	 * @return String - the Club object converted to a readable string
	 */
	@Override
	public String toString() {
		String t;
		clubLog.trace("toString() Entering.");
		t = this.getID() + ":" + this.getTitle() + ":" + this.getFirstName() + ":" + this.getMiddleName() + ":"
				+ this.getLastName() + ":" + this.getAlias();
		clubLog.trace("toString() Leaving normally.");
		return t;

	}

	/**
	 * Set the id attribute of the in-memory representation of the Club
	 * object. The value of the id should be from the value assigned by the
	 * database, its an auto-assigned, auto-incremented database value.
	 * 
	 * In theory, this method could be called from any part of the application.
	 * In practice it will only be called by the methods in the Database object.
	 * 
	 * All Ids must be unique and advancing/incrementing. Old Ids are not re-used.
	 * 
	 * @param id
	 * The integer value that the Id should be set to.
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}
}
