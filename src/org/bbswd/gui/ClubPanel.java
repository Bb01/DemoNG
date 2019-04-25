package org.bbswd.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.bbswd.model.Database;
import org.bbswd.model.Club;

/**
 * A ClubPanel object is used to work on Club Objects.
 * This is just a placeholder at.
 * 
 * In the future, It contains 2 x basic areas:
 * <ol>
 * <li>A "gather info" panel at the top to allow a user enter data (typically
 * containing a tabbed pane (with Add, Change and Delete). Each tab contains a
 * collection of and some sort of OK/Cancel component).</li>
 * <li>A "display info" panel at the bottom for a user to see the data on
 * file/entered (typically a scrolled table).</li>
 * </ol>
 * 
 * Inside the top pane, there are labels and text widgets that allow a user to
 * specify values for Club attributes (eg name, etc). This data entry
 * area is in a scrolled window so that new Club attributes can be added in
 * the future without having to do a re-layout of the parent panel. It also
 * contains an OK/Cancel pair at the bottom.
 * 
 * The ClubPanel object deals with:
 * 
 * <ol>
 * <li>Accepting info needed to Add a new Club from the end-user via the GUI
 * </li>
 * <li>Dispatching that information to a validation routine</li>
 * <li>If the validation is good then</li>
 * <li>
 * <ol>
 * <li>ensuring that the data is written to the back end database</li>
 * <li>writing the same data to an in-memory cache and</li>
 * <li>ensuring that the data in the ClubDataDisplayPanel is updated in such a
 * way that the new record is visible</li>
 * </ol>
 * </li>
 * <li>If the validation is bad then</li>
 * <li>
 * <ol>
 * <li>ensuring that appropriate feedback is given to the end-user via GUI</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author brian
 *
 */
public class ClubPanel extends JPanel {

	static final long serialVersionUID = 12345L;

	private ClubAddDataGatherPanel clubAddDataGatherPanel;
	private ClubDataDisplayPanel clubDataDisplayPanel;
	private Database database;

	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	
	public ClubPanel() {

		try {
			database = new Database();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			database.loadClubData();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 1 };
		gbl.rowHeights = new int[] { 0, 0, 0 };
		gbl.columnWeights = new double[] { 0.0 };
		gbl.rowWeights = new double[] { 0.0, 0.5, 0.5 };
		setLayout(gbl);

		setBorder(BorderFactory.createTitledBorder("ClubPanel"));

		/*
		 * TODO: Need proper "Club" panels!
		 */
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = (double) 1.0;
		gbc.weighty = (double) 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		clubAddDataGatherPanel = new ClubAddDataGatherPanel(database);
		add(clubAddDataGatherPanel, gbc);

		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		clubDataDisplayPanel = new ClubDataDisplayPanel(database);
		add(clubDataDisplayPanel, gbc);

		clubAddDataGatherPanel.setClubAddFormListener(new ClubAddFormListener() {
			public void clubAddFormEventOccurred(ClubAddFormEvent e) {
				Club club = e.getClub();
				clubDataDisplayPanel.addClubToTable(club);
			}
		});

		clubDataDisplayPanel.setClubTableListener(new ClubTableListener() {
			public void clubTableEventOccurred(ClubTableEvent e) {
				Club club = e.getClub();
				System.out.println("Here's the club that was selected:" + club);
			}
		});
	}
}
