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
import org.eclipse.swt.widgets.Event;
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
import java.awt.Dimension;
import java.net.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Administration {

	protected Shell shlIpfspicsAdministrationTool = new Shell();


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
	
	SQLConnection dbConnection = new SQLConnection();
	
	//SWT Window app =================================================
	
	public String hashURL = "http://ipfs.pics";
	public int arrayIndex = 0;
	private Text txtTest;
	private Text textNbOfHashes;

	//Button Creator==================================
	
	Button btnNewButtonPrevious = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonNext = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonSFW = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonNSFW = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonBan = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonCTC = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnNewButtonForget = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnGetArraySize = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	Button btnGoogleImage = new Button(shlIpfspicsAdministrationTool, SWT.NONE);
	
	//CheckButton Creator==================================

	Button btnCheckButton = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
	Button btnCheckButton_1 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
	Button btnCheckButton_2 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
	Button btnCheckButton_3 = new Button(shlIpfspicsAdministrationTool, SWT.CHECK);
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		shlIpfspicsAdministrationTool.setSize((int)(width - (width/10)), (int)(height - (height/10)));
		shlIpfspicsAdministrationTool.setText("ipfs.pics Administration Tool");
		
		shlIpfspicsAdministrationTool.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				dbConnection.close();
			}
		});

		dbConnection.connect();
		
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
		browser.setBounds(10, 10, (int)(width - ((width/8))), (int)(height - ((height/10)*2)));
		
		//Check buttons display initializer===============================
		
		btnCheckButton.setBounds(119, 757, 160, 22);
		btnCheckButton.setText("Not Administrated");
		
		btnCheckButton_1.setBounds(119, 790, 160, 22);
		btnCheckButton_1.setText("All The Hashes");
		
		btnCheckButton_2.setBounds(285, 757, 150, 22);
		btnCheckButton_2.setText("Only Banned Ones");
		
		btnCheckButton_3.setBounds(285, 790, 150, 22);
		btnCheckButton_3.setText("Only NSFW Ones");
		
		//Button display initalizing======================================
		
		btnNewButtonPrevious.setBounds(10, 722, 93, 29);
		btnNewButtonPrevious.setText("Previous");
		//TODO : Fix this button
		btnNewButtonPrevious.setEnabled(false);
		
		btnNewButtonNext.setBounds(1281, 722, 93, 29);
		btnNewButtonNext.setText("Next");
		
		btnNewButtonSFW.setBounds(570, 722, 155, 29);
		btnNewButtonSFW.setText("SFW");
		
		btnNewButtonNSFW.setBounds(404, 722, 160, 29);
		btnNewButtonNSFW.setText("NSFW");
		
		btnNewButtonBan.setBounds(731, 722, 61, 29);
		btnNewButtonBan.setText("Ban");
		
		btnNewButtonCTC.setBounds(798, 722, 477, 29);
		btnNewButtonCTC.setText("Copy To Clipboard");
		
		btnNewButtonForget.setBounds(109, 722, 74, 29);
		btnNewButtonForget.setText("Forget");
		//TODO : Fix this button
		btnNewButtonForget.setEnabled(false);
		
		btnGetArraySize.setBounds(785, 768, 119, 29);
		btnGetArraySize.setText("Get Array Size");
		
		btnGoogleImage.setBounds(189, 722, 209, 29);
		btnGoogleImage.setText("Google Image");
		
		disableAllButtons();
		
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

				arrayIndex++;
				
				if (hashArray.size() == 0 || arrayIndex == hashArray.size()) {
					browser.setUrl("http://perdu.com");
				} else if(btnCheckButton_2.getSelection()) {
					hashURL = "http://ipfs.pics/ipfs/" + hashArray.get(arrayIndex);
					browser.setUrl(hashURL);
				} else {
					hashURL = "http://ipfs.pics/" + hashArray.get(arrayIndex);
					browser.setUrl(hashURL);
				}
			}
		});
		
		btnNewButtonSFW.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				dbConnection.queryUpdate("UPDATE hash_info SET sfw = 1, nsfw = 0, banned = 0 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				
				if (dbConnection.getUpdated() == true) {
					btnNewButtonSFW.setEnabled(false);
				}
				
			}
		});
		
		btnNewButtonNSFW.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				dbConnection.queryUpdate("UPDATE hash_info SET sfw = 0, nsfw = 1, banned = 0 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				
				if (dbConnection.getUpdated() == true) {
					btnNewButtonNSFW.setEnabled(false);
				}
			}
		});
		
		btnNewButtonBan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				dbConnection.queryUpdate("UPDATE hash_info SET sfw = 0, nsfw = 0, banned = 1 WHERE hash = '" + hashArray.get(arrayIndex) + "'");
				
				if (dbConnection.getUpdated() == true) {
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
				
				hashArray.remove(arrayIndex);
			}
		});

		btnGetArraySize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String arraySize = String.valueOf(hashArray.size()); 
				textNbOfHashes.setText(arraySize);
			}
		});
		
		btnGoogleImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String OS = System.getProperty("os.name").toLowerCase();
				
				if (OS.indexOf("win") >= 0) {
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
				
			}
		});
		
		//Menu bar Action Listener==================================

		mntmRequestHashes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (btnCheckButton.getSelection()) {
					txtTest.setText("Not Administrated Selected");
					
					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE sfw = 0 AND nsfw = 0 AND banned = 0", hashArray);
					
					arrayIndex = -1;
					
				} else if (btnCheckButton_1.getSelection()) {
					txtTest.setText("All Hashes Selected");
					
					enableAllButtons();
					
					dbConnection.querySelect("SELECT * FROM hash_info", hashArray);
					
				} else if (btnCheckButton_2.getSelection()) {
					txtTest.setText("Banned Ones Selected");
					
					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE banned = 1", hashArray);
					
				} else if (btnCheckButton_3.getSelection()) {
					txtTest.setText("NSFW Ones Selected");

					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE nsfw = 1", hashArray);
					
				} else {
					txtTest.setText("Please select what hashes you want.");
				}
			}
		});
		
		mntmResetQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableAllButtons();
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
	
	/**
	 * Enables all the buttons
	 */
	public void enableAllButtons() {
		btnNewButtonPrevious.setEnabled(true);
		btnNewButtonNext.setEnabled(true);
		btnNewButtonSFW.setEnabled(true);
		btnNewButtonNSFW.setEnabled(true);
		btnNewButtonBan.setEnabled(true);
		btnNewButtonCTC.setEnabled(true);
		//btnNewButtonForget.setEnabled(true);
		btnGetArraySize.setEnabled(true);
	}
	
	/**
	 * Disables all the buttons
	 */
	public void disableAllButtons() {
		btnNewButtonPrevious.setEnabled(false);
		btnNewButtonNext.setEnabled(false);
		btnNewButtonSFW.setEnabled(false);
		btnNewButtonNSFW.setEnabled(false);
		btnNewButtonBan.setEnabled(false);
		btnNewButtonCTC.setEnabled(false);
		btnNewButtonForget.setEnabled(false);
		btnGetArraySize.setEnabled(false);
	}
	
}
