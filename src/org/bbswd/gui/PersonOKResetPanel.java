package org.bbswd.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PersonOKResetPanel extends JPanel {

	static final long serialVersionUID = 12345L;

	JButton btn_OK;
	JButton btn_Reset;

	private PersonAddDataListener personAddDataListener;
	private PersonAddResetListener personAddResetListener;


	/**
	 * Create a simple OK/Reset panel. This is a panel with a single pair of
	 * pushbuttons in a single row, evenly spaced.
	 */
	public PersonOKResetPanel(PersonAddDataListener personListener) {

		personAddDataListener = personListener;
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 0, 0 };
		gbl.rowHeights = new int[] { 0 };
		gbl.columnWeights = new double[] { 1.0, 1.0 };
		gbl.rowWeights = new double[] { 0.0 };
		setLayout(gbl);

		setBorder(BorderFactory.createTitledBorder("OK/Reset"));
		
		if (personListener == null) {
			System.out.println("Why null?");
		}

		btn_OK = new JButton("OK");
		btn_OK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				personAddDataListener.personAddDataEntered();
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(btn_OK, gbc);

		btn_Reset = new JButton("Reset");
		btn_Reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				personAddResetListener.personAddResetEntered();
			}
		});

		gbc.gridx = 1;
		add(btn_Reset, gbc);
	}

	/**
	 * When a user supplied attribute values is tested against some constraints, the constraint tests could fail.
	 * There could be several reasons for the failure.
	 * Any issues with the user supplied values are recorded in a list that is processed in this method.
	 * Currently, this means that a dialog box is displayed for each issue recorded for each attribute.
	 * 
	 * @param attName		The name of attribute (as it appears in the UI) being processed. 
	 * @param attVal		The value of the attribute being processed.
	 * @param issueList		An ArrayList of <String> with the issues to be processed.
	 */
	void handleResult(String attName, String attVal, ArrayList<String> issueList) {
		if (issueList.size() == 0) {
			/*
			 * Clean run! No issues.
			 */
		} else {
			/*
			 * At least one issue arose. It could be one or more warnings or
			 * errors. Just iterate through the list of issues, then purge the
			 * list.
			 */
			int size = issueList.size();
			for (int i = 0; i < size; i++) {
				if ((issueList.get(i)).contains("WARNING")) {
					JOptionPane.showMessageDialog(null, issueList.get(i), "Warning in " + attName + ": " + attVal,
							JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, issueList.get(i), "Error in " + attName + ": " + attVal,
							JOptionPane.ERROR_MESSAGE);
				}
			}

			for (int i = size; i > 0; i--) {
				issueList.remove(i - 1);
			}
		}
	}
}
