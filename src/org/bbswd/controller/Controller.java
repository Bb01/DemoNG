package org.bbswd.controller;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * The GUI just collects data from the user.
 * Its a presentation layer.
 * 
 * The controller validates the data entered via the GUI.
 * It provides this validation functionality to the GUI through a
 * Some validation can be done in the Controller using rules/constraints.
 * More validation can be done in the back-end database, which the controller
 * will also handle.
 * 
 * Another function of the controller is to maintain a cache of data in memory
 * that the GUI uses for display purposes.
 * Its important that this cache is kept in sync with the data in the back-end
 * and this is part of the controllers charter.
 *  
 * Validation of that data 
 * So the idea is that the GUI/MainFrame will call the controller when it wants to 
 * add things to the database.
 * 
 */
public class Controller {
	final public static boolean FIRSTNAME_OPTIONAL = false;
	final public static boolean LASTNAME_OPTIONAL = false;
	final public static boolean MIDDLENAME_OPTIONAL = true;
	final public static boolean ALIAS_OPTIONAL = true;
	final public static boolean TITLE_OPTIONAL = true;

	final public static int MIN_FIRSTNAME_LEN = 2;
	final public static int MIN_LASTNAME_LEN = 1;
	final public static int MIN_MIDDLENAME_LEN = 0;
	final public static int MIN_ALIAS_LEN = 0;
	final public static int MIN_TITLE_LEN = 0;

	final public static int MAX_FIRSTNAME_LEN = 32;
	final public static int MAX_LASTNAME_LEN = 32;
	final public static int MAX_MIDDLENAME_LEN = 32;
	final public static int MAX_ALIAS_LEN = 32;
	final public static int MAX_TITLE_LEN = 20;

	final public static String FIRSTNAME_REGEX = "(^[A-Z]([A-Za-z ]{1,31})$)";
	final public static String LASTNAME_REGEX = "(^[A-Za-z]([A-Za-z ']{0,31})$)";
	final public static String MIDDLENAME_REGEX = "(^$)|(^[A-Z]$)|((^[A-Z][A-Za-z ]{1,31})$)";
	final public static String ALIAS_REGEX = "(^$)|(^[A-Z]$)|((^[A-Z][A-Za-z ]{1,31})$)";
	final public static String TITLE_REGEX = "(^$)|(^[A-Z]$)|((^[A-Z][A-Za-z .]{1,19})$)";

	final public static String ErrMsgs[] = { 
			"BBSWD00000: ERROR: Generic Error!", 
			"BBSWD00001: WARNING: Generic Warning!",
			"BBSWD00002: INFO: Generic Informational!", 
			"BBSWD00003: DEBUG: Generic Informational!",
			"BBSWD00004: VERBOSE: Generic Informational!", 
			"BBSWD00010: ERROR: Value is null.",
			"BBSWD00011: ERROR: Value not the minimum length required.",
			"BBSWD00012: WARNING: Value is too long - truncated.",
			"BBSWD00013: ERROR: Value is not composed of valid char sequence." };

	public boolean constraintCheck(boolean opt, int min_len, int max_len, String regex, String checkMe, StringBuffer ret, ArrayList<String> issueList) {
		boolean allGood = true;

		if (ret != null) {
			ret.setLength(0);
		}

		if (checkMe == null) {
			issueList.add("BBSWD00010: ERROR: Value is null.");
			allGood = false;
		} else {
			int len = checkMe.length();

			if (len != 0) {
				if (len < min_len) {
					issueList.add("BBSWD00011: ERROR: Value not the minimum length required.");
					allGood = false;
				}

				StringBuffer sb1 = new StringBuffer();
				if (len > max_len) {
					issueList.add("BBSWD00012: WARNING: Value is too long - truncated.");
					sb1.append(checkMe.substring(0, max_len));
					len = max_len;
				} else {
					sb1.append(checkMe);
				}

				Pattern theRegex = Pattern.compile(regex);
				Matcher match = theRegex.matcher(checkMe.substring(0, len));
				if (!match.find()) {
					issueList.add("BBSWD00013: ERROR: Value is not composed of valid char sequence.");
					allGood = false;
				}

				if (allGood) {
					ret.append(sb1);
				}
			} else {
				/*
				 * Length is zero, but is a value optional for the item being checked?
				 */
				if (opt == false) {
					issueList.add("BBSWD00011: ERROR: Value not the minimum length requiredxxx.");
					allGood = false;
				}			
			}
		}

		return allGood;
	}
}
