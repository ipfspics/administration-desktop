/*
 * Creates the window's content and manages most of it.
    Copyright (C) 2015 IpfsPics Team

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ipfs.pics.administration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.Desktop;
import java.net.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Administration {

	protected Shell shlIpfspicsAdministrationTool;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlIpfspicsAdministrationTool.open();
		shlIpfspicsAdministrationTool.layout();
		while (!shlIpfspicsAdministrationTool.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	//SQL Connection (TODO : Change this to another class) ===============================
	
	public int hashArrayIndex = 0;
	public ArrayList<String> hashArray = new ArrayList<String>();
	public boolean con = false; 
	public void tryConnectSelect(String url, String dbName, String driver, String userName, String password, String query) {
		try {
			
			//Tries to connect to the MySQL database dbName
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName, userName, password);
			
			//Statement st to select the correct hashes that haven't been backed up yet
			Statement st = conn.createStatement();
			
			//Executes wanted command
			ResultSet res = st.executeQuery(query);
			
			while (res.next()) {
				String hash = res.getString("hash");
				hashArray.add(hash);
			}
			
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tryConnectUpdate (String url, String dbName, String driver, String userName, String password, String query) {
		try {
		
			//Tries to connect to the MySQL database dbName
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName, userName, password);
		
			//Statement st to select the correct hashes that haven't been backed up yet
			Statement st = conn.createStatement();
		
			//Executes wanted command
			int val = st.executeUpdate(query);
			if (val == 0)
				System.out.println("Couldn't reach database, didn't update value");
			else if (val == 1) {
				System.out.println("Updated succesfully");
				con = true;
			}
				
		
			conn.close();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//SWT Window app =================================================
	
	public String hashURL = "http://ipfs.pics";
	public int arrayIndex = 0;
	private Text txtTest;
	private Text textNbOfHashes;
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlIpfspicsAdministrationTool = new Shell();
		shlIpfspicsAdministrationTool.setSize(1400, 875);
		shlIpfspicsAdministrationTool.setText("ipfs.pics Administration Tool");
		
		txtTest = new Text(shlIpfspicsAdministrationTool, SWT.BORDER);
		txtTest.setText("");
		txtTest.setBounds(465, 768, 306, 29);

		textNbOfHashes = new Text(shlIpfspicsAdministrationTool, SWT.BORDER);
		textNbOfHashes.setBounds(910, 768, 331, 29);

		//Menu Creator==================================
		
		Menu menu = new Menu(shlIpfspicsAdministrationTool, SWT.BAR);
		shlIpfspicsAdministrationTool.setMenuBar(menu);
		
		MenuItem mntmQuery = new MenuItem(menu, SWT.CASCADE);
		mntmQuery.setText("Query");
		
		Menu menu_1 = new Menu(mntmQuery);
		mntmQuery.setMenu(menu_1);
		
		MenuItem mntmRequestHashes = new MenuItem(menu_1, SWT.NONE);
		mntmRequestHashes.setText("Request Hashes");
		
		MenuItem mntmResetQuery = new MenuItem(menu_1, SWT.NONE);
		mntmResetQuery.setText("Reset Query");
		
		//Browser Creator==================================
		
		Browser browser = new Browser(shlIpfspicsAdministrationTool, SWT.NONE);
		browser.setUrl(hashURL);
		browser.setBounds(10, 10, 1364, 706);
		
		//CheckButton Creator==================================
		
		Button btnCheckButton = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
		btnCheckButton.setBounds(119, 757, 160, 22);
		btnCheckButton.setText("Not Administrated");
		
		Button btnCheckButton_1 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
		btnCheckButton_1.setBounds(119, 790, 160, 22);
		btnCheckButton_1.setText("All The Hashes");
		
		Button btnCheckButton_2 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
		btnCheckButton_2.setBounds(285, 757, 150, 22);
		btnCheckButton_2.setText("Only Banned Ones");
		
		Button btnCheckButton_3 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
		btnCheckButton_3.setBounds(285, 790, 150, 22);
		btnCheckButton_3.setText("Only NSFW Ones");
		
		//Button Creator==================================
		
		Button btnNewButtonPrevious = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonPrevious.setBounds(10, 722, 93, 29);
		btnNewButtonPrevious.setText("Previous");
		//TODO : Fix this button
		btnNewButtonPrevious.setEnabled(false);
		
		Button btnNewButtonNext = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonNext.setBounds(1281, 722, 93, 29);
		btnNewButtonNext.setText("Next");
		
		Button btnNewButtonSFW = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonSFW.setBounds(570, 722, 155, 29);
		btnNewButtonSFW.setText("SFW");
		
		Button btnNewButtonNSFW = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonNSFW.setBounds(404, 722, 160, 29);
		btnNewButtonNSFW.setText("NSFW");
		
		Button btnNewButtonBan = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonBan.setBounds(731, 722, 61, 29);
		btnNewButtonBan.setText("Ban");
		
		Button btnNewButtonCTC = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonCTC.setBounds(798, 722, 477, 29);
		btnNewButtonCTC.setText("Copy To Clipboard");
		
		Button btnNewButtonForget = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnNewButtonForget.setBounds(109, 722, 74, 29);
		btnNewButtonForget.setText("Forget");
		//TODO : Fix this button
		btnNewButtonForget.setEnabled(false);
		
		Button btnGetArraySize = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnGetArraySize.setBounds(785, 768, 119, 29);
		btnGetArraySize.setText("Get Array Size");
		
		Button btnGoogleImage = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
		btnGoogleImage.setBounds(189, 722, 209, 29);
		btnGoogleImage.setText("Google Image");
		
		//Disables all buttons (TODO : Optimize this - Other class/method) ============================
		btnNewButtonPrevious.setEnabled(false);
		btnNewButtonNext.setEnabled(false);
		btnNewButtonSFW.setEnabled(false);
		btnNewButtonNSFW.setEnabled(false);
		btnNewButtonBan.setEnabled(false);
		btnNewButtonCTC.setEnabled(false);
		btnNewButtonForget.setEnabled(false);
		btnGetArraySize.setEnabled(false);
		
		//Button Action Listener==================================
		
		btnNewButtonPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				arrayIndex--;
				hashURL = "http://ipfs.pics/" + hashArray.get(arrayIndex);
				browser.setUrl(hashURL);
			}
		});
		
		btnNewButtonNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btnNewButtonSFW.setEnabled(true);
				btnNewButtonNSFW.setEnabled(true);
				btnNewButtonBan.setEnabled(true);
				
				if (hashArray.size() == 0 || arrayIndex == hashArray.size()) {
					browser.setUrl("http://perdu.com");
				} else {
					hashURL = "http://ipfs.pics/" + hashArray.get(arrayIndex);
					browser.setUrl(hashURL);
				}
			}
			
		});
		
		btnNewButtonSFW.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryConnectUpdate("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
						"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "UPDATE hash_info SET sfw = 1, nsfw = 0, banned = 0 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				arrayIndex++;
				
				if (con == true) {
					btnNewButtonSFW.setEnabled(false);
					btnNewButtonNSFW.setEnabled(false);
					btnNewButtonBan.setEnabled(false);
				}
				
			}
		});
		
		btnNewButtonNSFW.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryConnectUpdate("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
						"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "UPDATE hash_info SET sfw = 0, nsfw = 1, banned = 0 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				arrayIndex++;
				
				if (con == true) {
					btnNewButtonSFW.setEnabled(false);
					btnNewButtonNSFW.setEnabled(false);
					btnNewButtonBan.setEnabled(false);
				}
			}
		});
		
		btnNewButtonBan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryConnectUpdate("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
						"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "UPDATE hash_info SET sfw = 0, nsfw = 0, banned = 1 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				arrayIndex++;
				
				if (con == true) {
					btnNewButtonSFW.setEnabled(false);
					btnNewButtonNSFW.setEnabled(false);
					btnNewButtonBan.setEnabled(false);
				}
			}
		});
		
		btnNewButtonCTC.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringSelection stringSelection = new StringSelection(hashURL);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		
		btnNewButtonForget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryConnectUpdate("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
						"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "DELETE FROM hash_info WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				hashArray.remove(arrayIndex);
				arrayIndex++;
			}
		});

		btnGetArraySize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hashArray.size() != 0) {
					String arraySize = String.valueOf(hashArray.size()); 
					textNbOfHashes.setText(arraySize);
				}
			}
		});
		
		btnGoogleImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!java.awt.Desktop.isDesktopSupported()) {
					System.out.println("No. >:(");
					System.exit(1);
				}
				
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				

				if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

		            System.err.println( "Desktop doesn't support the browse action (fatal)");
		            System.exit( 1 );
		        }
				
				try {

	                java.net.URI uri = new java.net.URI("https://images.google.com/searchbyimage?image_url=http://ipfs.pics/ipfs/" + hashArray.get(arrayIndex));
	                desktop.browse( uri );
	            }
	            catch (Exception e1) {

	                System.err.println( e1.getMessage() );
	            }
				
			}
		});
		
		//Menu bar Action Listener==================================

		mntmRequestHashes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (btnCheckButton.getSelection()) {
					txtTest.setText("Not Administrated Selected");
					
					//TODO : Optimize other class/method (line 212-355-372-387-408)
					//btnNewButtonPrevious.setEnabled(true);
					btnNewButtonNext.setEnabled(true);
					btnNewButtonSFW.setEnabled(true);
					btnNewButtonNSFW.setEnabled(true);
					btnNewButtonBan.setEnabled(true);
					btnNewButtonCTC.setEnabled(true);
					//btnNewButtonForget.setEnabled(true);
					btnGetArraySize.setEnabled(true);
					btnGoogleImage.setEnabled(true);

					tryConnectSelect("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
							"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "SELECT hash FROM hash_info WHERE sfw = 0 AND nsfw = 0 AND banned = 0");
					
					
				} else if (btnCheckButton_1.getSelection()) {
					txtTest.setText("All Of The Fuckers Selected");
					
					//btnNewButtonPrevious.setEnabled(true);
					btnNewButtonNext.setEnabled(true);
					btnNewButtonSFW.setEnabled(true);
					btnNewButtonNSFW.setEnabled(true);
					btnNewButtonBan.setEnabled(true);
					btnNewButtonCTC.setEnabled(true);
					//btnNewButtonForget.setEnabled(true);
					btnGetArraySize.setEnabled(true);
					btnGoogleImage.setEnabled(true);
					
					tryConnectSelect("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
							"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "SELECT hash FROM hash_info");
					
				} else if (btnCheckButton_2.getSelection()) {
					txtTest.setText("Banned Ones Selected");
					
					//btnNewButtonPrevious.setEnabled(true);
					btnNewButtonNext.setEnabled(true);
					btnNewButtonSFW.setEnabled(true);
					btnNewButtonNSFW.setEnabled(true);
					btnNewButtonBan.setEnabled(true);
					btnNewButtonCTC.setEnabled(true);
					//btnNewButtonForget.setEnabled(true);
					btnGetArraySize.setEnabled(true);
					btnGoogleImage.setEnabled(true);

					tryConnectSelect("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
							"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "SELECT hash FROM hash_info WHERE banned = 1");
					
				} else if (btnCheckButton_3.getSelection()) {
					txtTest.setText("NSFW Ones Selected");

					//btnNewButtonPrevious.setEnabled(true);
					btnNewButtonNext.setEnabled(true);
					btnNewButtonSFW.setEnabled(true);
					btnNewButtonNSFW.setEnabled(true);
					btnNewButtonBan.setEnabled(true);
					btnNewButtonCTC.setEnabled(true);
					//btnNewButtonForget.setEnabled(true);
					btnGetArraySize.setEnabled(true);
					btnGoogleImage.setEnabled(true);

					tryConnectSelect("jdbc:mysql://ipfs.pics:3306/", PrivateVariables.getDbName(),
							"com.mysql.jdbc.Driver", PrivateVariables.getDbUser(), PrivateVariables.getDbPswd(), "SELECT hash FROM hash_info WHERE nsfw = 1");
					
				} else {
					txtTest.setText("Please select what hashes you want.");
				}
			}
		});
		
		mntmResetQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnNewButtonPrevious.setEnabled(false);
				btnNewButtonNext.setEnabled(false);
				btnNewButtonSFW.setEnabled(false);
				btnNewButtonNSFW.setEnabled(false);
				btnNewButtonBan.setEnabled(false);
				btnNewButtonCTC.setEnabled(false);
				btnNewButtonForget.setEnabled(false);
				btnGetArraySize.setEnabled(false);
				hashArray.clear();
				hashURL = "http://ipfs.pics/";
				browser.setUrl(hashURL);
			}
		});
		
		//CheckButton Action Listener==================================
		

		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckButton_1.setSelection(false);
				btnCheckButton_2.setSelection(false);
				btnCheckButton_3.setSelection(false);
			}
		});
		

		btnCheckButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckButton.setSelection(false);
				btnCheckButton_2.setSelection(false);
				btnCheckButton_3.setSelection(false);
			}
		});
		

		btnCheckButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckButton_1.setSelection(false);
				btnCheckButton.setSelection(false);
				btnCheckButton_3.setSelection(false);
			}
		});
		

		btnCheckButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckButton_1.setSelection(false);
				btnCheckButton_2.setSelection(false);
				btnCheckButton.setSelection(false);
			}
		});
	}
	
	
	
}
