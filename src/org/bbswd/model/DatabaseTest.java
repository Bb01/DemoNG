package org.bbswd.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	private Database database;
	
	@Before
	public void setup() throws Exception {
		System.out.println("Setting up...");
		database=new Database();
	}

	@Test
	public void TestConnection() throws Exception {
		System.out.println("TestConnection()");
		assertNotNull(database);
	}
	
	@Test
	public void TestConnectDisconnect() throws Exception {
		System.out.println("TestConnectDisconnect()");
		assertNotNull(database);
	}
	
	@Test
	public void ConnectGetPerson() throws Exception {
		System.out.println("ConnectGetPerson()");
		Person person;
		database.loadPersonData();
		person=database.getPerson(0);
	}
	
	@Test
	public void ConnectGetListOfPersons() throws Exception {
		System.out.println("ConnectGetListOfPersons()");
		ArrayList<Person> person;
		database.loadPersonData();
		person=database.getPersonList();
		assertNotEquals(0, person.size());
	}
	
	@Test
	public void AddAPerson() throws Exception {
		System.out.println("AddAPerson()");
		ArrayList<Person> personList=database.getPersonList();
		int personListSize=personList.size();

		Person person=new Person("I", "Tester", "Wanna B A", "", "");
		database.addPerson(person);
		
		personList=database.getPersonList();
		assertEquals(personListSize+1, personList.size());
	}
	
	@Test (expected=Database.NullConnection.class)
	public void AfterDisconnectOneCannotReuseTheDatabaseConnection() throws Exception {
		System.out.println("AfterDisconnectOneCannotReuseTheDatabaseConnection()");
		Person person;
		database.loadPersonData();
		person=database.getPerson(0);
		database.disconnect();
		person=database.getPerson(0);	
	}
	
	@After
	public void runAfterTestMethod() {
		database.disconnect();
        System.out.println("Cleaning up...");
        System.out.println("");
        System.out.println("");
    }
}
