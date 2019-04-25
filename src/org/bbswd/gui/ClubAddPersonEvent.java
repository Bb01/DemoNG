package org.bbswd.gui;

import java.util.EventObject;

public class ClubAddPersonEvent extends EventObject {

	private static final long serialVersionUID = 12345L;

	private String id;
	private String fname;
	private String lname;
	private String mname;
	private String alias;
	private String title;

	public ClubAddPersonEvent(Object source) {
		super(source);
	}

	public ClubAddPersonEvent(Object source, String id, String fname, String lname, String mname, String alias, String title) {
		super(source);

		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.mname = mname;
		this.alias = alias;
		this.title = title;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
