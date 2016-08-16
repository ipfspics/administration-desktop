/*
 * Creates the methods handling the mysql connector
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

package pics.ipcs.administration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;



public class SQLConnection {
	private boolean updated = false;
	private boolean selected = false;
	
	private Connection conn;
	
	private String URL = "jdbc:mysql://ipfs.pics:3306/";
	private String driver = "com.mysql.jdbc.Driver";
	
	/**
	 * Instanciates the connection to the database
	 */
	public void connect() {
		try {
			Class.forName(this.driver).newInstance();
			conn = DriverManager.getConnection(this.URL+PrivateVariables.getDbName(), PrivateVariables.getDbUser(), PrivateVariables.getDbPswd());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection to the database
	 */
	public void close() {
		try {
			
			if (conn != null) {
				conn.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes an UPDATE query
	 * 
	 * @param query The query to be sent
	 */
	public void queryUpdate(String query) {
		updated = false;
		
		try {
			Statement st = conn.createStatement();
			
			int val = st.executeUpdate(query);
			if (val == 0)
				System.out.println("Couldn't reach database, didn't update value");
			else {
				System.out.println("Updated Succesfully");
				updated = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes a SELECT query
	 * 
	 * @param query Query to be sent
	 * @param pHashArray Array that will contain the selection
	 */
	public void querySelect(String query, ArrayList<Hash> pHashArray) {
		selected = false;
		
		try {
			Statement st = conn.createStatement();
			
			ResultSet res = st.executeQuery(query);
			
			while (res.next()) {
				String hash = res.getString("hash");
				String nbViews = res.getString("nb_views");
				pHashArray.add(new Hash(hash, nbViews));
			}
			
			if (pHashArray.size() > 0) {
				selected = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the UPDATE query state
	 * 
	 * @return Whether the query was successful or not
	 */
	public boolean getUpdated() {
		return updated;
	}
	
	/**
	 * Gets the SELECT query state
	 * 
	 * @return Whether the query was successful or not
	 */
	public boolean isSelected() {
		return selected;
	}
}
