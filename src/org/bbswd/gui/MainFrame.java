package org.bbswd.gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import java.awt.GridBagConstraints;

import java.awt.Insets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Bb
 * @version v1.0a
 * 
 * This is what we are trying to build:
 * <p><img src="doc-files/MainFrameGUIScreenLayout.png" alt="MainFrameGUIScreenLayout">
 * <p>Hope you like the images.
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 12345L;
	
	/*
	 * For the config.properties to be picked up at runtime, the resources
	 * folder (where its stored along with other application resources), must be in 
	 * classpath at runtime.
	 * eclipse/project/properties/Java Build Path/Libraries/Add Class Path.../
	 */
	private String resources = "config.properties";
	private InputStream inputS = MainFrame.class.getClassLoader().getResourceAsStream(resources);
	Properties props = new Properties();

	private JPanel mainPanel;
	private ToolBarPanel toolBarPanel;
	private PersonPanel personPanel;

	private MenuBar menuBar;

	private static final Logger mainFrameLog = LogManager.getLogger(MainFrame.class.getName());

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            An array of String with arguments passed in from
	 *            the JVM.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the main frame for the application window.
	 */
	public MainFrame() {
		mainFrameLog.trace("MainFrame() Entering.");

		if (inputS == null) {
			System.out.println("Unable to locate application resource file: config.properties");
			return;
		}
		try {
			props.load(inputS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(props.getProperty("jdbc.url"));
		System.out.println(props.getProperty("jdbc.username"));
		System.out.println(props.getProperty("jdbc.password"));
		System.out.println(props.getProperty("title"));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Application Main Window");
		setLocation(100, 100);
		setMinimumSize(new Dimension(450, 500));

		/*
		 * Change icon from (default) Java Coffee Cup to custom value. Check if
		 * the file exists. If it exists then open it an apply to the frame.
		 */
		File f = new File("./resources/image-32x32.gif"); // TODO: switch to resource reference
		if (f.exists() && !f.isDirectory()) {
			setIconImage(new ImageIcon("./resources/image-32x32.gif").getImage());
		}

		menuBar = new MenuBar();
		setJMenuBar(menuBar.get());

		/*
		 * The mainPanel is going to contain (from top to bottom):
		 * + Toolbar
		 * + A panel to allow data to be entered (with labels, text fields and OK/Cancel)
		 * + A panel to display entered data (a scrolled table)
		 */
		mainPanel = new JPanel();
		setContentPane(mainPanel);

		mainPanel.setBorder(BorderFactory.createTitledBorder("MainPanel"));

		/*
		 * Each container component is managed by a GridBagLayout. Each widget
		 * within each container component will have their own associated
		 * GridBagConstraints object.
		 */
		GridBagLayout gbl = new GridBagLayout();

		/*
		 * Set the overrides to the column minimum widths. If this field is
		 * non-null the values are applied to the gridbag after all of the
		 * minimum columns widths have been calculated. If columnWidths has more
		 * elements than the number of columns, columns are added to the gridbag
		 * to match the number of elements in columnWidth.
		 */
		gbl.columnWidths = new int[] { 1 };

		/*
		 * Set the overrides to the row minimum heights. If this field is
		 * non-null the values are applied to the gridbag after all of the
		 * minimum row heights have been calculated. If rowHeights has more
		 * elements than the number of rows, rows are added to the gridbag to
		 * match the number of elements in rowHeights.
		 * 
		 * Row 0 - Object Toolbar Row 1 - Object Tabbed Panel
		 */
		gbl.rowHeights = new int[] { 0, 0 };

		/*
		 * Set the overrides to the column weights. If this field is non-null
		 * the values are applied to the gridbag after all of the columns
		 * weights have been calculated. If columnWeights[i] > weight for column
		 * i, then column i is assigned the weight in columnWeights[i]. If
		 * columnWeights has more elements than the number of columns, the
		 * excess elements are ignored - they do not cause more columns to be
		 * created.
		 */
		gbl.columnWeights = new double[] { 0.0 };

		/*
		 * Set the overrides to the row weights. If this field is non-null the
		 * values are applied to the gridbag after all of the rows weights have
		 * been calculated. If rowWeights[i] > weight for row i, then row i is
		 * assigned the weight in rowWeights[i]. If rowWeights has more elements
		 * than the number of rows, the excess elements are ignored - they do
		 * not cause more rows to be created.
		 */
		gbl.rowWeights = new double[] { 0.0, 1.0 };

		/*
		 * Apply the gbl (GridBagLayout) layout to (this) mainPanel.
		 */
		setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = (double) 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		toolBarPanel = new ToolBarPanel();
		mainPanel.add(toolBarPanel, gbc);

		/*
		 * Whatever is placed here, in the mainPanel, is driven by what type of
		 * object is selected in the Toolbar. In the future, the default object
		 * should be displayed. For now, its a "Person" object that the user
		 * sees/operates on by default.
		 */
		gbc.gridx = 0;
		gbc.gridy = 1;
		personPanel = new PersonPanel();
		mainPanel.add(personPanel, gbc);

		pack();
		setVisible(true);

		mainFrameLog.trace("MainFrame() Leaving normally.");
	}
}