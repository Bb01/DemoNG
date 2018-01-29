package org.bbswd.model;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.bbswd.model.AgeCategory;
import org.bbswd.model.GenderCategory;

/**
 * The Person class is used to store an individual's name/details. A Person is
 * defined as a combination of:
 * <ul>
 * <li>Title</li>
 * <li>First Name</li>
 * <li>Middle Name</li>
 * <li>Last Name</li>
 * <li>Alias</li>
 * <li>Gender</li>
 * <li>Age Category</li>
 * <li>Photo</li>
 * </ul>
 * Each of the members of the class have their own properties like:
 * <ul>
 * <li>Minimum Length</li>
 * <li>Maximum Length</li>
 * <li>Allowed values (controlled by Regex)</li>
 * <li>Whether a value is optional or not</li>
 * </ul>
 * 
 * Here are some things that should be looked at for this class:
 * <ul>
 * <li>TODO: In some of the "Person" Logging we can add more info to make the
 * log output more meaningful to the reader.</li>
 * </ul>
 * 
 * @author Bb
 * @version v1.0a
 * 
 */

public class Person {
	private static final Logger personLog = LogManager.getLogger(Person.class.getName());

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
	 * Constructor for a Person object with values provided for 5 of 5 members
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
	public Person(String fn, String ln, String mn, String a, String t) {
		personLog.trace("Name(String, String, String, String, String) Entering.");

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

		personLog.trace("Name(String, String, String, String, String) Leaving normally.");
	}

	/**
	 * Return to the caller the ID member of the Person object.
	 * 
	 * @return String The contents of the "ID" member of the Person object.
	 */
	public int getID() {
		personLog.trace("getID() Entering.");
		return id;
	}

	/**
	 * Return to the caller the First Name member of the Person object.
	 * 
	 * @return String The contents of the "First Name" member of the Name
	 *         object.
	 */
	public String getFirstName() {
		personLog.trace("getFirstName() Entering.");
		return firstName.toString();
	}

	/**
	 * Return to the caller the Last Name member of the Person object.
	 * 
	 * @return String The contents of the "Last Name" member of the Person
	 *         object.
	 */
	public String getLastName() {
		personLog.trace("getLastName() Entering.");
		return lastName.toString();
	}

	/**
	 * Return to the caller the Middle Name member of the Person object.
	 * 
	 * @return String The contents of the "Middle Name" member of the Name
	 *         object.
	 */
	public String getMiddleName() {
		personLog.trace("getMiddleName() Entering.");
		return middleName.toString();
	}

	/**
	 * Return to the caller the "Alias" member of the Person object.
	 * 
	 * @return String The contents of the Alias member of the Person object.
	 */
	public String getAlias() {
		personLog.trace("getAlias() Entering.");
		return alias.toString();
	}

	/**
	 * Return to the caller the Title member of the Person object.
	 * 
	 * @return String The contents of the "Title" member of the Person object.
	 */
	public String getTitle() {
		personLog.trace("getTitle() Entering.");
		return title.toString();
	}

	/**
	 * This "toString" method returns a string representation of a "Name"
	 * object. Each member is separated by a ":" (colons are not allowed as part
	 * of value for a Person object). The members are ordered:
	 * <ol>
	 * <li>Title</li>
	 * <li>FirstName</li>
	 * <li>MiddleName</li>
	 * <li>LastName</li>
	 * <li>Alias</li>
	 * </ol>
	 * 
	 * @return String - the Person object converted to a readable string
	 */
	@Override
	public String toString() {
		String t;
		personLog.trace("toString() Entering.");
		t = this.getID() + ":" + this.getTitle() + ":" + this.getFirstName() + ":" + this.getMiddleName() + ":"
				+ this.getLastName() + ":" + this.getAlias();
		personLog.trace("toString() Leaving normally.");
		return t;

	}

	/**
	 * Set the id attribute of the in-memory representation of the Person
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
	
	/*
	 * TODO: Add some methods here to get/set the gender & age categories. Will
	 * also need a getter for the ID.
	 */
}
