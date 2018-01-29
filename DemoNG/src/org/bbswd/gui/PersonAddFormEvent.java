package org.bbswd.gui;

import java.util.EventObject;

import org.bbswd.model.Person;

/**
 * A FormEvent object, which extends EventObject also contains a Person object.
 * 
 * @author brian
 *
 */
public class PersonAddFormEvent extends EventObject {
	private Person person;

	/**
	 * Constructor for a FormEvent. This particular constructor can probably be
	 * removed; I don't think its used. But I have left it for safety, just in
	 * case it gets called from some part of the runtime that has not been
	 * considered.
	 * 
	 * @param source
	 *            is an Object that is the source of the event being
	 *            constructed. In this specific case, it will be the data form
	 *            panel in the application that is raising the event.
	 */
	public PersonAddFormEvent(Object source) {
		super(source);
		this.person = null;
	}

	/**
	 * Constructor for a FormEvent.
	 * 
	 * @param source
	 *            is an Object that is the source of the event being
	 *            constructed. In this specific case, it will be the data form
	 *            panel in the application that is raising the event.
	 *
	 * @param person
	 *            is an Object of class Person that contains details of the
	 *            Person about the FormEvent being constructed.
	 * 
	 */
	public PersonAddFormEvent(Object source, Person person) {
		super(source);
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}