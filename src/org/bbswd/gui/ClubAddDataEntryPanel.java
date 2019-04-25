package org.bbswd.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author brian
 *
 *         This class creates a panel of labeled data entry fields and allows
 *         the user to select OK/Cancel.
 * 
 */
public class ClubAddDataEntryPanel extends JPanel {

	static final long serialVersionUID = 12345L;

	private JTextField tf_FirstName;
	private JTextField tf_LastName;
	private JTextField tf_MiddleName;
	private JTextField tf_Alias;
	private JTextField tf_Title;
	
	private JLabel lbl_FirstName;
	private JLabel lbl_LastName;
	private JLabel lbl_MiddleName;
	private JLabel lbl_Alias;
	private JLabel lbl_Title;

	private String fname;
	private String lname;
	private String mname;
	private String alias;
	private String title;

	private GridBagConstraints gbc_left;
	private GridBagConstraints gbc_right;

	private static final Logger clubDataEntryPanelLog = LogManager.getLogger(ClubAddDataEntryPanel.class.getName());

	/**
	 * Create the Person Data Entry Panel
	 * 
	 * This panel is used to contain a series of labels and text fields that are
	 * used to enter basic data about the .
	 *
	 * The data is collected but NOT validated. Mouse over tool tips/hints are
	 * in place for each JLabel field.
	 */
	public ClubAddDataEntryPanel() {

		clubDataEntryPanelLog.trace("ClubDataEntryPanel() Entering.");

		/*
		 * Surround the JPanel with a border of 5 pixels.
		 */
		// setBorder(new EmptyBorder(5, 5, 5, 5));
		setBorder(BorderFactory.createTitledBorder("ClubDataEntryPanel"));

		GridBagLayout gbl = new GridBagLayout();

		/*
		 * Set the overrides to the column minimum widths. If this field is
		 * non-null the values are applied to the gridbag after all of the
		 * minimum columns widths have been calculated. If columnWidths has more
		 * elements than the number of columns, columns are added to the gridbag
		 * to match the number of elements in columnWidths.
		 */
		gbl.columnWidths = new int[] { 0, 0 };

		/*
		 * Set the overrides to the row minimum heights. If this field is
		 * non-null the values are applied to the gridbag after all of the
		 * minimum row heights have been calculated. If rowHeights has more
		 * elements than the number of rows, rows are added to the gridbag to
		 * match the number of elements in rowHeights.
		 * 
		 * We've got 5 x rows, even/same heights.
		 */
		gbl.rowHeights = new int[] { 0, 0, 0, 0, 0 };

		/*
		 * Set the overrides to the column weights. If this field is non-null
		 * the values are applied to the gridbag after all of the columns
		 * weights have been calculated. If columnWeights[i] > weight for column
		 * i, then column i is assigned the weight in columnWeights[i]. If
		 * columnWeights has more elements than the number of columns, the
		 * excess elements are ignored - they do not cause more columns to be
		 * created.
		 * 
		 * The right most column, gets more weight.
		 */
		gbl.columnWeights = new double[] { 1.0, 1.0 };

		/*
		 * Set the overrides to the row weights. If this field is non-null the
		 * values are applied to the gridbag after all of the rows weights have
		 * been calculated. If rowWeights[i] > weight for row i, then row i is
		 * assigned the weight in rowWeights[i]. If rowWeights has more elements
		 * than the number of rows, the excess elements are ignored - they do
		 * not cause more rows to be created.
		 * 
		 * We've got 5 x rows, evenly weighted.
		 */
		gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };

		/*
		 * Set up the layout manager and use a pair of GBCs (one for left side,
		 * one for the right side of the Data Entry Panel).
		 */
		setLayout(gbl);

		gbc_left = new GridBagConstraints();
		gbc_right = new GridBagConstraints();

		lbl_FirstName = new JLabel("First Name*");
		lbl_FirstName.setToolTipText(
				"Required. Minimum length = 2. Maximum length = 32. Alphabetic. Starts with upper case letter.");

		/*
		 * "insets" controls external padding between component edge and display
		 * area Top Left Bottom Right (TLBR)
		 */
		gbc_left.insets = new Insets(0, 0, 5, 5);

		/*
		 * x,y = 0,0 = Top Left Corner of the grid
		 */
		gbc_left.gridx = 0;
		gbc_left.gridy = 0;
		/*
		 * Now add the label and its constraints to the JPanel
		 */
		add(lbl_FirstName, gbc_left);

		/*
		 * Firstname TextField Add some listeners.
		 */
		tf_FirstName = new JTextField("", 32);
		tf_FirstName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fname = tf_FirstName.getText();
			}
		});

		tf_FirstName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				tf_FirstName.selectAll();
			}

			/*
			 * TODO: I need to investigate this further. fname is a String
			 * object. We can see it gets assigned values in these methods. But
			 * is there a (String) memory leak here? I am thinking that a text
			 * field gets a value, which we store in a String, then the user may
			 * change that value and a new String assignment is made. But what
			 * happened to the previous String? I just need to understand the
			 * process correctly and put in counter-measures if needed.
			 */
			@Override
			public void focusLost(FocusEvent fe) {
				fname = tf_FirstName.getText();
			}
		});

		gbc_right.insets = new Insets(0, 0, 5, 0);
		gbc_right.anchor = GridBagConstraints.LINE_START;
		gbc_right.gridx = 1;
		gbc_right.gridy = 0;
		gbc_right.weightx = (double) 1.0;
		add(tf_FirstName, gbc_right);

		/*
		 * LastName Label and Text Box
		 */
		lbl_LastName = new JLabel("Last Name*");
		lbl_LastName.setToolTipText("Required. Minimum Length = 1. Maximum length = 32. Alphabetic.");
		gbc_left.gridy = 1;
		add(lbl_LastName, gbc_left);

		tf_LastName = new JTextField("", 32);
		tf_LastName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				lname = tf_LastName.getText();
			}
		});

		tf_LastName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				tf_LastName.selectAll();
			}

			@Override
			public void focusLost(FocusEvent fe) {
				lname = tf_LastName.getText();
			}
		});

		gbc_right.gridy = 1;
		add(tf_LastName, gbc_right);

		/*
		 * Middle name Label and Text Box
		 */
		lbl_MiddleName = new JLabel("Middle Name");
		lbl_MiddleName.setToolTipText("Minimum Length = 0. Maximum length = 32. Alphabetic.");
		gbc_left.gridy = 2;
		add(lbl_MiddleName, gbc_left);

		tf_MiddleName = new JTextField("", 32);
		tf_MiddleName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				mname = tf_MiddleName.getText();
			}
		});

		tf_MiddleName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				tf_MiddleName.selectAll();
			}

			@Override
			public void focusLost(FocusEvent fe) {
				mname = tf_MiddleName.getText();
			}
		});

		gbc_right.gridy = 2;
		add(tf_MiddleName, gbc_right);

		/*
		 * Alias Label and Text Box
		 */
		lbl_Alias = new JLabel("Alias");
		lbl_Alias.setToolTipText("Minimum Length = 0. Maximum length = 32. Alphabetic.");
		gbc_left.gridy = 3;
		add(lbl_Alias, gbc_left);

		tf_Alias = new JTextField("", 32);
		tf_Alias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				alias = tf_Alias.getText();
			}
		});

		tf_Alias.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				tf_Alias.selectAll();
			}

			@Override
			public void focusLost(FocusEvent fe) {
				alias = tf_Alias.getText();
			}
		});

		gbc_right.gridy = 3;
		add(tf_Alias, gbc_right);

		/*
		 * Title Label and Text Box
		 */
		lbl_Title = new JLabel("Title");
		lbl_Title.setToolTipText("Minimum Length = 0. Maximum length = 20. Alphabetic.");
		gbc_left.gridy = 4;
		add(lbl_Title, gbc_left);

		tf_Title = new JTextField("", 20);
		tf_Title.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				title = tf_Title.getText();
			}
		});

		tf_Title.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				tf_Title.selectAll();
			}

			@Override
			public void focusLost(FocusEvent fe) {
				title = tf_Title.getText();
			}
		});

		gbc_right.gridy = 4;
		add(tf_Title, gbc_right);
	}

	/**
	 * Return the contents of the First Name text field.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return fname;
	}

	/**
	 * Return the contents of the Last Name text field.
	 * 
	 * @return
	 */
	public String getLastName() {
		return lname;
	}

	/**
	 * Return the contents of the Middle Name text field.
	 * 
	 * @return
	 */
	public String getMiddleName() {
		return mname;
	}

	/**
	 * Return the contents of the Alias text field.
	 * 
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Return the contents of the Title text field.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}
}
