package org.bbswd.gui;

// import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.ComponentOrientation;
/**
 * @author Bb
 * @version v1.0a
 *
 */
public class MenuBar {

	static final long serialVersionUID = 12345L;

	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpenDB;
	private JMenuItem mntmExit;
	private JMenuItem mntmCloseDB;
	private JMenuItem mntmHelp;
	private JMenuItem mntmRefresh;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_4;
	private JMenu mnOptions;
	private JMenuItem mntmNewMenuItem;
	private JSeparator separator_5;
	private JMenuItem mntmImport;
	private JMenuItem mntmExport;
	
	private JMenu mnEdit;
	private JMenu mnView;
	
	private static final Logger menuBarLog = LogManager.getLogger(MenuBar.class.getName());
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
		
	public JMenuBar get() {
		return(menuBar);
	}
	
		public MenuBar() {

		menuBarLog.trace("MenuBar() Entering.");
		
	 	menuBar = new JMenuBar();		
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

if (1 == 0) {
		mntmNew = new JMenuItem("New...");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		
		separator_5 = new JSeparator();
		mnFile.add(separator_5);
		
		/*
		 * Open is intended to open a database.
		 * So the idea is that you redirect the application at a different database.
		 * It could be a different database with the same DB server.
		 * It could be a different database server completely.
		 * However, I think we can only be connected to one database at a time.
		 * So what to do if a user tries to open a database, while still connected to another?
		 * Any changes made in the GUI - should they be committed then to the database?
		 * Or are they lost?
		 */
		mntmOpenDB = new JMenuItem("OpenDB...");
		mntmOpenDB.setToolTipText("Open connection to a DB");
		mntmOpenDB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpenDB);
		mntmCloseDB = new JMenuItem("CloseDB");
		mntmCloseDB.setToolTipText("Close and flush connection with the database.");
		mntmCloseDB.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mntmCloseDB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnFile.add(mntmCloseDB);
		
		separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		mntmImport = new JMenuItem("Import....");
		mntmImport.setToolTipText("Import table contents from an external data respository/format (eg .csv)");
		mntmImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnFile.add(mntmImport);
		

		/*
		 * For this next part we need to do some specific things:
		 * + Add some logging (and remove the System.out.println()s
		 * + Add some proper exception handling - looking for IOExceptions etc and all that
		 * + Remove that hardcoded string and replace it with a symbolic constant (perhaps) or interrogate the JFileChooser further to get the absolute path.
		 *
		 * OK some thoughts here.
		 * We are importing data from an external source.
		 * We have no idea if the format of the records or the fields
		 * within the records contain good data.
		 * So (just) assuming the records are good how do we verify the field values?
		 * We want to store the values in a list in RAM.
		 * Its a list of Name objects.
		 */
//		mntmImport.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent ae) {
//				/*
//				 * Handle the Import menu button action
//				 */				
//				personGUILog.trace("Import():actionPerformed()");
//				if (ae.getSource() == mntmImport) {
//					
//					/*
//					 * Here we are trying to ensure that the FileChooser
//					 * is used to only go to the project's data directory
//					 * to operate on the import file.
//					 * Generally, we don't want people being able to read/write
//					 * to any part of the file system. This application is 
//					 * trying to confine them to a particular area/subtree.
//					 */
//					File root = new File("./data");
//					FileSystemView fsv= new SingleRootFileSystemView(root);
//					JFileChooser fc = new JFileChooser(fsv);					
//					
//					int returnVal = fc.showOpenDialog(MenuBar.this);
//					if (returnVal == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						String path = file.getAbsolutePath();
//
//						try {
//							InputStream inputStream = new FileInputStream(new File(path));
//							BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//							personGUILog.trace("Opening:" + path);
//							try {
//								String line = null;
//								String csvSplitBy = ",";
//								line = reader.readLine();
//								while (line != null) {
//									String[] field = line.split(csvSplitBy);
//
//									fname = field[0];
//									lname = field[1];
//									mname = field[2];
//									alias = field[3];
//									title = field[4];
//
//									personGUILog.trace("LineIn:" + line);
//									personGUILog.trace("LineOut:" + fname + lname + mname + alias + title);
//
//									/*
//									 * When importing data we are do a bulk read.
//									 * So what to do if there's an issue?
//									 * Do we through that record away (and move to the next)?
//									 * Or do we stop and return (after a cleanup)?
//									 * 
//									 * There's no way for the end-user to fix up a dodgey piece of data
//									 * when its being bulk read from a file. So option 2.
//									 */
//									Person person = new Person();
//									try {
//										person.setFirstName(fname);
//									} catch (IsNull | TooShort | Invalid e ) {
//										personGUILog.trace("Problem with fname:" + fname + ":");
//									}
//
//									try {
//										person.setLastName(lname);
//									} catch (IsNull | TooShort | Invalid e ) {
//										personGUILog.trace("Problem with lname:" + lname + ":");
//									}
//
//									try {
//										person.setMiddleName(mname);
//									} catch (IsNull | Invalid e ) {
//										personGUILog.trace("Problem with mname:" + mname + ":");
//									}
//
//									try {
//										person.setAlias(alias);
//									} catch (IsNull | Invalid e ) {
//										personGUILog.trace("Problem with alias:" + alias + ":");
//									}
//
//									try {
//										person.setTitle(title);
//									} catch (IsNull | Invalid e ) {
//										personGUILog.trace("Problem with lname:" + title + ":");
//									}
//
//									// OK - we seem to be good. Lets add the data to the internal list.
//									listOfPerson.add(person);
//									personGUILog.trace("list size:" + listOfPerson.size()+ ":");
//
//									// And now we need to add to the table in the GUI.
//									Object[] row = new Object[5];
//									row[0] = fname;
//									row[1] = lname;
//									row[2] = mname;
//									row[3] = alias;
//									row[4] = title;
//
//									tableModel.addRow(row);
//									table.repaint();
//
//									line = reader.readLine(); 
//								} // endwhile
//							} catch (Exception e) {
//								e.printStackTrace();
//							} finally {
//								reader.close();
//								inputStream.close();
//							} // endtry
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} // endif
//				}// endif
//			}
//		});
}

		
		mntmExport = new JMenuItem("Export...");
		mntmExport.setToolTipText("Export table contents to an external data respository/format (eg .csv)");
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmExport);

if (1 == 0) {
		/*
		 * So for this event we need to:
		 * + Create a JFileChooser object and point it at the default write location
		 * + If the file exists - do we ask to overwrite?
		 * + Open the file
		 * + Transfer records to file
		 * + Close the file
		 * + Close the JFileChooser object
		 */
//		mntmExport.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent ae) {
//				/*
//				 * Handle the Export menu button action
//				 */
//				personGUILog.trace("Export():actionPerformed()");
//				if (ae.getSource() == mntmExport) {
//					File root = new File("./data");
//					FileSystemView fsv= new SingleRootFileSystemView(root);
//					JFileChooser fc = new JFileChooser(fsv);					
//
//					int returnVal = fc.showOpenDialog(MenuBar.this);
//					if (returnVal == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						String path = file.getAbsolutePath();
//						StringBuffer sb = new StringBuffer();
//
//						personGUILog.trace("Opening:" + path + ":");
//
//						int rc = table.getRowCount();
//						personGUILog.trace("Row Count from table widget:" + rc + ":");
//
//						try {
//							OutputStream outputStream = new FileOutputStream(new File(path));
//							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//
//							try {
//								Person person;
//								for (int i=0; i<rc; i++) {
//									person = listOfPerson.get(i);
//									fname = person.getFirstName();
//									lname = person.getLastName();
//									mname = person.getMiddleName();
//									alias = person.getAlias();
//									title = person.getTitle();
//
//									personGUILog.trace("Record:" + fname + "," + lname + "," + mname + "," + alias + "," + title + ":");
//									sb.append(fname + "," + lname + "," + mname + "," + alias + "," + title + "\n");
//								}
//								writer.write(sb.toString()); 
//							} catch (Exception e) {
//								e.printStackTrace();
//							} finally { 
//								writer.close(); 
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		});

}

		mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.setToolTipText("Refresh this application with contents from the database.");
		mntmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnFile.add(mntmRefresh);
		
		separator_1 = new JSeparator();
		mnFile.add(separator_1);

		mntmExit = new JMenuItem("Exit");
		mntmExit.setToolTipText("Exit this application saving contents to the database.");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				menuBarLog.trace("Exit():actionPerformed()");
				/*
				 * Handle the Exit menu button action
				 */
				if (ae.getSource() == mntmExit) {
					System.exit(0);
				}
			}
		});
		
		mnEdit = new JMenu("Edit");
		mnEdit.setToolTipText("Edit data");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);
		
		mnView = new JMenu("View");
		mnView.setToolTipText("View data");
		mnView.setMnemonic('V');
		menuBar.add(mnView);

		mnOptions = new JMenu("Options");
		mnOptions.setToolTipText("View/configure application options");
		mnOptions.setMnemonic('O');
		menuBar.add(mnOptions);
		
		mntmNewMenuItem = new JMenuItem("Preferences...");
		mnOptions.add(mntmNewMenuItem);
		
		mnHelp = new JMenu("Help");
		mnHelp.setToolTipText("Get help on different aspects of the application");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		mntmHelp = new JMenuItem("Help...");
		mntmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmHelp);
		
		separator = new JSeparator();
		mnHelp.add(separator);
		
		/*
		 * In the about box I want info like the following to appear:
		 * + Logo for the application
		 * + Version number (V.R.M.F, Build #, Date)
		 * + Push button to a web site
		 * + Copyright claim
		 * + Credits
		 */
		mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				menuBarLog.trace("About():actionPerformed()");
				/*
				 * Handle the About menu button action
				 */
				if (ae.getSource() == mntmAbout) {
					AboutGUI nw = new AboutGUI();
					nw.setTitle("About");
					File f = new File("./rc/image-32x32.gif");
					if (f.exists() && !f.isDirectory()) {
						nw.setIconImage(new ImageIcon("./rc/image-32x32.gif").getImage());						
					} else {
						// TODO: should put in a proper runtime log/trace message here.
						System.out.println("Hello");
					}
					nw.setVisible(true);
				}
			}
		});
	}
}
