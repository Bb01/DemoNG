package org.bbswd.gui;

/**
 * This interface is used to "Listen" for when a user selects "Reset" when entering person data into
 * the system.
 * 
 * Any class that implements this interface must provide an actual
 * implementation of the "personAddResetEntered()" method. The method is used to
 * reset elements of the GUI.
 */
public interface PersonAddResetListener {
	public void personAddResetEntered();
}
