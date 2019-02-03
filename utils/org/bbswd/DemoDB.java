package org.bbswd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bbswd.model.Database;
import org.bbswd.model.Person;

/**
 * This is just a demo utility to trick around with demo application data in the
 * database.
 * 
 * @author brian
 *
 */
public class DemoDB {

	private final static String[][] data = { { "", "Mickey", "Mouse", "Disney", "MM", "Mr" },
			{ "", "Bugs", "Bunny", "Warner", "Bugsy", "Mr" },
			{ "", "Yosemite", "Sam", "Warner", "", "Mr" },
			{ "", "Road", "Runner", "Warner", "Bird Brain", "" },
			{ "", "Wile E", "Coyote", "Warner", "", "Mr" },
			{ "", "Fred", "Flintstone", "Hanna Barbera", "Rocky", "Mr" },
			{ "", "Woody", "Woodpecker", "Walter Lantz", "", "" },
			{ "", "Ima", "NewUser", "Bb", "", ""}};
	private static ArrayList<Person> personList;
	private static Database db;

	public static void main(String[] args) {
		System.out.println("Database Demo Start");


		try {
			db = new Database();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		try {
			db.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addPeopleToDB();
		
		loadPeopleFromDB();


		db.disconnect();
		
		/*
		 * Now that everything is in the Database, what have we got in the cached ArrayList<Person>?
		 */
		System.out.println("In memory cache:");
		personList = db.getPersonList();
		for (Person p: personList ) System.out.println(p);
		
		
		
		System.out.println("Database Demo End");
	}

	/**
	 * Load all people from the DB into application memory.
	 */
	private static void loadPeopleFromDB() {
		try {
			db.loadPersonData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Add a (2D) array of people to the DB.
	 */
	public static void addPeopleToDB(){
		Person person;
		int len = data.length;

		for (int i = 0; i < len; i++) {
			person = new Person(data[i][1], data[i][2], data[i][3], data[i][4], data[i][5]);
			try {
				System.out.println("Going to add:" + person);
				db.addPerson(person);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
