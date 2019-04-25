package org.bbswd.gui;

import java.util.EventObject;

import org.bbswd.model.Club;

/**
 * A FormEvent object, which extends EventObject also contains a Person object.
 * 
 * @author brian
 *
 */
public class ClubAddFormEvent extends EventObject {
	private Club club;

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
	public ClubAddFormEvent(Object source) {
		super(source);
		this.club = null;
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
	public ClubAddFormEvent(Object source, Club club) {
		super(source);
		this.club = club;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}
}