package org.bbswd.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TableView.TableCell;

import org.bbswd.model.Database;
import org.bbswd.model.Club;

/**
 * This is a GUI object used to display Person objects in tabular form. Its a
 * panel that contains a scrollable table. The table has the headers "id",
 * "fname", "lname", "mname", "alias" & "title".
 * 
 * The panel has horizontal and vertical scroll bars.
 */
public class ClubDataDisplayPanel extends JPanel {

	static final long serialVersionUID = 12345L;

	private final String[] columnNames = { "id", "fname", "lname", "mname", "alias", "title" };
	private JScrollPane scrollPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private static int rowCount = 0;

	private ClubTableListener clubTableListener;
	// private String[][] data = null;

	/**
	 * Constructor. Create the GUI elements of the display panel and link them
	 * together. Then populate them with the Person data from the database.
	 * 
	 * @param database
	 *            An object that allows data to be fetched from the database.
	 */
	public ClubDataDisplayPanel(Database database) {

		gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 1 };
		gbl.rowHeights = new int[] { 0 };
		gbl.columnWeights = new double[] { 1.0 };
		gbl.rowWeights = new double[] { 0.0 };
		setLayout(gbl);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = (double) 1.0;
		gbc.weighty = (double) 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		setBorder(BorderFactory.createTitledBorder("ClubDataDisplayPanel"));

		/*
		 * Putting the table into a scroll pane container.
		 */
		table = new JTable();
		tableModel = new DefaultTableModel(null, columnNames);
		table.setModel(tableModel);
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setPreferredScrollableViewportSize(new Dimension(450, 200));
		table.setFillsViewportHeight(true);

		table.getSelectionModel().addListSelectionListener(new RowListener());

		add(scrollPane, gbc);

		/*
		 * Let's populate the table model with rows of data from the in-memory
		 * list of clubs.
		 */
		ArrayList<Club> cl = database.getClubList();
		Club club;
		int cl_size = cl.size();
		for (int i = 0; i < cl_size; i++) {
			club = cl.get(i);
			this.addClubToTable(club);
		}
	}

	/**
	 * Adds a club as a new row in the table model. This method controls which
	 * attributes of a club get displayed, as not every attribute of "club"
	 * will be displayed.
	 * 
	 * @param club
	 *            An object that contains all the in-memory data of the club
	 *            to be displayed.
	 */
	public void addClubToTable(Club club) {
		Object[] row = new Object[6];
		row[0] = club.getID();
		row[1] = club.getFirstName();
		row[2] = club.getLastName();
		row[3] = club.getMiddleName();
		row[4] = club.getAlias();
		row[5] = club.getTitle();

		rowCount++;

		tableModel.addRow(row);
	}

	/**
	 * Method to return the number of rows in the application in-memory list of
	 * Persons.
	 * 
	 * @return int containing the value of the number of rows.
	 */
	public int getRowCount() {
		return rowCount;
	}

	public void setClubTableListener(ClubTableListener listener) {
		this.clubTableListener = listener;
	}

	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}

			/*
			 * Somehow I need to construct a club object from the info in the
			 * table row.
			 * 
			 * However, we must not forget that all the information about a
			 * club may not be displayed in the table. The index (Column 0) is
			 * unique. In theory, that should be all that is needed.
			 */
			Club club = new Club(table.getValueAt(table.getSelectedRow(), 1).toString(),
					table.getValueAt(table.getSelectedRow(), 2).toString(),
					table.getValueAt(table.getSelectedRow(), 3).toString(),
					table.getValueAt(table.getSelectedRow(), 4).toString(),
					table.getValueAt(table.getSelectedRow(), 5).toString());
			// TODO: I need to look at this carefully - setting Id on a Person
			// object is not what I want to be doing - temporary hack
			// orig club.setId(table.getValueAt(table.getSelectedRow(),
			// 0).toString());
			club.setId((int) table.getValueAt(table.getSelectedRow(), 0));
			ClubTableEvent ev = new ClubTableEvent(this, club);
			if (clubTableListener != null) {
				clubTableListener.clubTableEventOccurred(ev);
			}
		}
	}

	/**
	 * Delete a club from the display table. The display table does not know
	 * anything about club records stored in a database, etc.
	 * 
	 * TODO: This is very crude, I know. It could be a lot more elegant.
	 * 
	 * @param club
	 *            The club to b deleted from the table
	 */
	public void deleteClubFromTable(Club club) {
		System.out.println("Going to delete this club:" + club);

		int id;
		id = club.getID();

		for (int row = 0; row < rowCount; row++) {
			// orig if (id.equals((String) table.getValueAt(row, 0))) { I
			// changed the piece below when changing Id from String to int.
			if (id == ((int) table.getValueAt(row, 0))) {
				((DefaultTableModel) table.getModel()).removeRow(row);
			}
		}

		rowCount--;
	}

	/**
	 * Change the value of a club in the display table. The club ID, which
	 * is stored in the club object cannot be changed. The club ID is used
	 * to locate the record to be updated in the display table. The other values
	 * in the club object are used to update the other columns.
	 * 
	 * @param club
	 *            The club to be changed in the table and the value its to be
	 *            changed to.
	 */
	public void changeClubInTable(Club club) {
		System.out.println("Going to change the club to:" + club);

		int id;
		id = club.getID();

		for (int row = 0; row < rowCount; row++) {
			// orig if (id.equals((String) table.getValueAt(row, 0))) {
			if (id == ((int) table.getValueAt(row, 0))) {
				table.setValueAt(club.getFirstName(), row, 1);
				table.setValueAt(club.getLastName(), row, 2);
				table.setValueAt(club.getMiddleName(), row, 3);
				table.setValueAt(club.getAlias(), row, 4);
				table.setValueAt(club.getTitle(), row, 5);
			}
		}
	}
}
