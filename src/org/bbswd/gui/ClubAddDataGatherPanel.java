package org.bbswd.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.bbswd.controller.Controller;
import org.bbswd.model.Database;
import org.bbswd.model.Club;

/**
 * A simple GUI element composed of 2 basic widgets:
 * 
 * <ul>
 * <li>The top part is a scrolled data entry component composed of labels and
 * data entry widgets (JTextFields mostly, Check Boxes, Drop Downs)</li>
 * <li>The bottom part is an OK/Submit Cancel/reset component. This is used to
 * formally submit the data entered in the top panel for verification and
 * storage in the application.</li>
 * </ul>
 * 
 * The data entered in the top widget is validated and then inserted into the
 * database. If the validation and insertion proceed without incident, then the
 * last thing to be done is that the parent is notified that the data has been
 * gathered, validated and added to the database. This is handled using a form
 * listener. That is, a listener (usually provided by the parent, in this case
 * the "ClubPanel") is invoked.
 * 
 * @author brian
 *
 */
public class ClubAddDataGatherPanel extends JPanel implements ActionListener {

	static final long serialVersionUID = 12345L;

	private JTabbedPane clubTabbedPane;
	private JPanel clubAddPanel;
	private JPanel clubChangePanel;
	private JPanel clubDeletePanel;
	private ClubAddDataEntryPanel clubAddDataEntryPanel;
	private ClubOKResetPanel oKResetPanel;

	private GridBagLayout gbl;
	private GridBagConstraints gbc;

	private ClubAddDataListener clubAddDataListener;
	private ClubAddResetListener clubAddResetListener;
	private Club club;
	private ClubAddFormListener clubAddFormListener;

	public ClubAddDataGatherPanel(Database database) {

		setBorder(BorderFactory.createTitledBorder("ClubDataGatherPanel"));

		gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 0 };
		gbl.rowHeights = new int[] { 0, 0 };
		gbl.columnWeights = new double[] { 1.0 };
		gbl.rowWeights = new double[] { 1.0, 0.0 };
		setLayout(gbl);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = (double) 1.0;
		gbc.weighty = (double) 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		clubTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(clubTabbedPane, gbc);

		clubAddPanel = new JPanel();
		clubTabbedPane.addTab("Add", null, clubAddPanel, null);
		clubChangePanel = new JPanel();
		clubTabbedPane.addTab("Change", null, clubChangePanel, null);
		clubDeletePanel = new JPanel();
		clubTabbedPane.addTab("Delete", null, clubDeletePanel, null);

		GridBagLayout gbla = new GridBagLayout();
		gbla.columnWidths = new int[] { 1 };
		gbla.rowHeights = new int[] { 0 };
		gbla.columnWeights = new double[] { 1.0 };
		gbla.rowWeights = new double[] { 0.0 };
		clubAddPanel.setLayout(gbla);

		gbc.weightx = (double) 1.0;
		gbc.weighty = (double) 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		clubAddDataEntryPanel = new ClubAddDataEntryPanel();
		JScrollPane scrollPane = new JScrollPane(clubAddDataEntryPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		clubAddPanel.add(scrollPane, gbc);

		setClubAddDataListener(new ClubAddDataListener() {
			private String fname;
			private String lname;
			private String mname;
			private String alias;
			private String title;

			/*
			 * This method is called when the user enters data about a new
			 * club and then selects some sort of OK/Submit/Confirm action.
			 * 
			 * It will fetch the data entered (it knows the data is within the
			 * ClubDataEntryPanel object) and then validates each one in turn.
			 * If all is good, the data is assembled into a "Club" object and
			 * added to the database.
			 * 
			 * (non-Javadoc)
			 * 
			 * @see org.bbswd.gui.ClubAddDataListener#clubAddDataEntered()
			 */
			public void clubAddDataEntered() {

				/**
				 * At this point I think what should happen is:
				 * 
				 * <ol>
				 * <li>Validate the Club returned</li>
				 * <li>If the club is valid, add to a global list.</li>
				 * <li>If the club is valid, add to the display panel.</li>
				 * <li>If the club is valid, maybe center/highlight the club
				 * added in the display panel.</li>
				 * <li>If the club is invalid, display (a series of?) dialog
				 * box(es) showing reasons why its invalid.</li>
				 * </ol>
				 */

				ArrayList<String> issueList = new ArrayList<String>();
				StringBuffer sb_fname = new StringBuffer("");
				StringBuffer sb_lname = new StringBuffer("");
				StringBuffer sb_mname = new StringBuffer("");
				StringBuffer sb_alias = new StringBuffer("");
				StringBuffer sb_title = new StringBuffer("");
				Controller controller = new Controller();
				boolean result = false;
				boolean allGood = true;

				fname = clubAddDataEntryPanel.getFirstName();
				result = controller.constraintCheck(Controller.FIRSTNAME_OPTIONAL, Controller.MIN_FIRSTNAME_LEN,
						Controller.MAX_FIRSTNAME_LEN, Controller.FIRSTNAME_REGEX, fname, sb_fname, issueList);
				if (result == false) {
					allGood = false;
				}
				handleResult("First Name", fname, issueList);

				lname = clubAddDataEntryPanel.getLastName();
				result = controller.constraintCheck(Controller.LASTNAME_OPTIONAL, Controller.MIN_LASTNAME_LEN,
						Controller.MAX_LASTNAME_LEN, Controller.LASTNAME_REGEX, lname, sb_lname, issueList);
				if (result == false) {
					allGood = false;
				}
				handleResult("Last Name", lname, issueList);

				mname = clubAddDataEntryPanel.getMiddleName();
				result = controller.constraintCheck(Controller.MIDDLENAME_OPTIONAL, Controller.MIN_MIDDLENAME_LEN,
						Controller.MAX_MIDDLENAME_LEN, Controller.MIDDLENAME_REGEX, mname, sb_mname, issueList);
				if (result == false) {
					allGood = false;
				}
				handleResult("Middle Name", mname, issueList);

				alias = clubAddDataEntryPanel.getAlias();
				result = controller.constraintCheck(Controller.ALIAS_OPTIONAL, Controller.MIN_ALIAS_LEN,
						Controller.MAX_ALIAS_LEN, Controller.ALIAS_REGEX, alias, sb_alias, issueList);
				if (result == false) {
					allGood = false;
				}
				handleResult("Alias", alias, issueList);

				title = clubAddDataEntryPanel.getTitle();
				result = controller.constraintCheck(Controller.TITLE_OPTIONAL, Controller.MIN_TITLE_LEN,
						Controller.MAX_TITLE_LEN, Controller.TITLE_REGEX, title, sb_title, issueList);
				if (result == false) {
					allGood = false;
				}
				handleResult("Title", title, issueList);

				if (!allGood) {
					return;
				}

				/*
				 * OK - we seem to be good. Lets add the data to the "database".
				 * This must be done, before the UI is updated because there
				 * could still be some data constraint/validation/integrity
				 * issues with the user data.
				 */
				club = new Club(sb_fname.toString(), sb_lname.toString(), sb_mname.toString(), sb_alias.toString(),
						sb_title.toString());

				int status = 0;
				try {
					database.connect();
					status = database.addClub(club);
				} catch (Exception e) {
					/*
					 * There was a problem.
					 * 
					 * The Club object was not added to the list. We need to
					 * display an Error Dialog and we don't update UI/Jtable.
					 */
					JOptionPane.showMessageDialog(null, "Could not add Club to database", "Error Message",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
				}
				
				if (status != 0 )
					return;

				/*
				 * At this point the data entered looks good/clean. Its been
				 * added to the backend database.
				 * 
				 * It should now be displayed in the DisplayTable.
				 * 
				 * A null event is created and passed to the actionPerformed
				 * method. This is just to transfer control to that method and
				 * allow it to do what it needs to do.
				 * 
				 */
				ActionEvent e = null;
				actionPerformed(e);
			}

			/**
			 * When a user supplied attribute values is tested against some
			 * constraints, the constraint tests could fail. There could be
			 * several reasons for the failure. Any issues with the user
			 * supplied values are recorded in a list that is processed in this
			 * method. Currently, this means that a dialog box is displayed for
			 * each issue recorded for each attribute.
			 * 
			 * @param attName
			 *            The name of attribute (as it appears in the UI) being
			 *            processed.
			 * @param attVal
			 *            The value of the attribute being processed.
			 * @param issueList
			 *            An ArrayList of <String> with the issues to be
			 *            processed.
			 */
			void handleResult(String attName, String attVal, ArrayList<String> issueList) {
				if (issueList.size() == 0) {
					/*
					 * Clean run! No issues.
					 */
				} else {
					/*
					 * At least one issue arose. It could be one or more
					 * warnings or errors. Just iterate through the list of
					 * issues, then purge the list.
					 */
					int size = issueList.size();
					for (int i = 0; i < size; i++) {
						if ((issueList.get(i)).contains("WARNING")) {
							JOptionPane.showMessageDialog(null, issueList.get(i),
									"Warning in " + attName + ": " + attVal, JOptionPane.WARNING_MESSAGE);
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
		});

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = (double) 0.0;
		oKResetPanel = new ClubOKResetPanel(clubAddDataListener);
		clubAddPanel.add(oKResetPanel, gbc);
	}

	private void setClubAddDataListener(ClubAddDataListener clubAddListener) {
		this.clubAddDataListener = clubAddListener;
	}

	private void setClubAddResetListener(ClubAddResetListener clubAddListener) {
		this.clubAddResetListener = clubAddListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ClubAddFormEvent ev = new ClubAddFormEvent(this, club);
		if (clubAddFormListener != null) {
			clubAddFormListener.clubAddFormEventOccurred(ev);
		}
	}

	public void setClubAddFormListener(ClubAddFormListener formListener) {
		this.clubAddFormListener = formListener;
	}
}