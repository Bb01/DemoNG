package org.bbswd.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A Database object is an object that allows a user to create application
 * objects (eg Person, Fixture, etc). The objects are stored in an external
 * database, but they can also be cached in application memory.
 * 
 * Objects in the database, are "the master". The ones in memory are
 * secondary/cached.
 * 
 * Currently the only application object support is the "Person" object.
 * 
 * @author brian
 *
 */
public class Database {
	private static Connection dBConnection=null;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
	
	private ArrayList<Person> personList;
	private ArrayList<Club> clubList;


	/**
	 * Database constructor. This will:
	 * <ol>
	 * <li>Establish a connection with a remote database</li>
	 * <li>Create and initialize an in-memory cached representation of the
	 * application data.</li>
	 * <li>Populate the cache with the latest data from the database.</li>
	 * </ol>
	 * 
	 * @throws Exception
	 * 
	 */
	public Database() throws Exception {
		if (dBConnection != null)
			throw new AlreadyConnected();

		personList = new ArrayList<Person>();
		clubList = new ArrayList<Club>();
		
		connect();
	}

	/**
	 * Connect to an external SQL database via JDBC.
	 * 
	 * This could be updated at some point to do a few things:
	 * <ul>
	 * <li>Create and initialize an in-memory representation of the application
	 * data (see default Database constructor).</li>
	 * <li>Allow a user to specify connection details as parameters.</li>
	 * <li>Add some application logging.</li>
	 * </ul>
	 * 
	 * @throws SQLException
	 */
	public void connect() throws Exception {

		if (dBConnection != null)
			throw new AlreadyConnected();

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver not found");
		}

		String url = "jdbc:mysql://localhost:3306/demong";
		//String url = "jdbc:mysql://brianbreathnach.com:3306/brianbre_demong";

		dBConnection = DriverManager.getConnection(url, "root", "BbBb");
		//dBConnection = DriverManager.getConnection(url, "brianbre_root", ".lG=-3kZ8Tou");
	}

	/**
	 * Disconnect from an SQL database.
	 */
	public void disconnect() {
		if (dBConnection != null) {
			try {
				dBConnection.close();
				dBConnection=null;
			} catch (SQLException e) {
				System.out.println("Can't close connection");
			}
		}
	}

	/**
	 * Load the data stored in the external database into the application.
	 * 
	 * I believe that what I need to do here is: + do a select on the database
	 * to fetch all the records. + read them one by one and create in-memory
	 * Person objects + add these Person objects to the application's in-memory
	 * ArrayList<Person>
	 * 
	 * Some issues:
	 * + We want the data to appear in the PersonDataDisplayPanel
	 *   which we cannot access directly from here.
	 */
	public void loadPersonData() throws SQLException {
		String checkCountSql = "SELECT COUNT(*) AS rowcount FROM person";
		PreparedStatement checkCountStmt = dBConnection.prepareStatement(checkCountSql);

		String selectPersonSql = "SELECT person_id, firstname, lastname, middlename, alias, title FROM person;";
		PreparedStatement selectPersonStmt = dBConnection.prepareStatement(selectPersonSql);

		System.out.println("Loading...");
		ResultSet checkResult = checkCountStmt.executeQuery();
		checkResult.next();
		int count = checkResult.getInt(1);
		if (count != 0) {
			/*
			 * There are some records in the database. Let's suck them up into
			 * the app.
			 */
			ResultSet myResult = selectPersonStmt.executeQuery();
			
			myResult.next();
			for (int i = 0; i < count; i++) {
				int id = myResult.getInt("person_id");
				String fname = myResult.getString("firstname");
				String lname = myResult.getString("lastname");
				String mname = myResult.getString("middlename");
				String title = myResult.getString("title");
				String alias = myResult.getString("alias");

				Person person = new Person(fname, lname, mname, title, alias);
				person.setId(id);
				personList.add(person);
				myResult.next();
			}
		}

		checkCountStmt.close();
		selectPersonStmt.close();
	}

	/**
	 * Add a Person object to the List of Person objects managed by the
	 * application.
	 * 
	 * When a person is added, the following occurs:
	 * <ul>
	 * <li>The Person object is inserted into the external DB.</li>
	 * <li>The auto-generated ID of the inserted record is extracted from the
	 * newly inserted DB Person object.</li>
	 * <li>The in-memory Person object is updated with the ID from the DB.</li>
	 * <li>The Person object is added to the cached in-memory Person List.</li>
	 * </ul>
	 * 
	 * @param person
	 *            The Person to be added.
	 *
	 * @return an integer value of 0 if successful, else non-zero.
	 */
	public int addPerson(Person person) throws SQLException {
		String checkCountSql = "SELECT COUNT(*) AS rowcount FROM person WHERE (firstname=? AND lastname=? AND middlename=? AND alias=? AND title=?)";
		PreparedStatement checkCountStmt = dBConnection.prepareStatement(checkCountSql);

		String selectPersonSql = "SELECT person_id FROM person WHERE (firstname=? AND lastname=? AND middlename=? AND alias=? AND title=?)";
		PreparedStatement selectPersonStmt = dBConnection.prepareStatement(selectPersonSql);

		String insertSql = "INSERT INTO person (firstname, lastname, middlename, alias, title) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = dBConnection.prepareStatement(insertSql);

		int retStatus = 0;

		String fname = person.getFirstName();
		String lname = person.getLastName();
		String mname = person.getMiddleName();
		String alias = person.getAlias();
		String title = person.getTitle();

		/*
		 * First, lets check if there's already a person in the database with
		 * the same details.
		 */
		checkCountStmt.setString(1, fname);
		checkCountStmt.setString(2, lname);
		checkCountStmt.setString(3, mname);
		checkCountStmt.setString(4, alias);
		checkCountStmt.setString(5, title);
		ResultSet checkResult = checkCountStmt.executeQuery();
		checkResult.next();
		int count = checkResult.getInt(1);
		if (count == 0) {
			/*
			 * Looks like this is a fresh one! So going to add it to the
			 * database.
			 */
			insertStmt.setString(1, fname);
			insertStmt.setString(2, lname);
			insertStmt.setString(3, mname);
			insertStmt.setString(4, alias);
			insertStmt.setString(5, title);

			int myResult = insertStmt.executeUpdate();
			if (myResult == 1) {
				System.out.println("Insert worked - YIPPEE");
				/*
				 * Next thing to do is get the ID that the DB assigned to the
				 * Person just added.
				 */
				selectPersonStmt.setString(1, fname);
				selectPersonStmt.setString(2, lname);
				selectPersonStmt.setString(3, mname);
				selectPersonStmt.setString(4, alias);
				selectPersonStmt.setString(5, title);
				checkResult = selectPersonStmt.executeQuery();
				checkResult.next();
				int id = checkResult.getInt("person_id");
				person.setId(id);
				personList.add(person);
			} else {
				System.out.println("Could not insert!");
				retStatus = 1;
			}
		} else {
			/*
			 * Other records match this, we don't want to add it.
			 */
			System.out.println("Multiple records matched - cannot add");
			retStatus = 1;
		}

		selectPersonStmt.close();
		insertStmt.close();
		checkCountStmt.close();

		return retStatus;
	}

	/**
	 * Returns the current date-time, at the time of invocation, in the format
	 * specified.
	 * 
	 * @return
	 */
	private static String getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return dateFormat.format(today.getTime());
	}

	/**
	 * Returns the entire List of Person objects.
	 * 
	 * @return The List of Person objects.
	 */
	public ArrayList<Person> getPersonList() {
		return personList;
	}

	/**
	 * Get an individual person from the Person list. I need to decide if this
	 * get's me the 'i'th person or the person with the id to the value of i.
	 * 
	 * @param i
	 *            the Person being sought.
	 * 
	 * @return Returns a Person object corresponding to the one specified by
	 *         'i'.
	 */
	public Person getPerson(int i) {
		if (dBConnection != null)
			return personList.get(i);
		throw new NullConnection();
	}
	
	//////////
	
	/**
	 * Load the data stored in the external database into the application.
	 * 
	 * I believe that what I need to do here is: + do a select on the database
	 * to fetch all the records. + read them one by one and create in-memory
	 * Person objects + add these Person objects to the application's in-memory
	 * ArrayList<Person>
	 * 
	 * Some issues:
	 * + We want the data to appear in the PersonDataDisplayPanel
	 *   which we cannot access directly from here.
	 */
	public void loadClubData() throws SQLException {
		String checkCountSql = "SELECT COUNT(*) AS rowcount FROM person";
		PreparedStatement checkCountStmt = dBConnection.prepareStatement(checkCountSql);

		String selectClubSql = "SELECT person_id, firstname, lastname, middlename, alias, title FROM person;";
		PreparedStatement selectClubStmt = dBConnection.prepareStatement(selectClubSql);

		System.out.println("Loading...");
		ResultSet checkResult = checkCountStmt.executeQuery();
		checkResult.next();
		int count = checkResult.getInt(1);
		if (count != 0) {
			/*
			 * There are some records in the database. Let's suck them up into
			 * the app.
			 */
			ResultSet myResult = selectClubStmt.executeQuery();
			
			myResult.next();
			for (int i = 0; i < count; i++) {
				int id = myResult.getInt("person_id");
				String fname = myResult.getString("firstname");
				String lname = myResult.getString("lastname");
				String mname = myResult.getString("middlename");
				String title = myResult.getString("title");
				String alias = myResult.getString("alias");

				Club club = new Club(fname, lname, mname, title, alias);
				club.setId(id);
				System.out.println("Club:" + i + ":" + club);
				clubList.add(club);
				myResult.next();
			}
		}

		checkCountStmt.close();
		selectClubStmt.close();
	}

	/**
	 * Add a Club object to the List of Club objects managed by the
	 * application.
	 * 
	 * When a club is added, the following occurs:
	 * <ul>
	 * <li>The Club object is inserted into the external DB.</li>
	 * <li>The auto-generated ID of the inserted record is extracted from the
	 * newly inserted DB Club object.</li>
	 * <li>The in-memory Club object is updated with the ID from the DB.</li>
	 * <li>The Club object is added to the cached in-memory Club List.</li>
	 * </ul>
	 * 
	 * @param club
	 *            The Club to be added.
	 *
	 * @return an integer value of 0 if successful, else non-zero.
	 */
	public int addClub(Club club) throws SQLException {
		String checkCountSql = "SELECT COUNT(*) AS rowcount FROM person WHERE (firstname=? AND lastname=? AND middlename=? AND alias=? AND title=?)";
		PreparedStatement checkCountStmt = dBConnection.prepareStatement(checkCountSql);

		String selectClubSql = "SELECT person_id FROM person WHERE (firstname=? AND lastname=? AND middlename=? AND alias=? AND title=?)";
		PreparedStatement selectClubStmt = dBConnection.prepareStatement(selectClubSql);

		String insertSql = "INSERT INTO person (firstname, lastname, middlename, alias, title) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = dBConnection.prepareStatement(insertSql);

		int retStatus = 0;

		String fname = club.getFirstName();
		String lname = club.getLastName();
		String mname = club.getMiddleName();
		String alias = club.getAlias();
		String title = club.getTitle();

		/*
		 * First, lets check if there's already a club in the database with
		 * the same details.
		 */
		checkCountStmt.setString(1, fname);
		checkCountStmt.setString(2, lname);
		checkCountStmt.setString(3, mname);
		checkCountStmt.setString(4, alias);
		checkCountStmt.setString(5, title);
		ResultSet checkResult = checkCountStmt.executeQuery();
		checkResult.next();
		int count = checkResult.getInt(1);
		if (count == 0) {
			/*
			 * Looks like this is a fresh one! So going to add it to the
			 * database.
			 */
			insertStmt.setString(1, fname);
			insertStmt.setString(2, lname);
			insertStmt.setString(3, mname);
			insertStmt.setString(4, alias);
			insertStmt.setString(5, title);

			int myResult = insertStmt.executeUpdate();
			if (myResult == 1) {
				System.out.println("Insert worked - YIPPEE");
				/*
				 * Next thing to do is get the ID that the DB assigned to the
				 * Person just added.
				 */
				selectClubStmt.setString(1, fname);
				selectClubStmt.setString(2, lname);
				selectClubStmt.setString(3, mname);
				selectClubStmt.setString(4, alias);
				selectClubStmt.setString(5, title);
				checkResult = selectClubStmt.executeQuery();
				checkResult.next();
				int id = checkResult.getInt("person_id");
				club.setId(id);
				clubList.add(club);
			} else {
				System.out.println("Could not insert!");
				retStatus = 1;
			}
		} else {
			/*
			 * Other records match this, we don't want to add it.
			 */
			System.out.println("Multiple records matched - cannot add");
			retStatus = 1;
		}

		selectClubStmt.close();
		insertStmt.close();
		checkCountStmt.close();

		return retStatus;
	}


	/**
	 * Returns the entire List of Person objects.
	 * 
	 * @return The List of Person objects.
	 */
	public ArrayList<Club> getClubList() {
		return clubList;
	}

	/**
	 * Get an individual club from the Club list. I need to decide if this
	 * get's me the 'i'th club or the club with the id to the value of i.
	 * 
	 * @param i
	 *            the Club being sought.
	 * 
	 * @return Returns a Club object corresponding to the one specified by
	 *         'i'.
	 */
	public Club getClub(int i) {
		if (dBConnection != null)
			return clubList.get(i);
		throw new NullConnection();
	}
	//////////
		
	public static class NullConnection extends RuntimeException {
	}
	
	public static class AlreadyConnected extends RuntimeException {
	}
}
