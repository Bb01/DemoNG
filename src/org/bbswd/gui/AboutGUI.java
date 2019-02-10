// Make a new change

package org.bbswd.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Display a modal dialog box with details of the application.
 * 
 * <p>
 * The details displayed include:
 * 
 * <ol>
 * <li>Copyright</li>
 * <li>Logo</li>
 * <li>URL to web site</li>
 * <li>Version</li>
 * </ol>
 * 
 */

public class AboutGUI extends JFrame {

	static final long serialVersionUID = 12345L;

	private static final Logger aboutGUILog = LogManager.getLogger(AboutGUI.class.getName());
	private JPanel contentPane;

	public static void main() {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AboutGUI frame = new AboutGUI();
					frame.setVisible(true);
					frame.setTitle("About");
					File f = new File("./rc/image-32x32.gif");
					if (f.exists() && !f.isDirectory()) {
						frame.setIconImage(new ImageIcon("./rc/image-32x32.gif").getImage());						
					} else {
						// TODO: should put in a proper runtime log/trace message here. This should never happen, the file is a hard-coded application resource. 
						System.out.println("Hello");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AboutGUI() {
		aboutGUILog.trace("AboutGUI() Entering...");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// TODO: how do we change from a start up location of 100,100 to something relative to the parent main window?
		setLocation(100, 100);
		setMinimumSize(new Dimension(300, 340));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 1 };
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{ 0.0 };
		gbl_contentPanel.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1};

		contentPane.setLayout(gbl_contentPanel);
		{
			JLabel lblCopyright = new JLabel("Copyright 2017 Brian B");
			GridBagConstraints gbc_lblCopyright = new GridBagConstraints();
			gbc_lblCopyright.insets = new Insets(0, 0, 5, 5);
			gbc_lblCopyright.gridx = 1;
			gbc_lblCopyright.gridy = 0;
			contentPane.add(lblCopyright, gbc_lblCopyright);
		}
		{
			JLabel lblLogo = new JLabel("");
			lblLogo.setIcon(new ImageIcon("./rc/image-160x185.gif"));
			GridBagConstraints gbc_lblLogo = new GridBagConstraints();
			gbc_lblLogo.insets = new Insets(0, 0, 5, 5);
			gbc_lblLogo.gridx = 1;
			gbc_lblLogo.gridy = 1;
			contentPane.add(lblLogo, gbc_lblLogo);
		}
		{
			JButton btnLink = new JButton("www.google.com");
			btnLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						Desktop dt = Desktop.getDesktop();
						URI uri = new URI("www.google.com");
						dt.browse(uri.resolve(uri));
					} catch (URISyntaxException e) {
						// TODO: Do something - This should never happen, the URI is a hardcoded application resource.
						// Should log the event.
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: Do something - this could happen if there was no network or site was down, etc.
						// Should log the event.
						e.printStackTrace();
					}
				}
			});
			GridBagConstraints gbc_btnLink = new GridBagConstraints();
			gbc_btnLink.insets = new Insets(0, 0, 5, 5);
			gbc_btnLink.gridx = 1;
			gbc_btnLink.gridy = 2;
			contentPane.add(btnLink, gbc_btnLink);
		}
		{
			JLabel lblVersion = new JLabel("Version 0.0a");
			GridBagConstraints gbc_lblVersion = new GridBagConstraints();
			gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
			gbc_lblVersion.gridx = 1;
			gbc_lblVersion.gridy = 3;
			contentPane.add(lblVersion, gbc_lblVersion);
		}
		{
			JButton okButton = new JButton("OK");
			GridBagConstraints gbc_okButton = new GridBagConstraints();
			gbc_okButton.insets = new Insets(0, 0, 5, 5);
			gbc_okButton.gridx = 1;
			gbc_okButton.gridy = 4;
			contentPane.add(okButton, gbc_okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AboutGUI.this.setVisible(false);
					AboutGUI.this.dispose();
				}
			});
			getRootPane().setDefaultButton(okButton);
		}
		aboutGUILog.trace("AboutGUI() Leaving...");
	}
}
