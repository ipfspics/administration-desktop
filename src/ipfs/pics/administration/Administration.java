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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

public class Administration {

	protected Shell adminTool = new Shell();
	Display display = Display.getDefault();


	/**
	 * Open the window.
	 */
	public void open() {
		createContents();
		adminTool.setImage(new Image(display, "C:\\Users\\GoldMember\\Documents"));
		adminTool.open();
		adminTool.layout();
		while (!adminTool.isDisposed()) {
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
	private Text textNbOfHashes;
	Image appIcon = new Image(display, "");

	//Button Creator==================================
	
	Button btnNewButtonPrevious = new Button(adminTool, SWT.NONE);
	Button btnNewButtonNext = new Button(adminTool, SWT.NONE);
	Button btnNewButtonSFW = new Button(adminTool, SWT.NONE);
	Button btnNewButtonNSFW = new Button(adminTool, SWT.NONE);
	Button btnNewButtonBan = new Button(adminTool, SWT.NONE);
	Button btnNewButtonCTC = new Button(adminTool, SWT.NONE);
	Button btnNewButtonForget = new Button(adminTool, SWT.NONE);
	Button btnGoogleImage = new Button(adminTool, SWT.NONE);
	
	//CheckButton Creator==================================

	Button btnCheckButton = new Button(adminTool, SWT.CHECK);
	Button btnCheckButton_1 = new Button(adminTool, SWT.CHECK);
	Button btnCheckButton_2 = new Button(adminTool, SWT.CHECK);
	Button btnCheckButton_3 = new Button(adminTool, SWT.CHECK);
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		int windowWidth = (int) (width - (width/10));
		int windowHeight = (int)(height - (height/10));
		int topBarSize = 59;
		
		adminTool.setSize(windowWidth, windowHeight);
		adminTool.setText("ipfs.pics Administration Tool");
		
		adminTool.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				dbConnection.close();
			}
		});

		dbConnection.connect();

		textNbOfHashes = new Text(adminTool, SWT.BORDER);
		textNbOfHashes.setBounds((10 + ((((windowWidth - 16) - 20)/8)*7)), (((windowHeight - topBarSize) - 100) + 29) + 20, ((windowWidth - 16) - 20)/8, 22);
		textNbOfHashes.setEditable(false);

		//Menu Creator==================================
		
		Menu menu = new Menu(adminTool, SWT.BAR);
		adminTool.setMenuBar(menu);
		
		MenuItem mntmQuery = new MenuItem(menu, SWT.CASCADE);
		mntmQuery.setText("Query");
		
		Menu menu_1 = new Menu(mntmQuery);
		mntmQuery.setMenu(menu_1);
		
		MenuItem mntmRequestHashes = new MenuItem(menu_1, SWT.NONE);
		mntmRequestHashes.setText("Request Hashes");
		
		MenuItem mntmResetQuery = new MenuItem(menu_1, SWT.NONE);
		mntmResetQuery.setText("Reset Query");
		
		//Browser Creator==================================
		
		Browser browser = new Browser(adminTool, SWT.NONE);
		browser.setUrl(hashURL);
		browser.setBounds(10, 10, (windowWidth - 16) - 20, (windowHeight - topBarSize) - 100);
		
		//Check buttons display initializer===============================
		
		btnCheckButton.setBounds(10, (((windowHeight - topBarSize) - 100) + 29) + 20, ((windowWidth - 16) - 20)/8, 22);
		btnCheckButton.setText("Not Administrated");
		
		btnCheckButton_1.setBounds(10, (((windowHeight - topBarSize) - 100) + 29) + 40, ((windowWidth - 16) - 20)/8, 22);
		btnCheckButton_1.setText("All The Hashes");
		
		btnCheckButton_2.setBounds((10 + (((windowWidth - 16) - 20)/8)), (((windowHeight - topBarSize) - 100) + 29) + 20, ((windowWidth - 16) - 20)/8, 22);
		btnCheckButton_2.setText("Only Banned Ones");
		
		btnCheckButton_3.setBounds((10 + (((windowWidth - 16) - 20)/8)), (((windowHeight - topBarSize) - 100) + 29) + 40, ((windowWidth - 16) - 20)/8, 22);
		btnCheckButton_3.setText("Only NSFW Ones");
		
		//Button display initalizing======================================
		
		btnNewButtonPrevious.setBounds(10, ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonPrevious.setText("Previous");
		//TODO : Fix this button
		btnNewButtonPrevious.setEnabled(false);
		
		btnNewButtonNext.setBounds((10 + ((((windowWidth - 16) - 20)/8)*7)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonNext.setText("Next");
		
		btnNewButtonSFW.setBounds((10 + ((((windowWidth - 16) - 20)/8)*4)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonSFW.setText("SFW");
		
		btnNewButtonNSFW.setBounds((10 + ((((windowWidth - 16) - 20)/8)*3)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonNSFW.setText("NSFW");
		
		btnNewButtonBan.setBounds((10 + ((((windowWidth - 16) - 20)/8)*5)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonBan.setText("Ban");
		
		btnNewButtonCTC.setBounds((10 + ((((windowWidth - 16) - 20)/8)*6)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonCTC.setText("Copy To Clipboard");
		
		btnNewButtonForget.setBounds((10 + (((windowWidth - 16) - 20)/8)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
		btnNewButtonForget.setText("Forget");
		//TODO : Fix this button
		btnNewButtonForget.setEnabled(false);
		
		btnGoogleImage.setBounds((10 + ((((windowWidth - 16) - 20)/8)*2)), ((windowHeight - topBarSize) - 100) + 15, ((windowWidth - 16) - 20)/8, 29);
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
					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE sfw = 0 AND nsfw = 0 AND banned = 0", hashArray);
					
					arrayIndex = -1;
					
					textNbOfHashes.setText(calculateNbHashes());
				} else if (btnCheckButton_1.getSelection()) {
					enableAllButtons();
					
					dbConnection.querySelect("SELECT * FROM hash_info", hashArray);
					
					arrayIndex = -1;
					
					textNbOfHashes.setText(calculateNbHashes());
				} else if (btnCheckButton_2.getSelection()) {
					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE banned = 1", hashArray);
					
					arrayIndex = -1;
					
					textNbOfHashes.setText(calculateNbHashes());
				} else if (btnCheckButton_3.getSelection()) {
					enableAllButtons();

					dbConnection.querySelect("SELECT * FROM hash_info WHERE nsfw = 1", hashArray);
					
					arrayIndex = -1;
					
					textNbOfHashes.setText(calculateNbHashes());
				} else {
					textNbOfHashes.setText("Select an option please");
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
	
	private String calculateNbHashes() {
		String arraySize = String.valueOf(hashArray.size());
		
		return arraySize;
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
	}
	
}
