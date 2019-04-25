package org.bbswd.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/*
 * This widget is used by the user to select the type of object they wish to operate on.
 * By default it starts up as the PersonPanel, but that can be changed.
 */
public class ToolBarPanel extends JPanel {

	static final long serialVersionUID = 12345L;
	static enum Panels {
		PERSON,
		CLUB,
		PITCH,
		FIXTURE;
	}
	private Panels defPanel = Panels.CLUB;

	private JButton btn_Person;
	private JButton btn_Club;
	private JButton btn_Pitch;
	private JButton btn_Fixture;
	

	public ToolBarPanel() {
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBorder(BorderFactory.createTitledBorder("Toolbar"));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		/*
		 * Person
		 */
		btn_Person = new JButton("Person");
		btn_Person.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Toolbar/Person selected.");
				defPanel = Panels.PERSON;
			}
		});

		GridBagConstraints gbc_btn_Person = new GridBagConstraints();
		gbc_btn_Person.insets = new Insets(0, 0, 5, 5);
		gbc_btn_Person.gridx = 0;
		gbc_btn_Person.gridy = 0;
		add(btn_Person, gbc_btn_Person);

		/*
		 * Club
		 */
		btn_Club = new JButton("Club");
		btn_Club.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Toolbar/Club selected.");
				defPanel = Panels.CLUB;
				return;
			}
		});
		

		GridBagConstraints gbc_btn_Club = new GridBagConstraints();
		gbc_btn_Club.insets = new Insets(0, 5, 5, 0);
		gbc_btn_Club.gridx = 1;
		gbc_btn_Club.gridy = 0;
		add(btn_Club, gbc_btn_Club);

		/*
		 * Pitch
		 */
		btn_Pitch = new JButton("Pitch");
		btn_Pitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Toolbar/Pitch selected.");
				defPanel = Panels.PITCH;
				return;
			}
		});

		GridBagConstraints gbc_btn_Pitch = new GridBagConstraints();
		gbc_btn_Pitch.insets = new Insets(0, 5, 5, 0);
		gbc_btn_Pitch.gridx = 2;
		gbc_btn_Pitch.gridy = 0;
		add(btn_Pitch, gbc_btn_Pitch);

		/*
		 * Fixture
		 */
		btn_Fixture = new JButton("Fixture");
		btn_Fixture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Toolbar/Fixture selected.");
				defPanel = Panels.FIXTURE;
				return;
			}
		});

		GridBagConstraints gbc_btn_Fixture = new GridBagConstraints();
		gbc_btn_Fixture.insets = new Insets(0, 5, 5, 0);
		gbc_btn_Fixture.gridx = 3;
		gbc_btn_Fixture.gridy = 0;
		add(btn_Fixture, gbc_btn_Fixture);	
	}


	public Panels getDefPanel() {
		return defPanel;
	}
}
