package org.bbswd.gui;

/**
 * This interface is used to "Listen" for data about a new person being added to
 * the system.
 * 
 * Any class that implements this interface must provide an actual
 * implementation of the "personAddDataEntered()" method. The method is used to
 * deliver the details of the person entered to a listener.
 */
public interface PersonAddDataListener {
	public void personAddDataEntered();
}
