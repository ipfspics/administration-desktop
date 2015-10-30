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

package ipfs.pics.administration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;



public class SQLConnection {
	//TODO : Move SQL connection related code to this class
	
	private static Connection conn;
	
	public void dbConnect(String pURL) {
		try {
			conn = DriverManager.getConnection(pURL+PrivateVariables.getDbName(), PrivateVariables.getDbUser(), PrivateVariables.getDbPswd());
			
		}
		catch (Exception e) {
			
		}
	}
	
	public void dbClose() {
		try {
			
			if (conn != null) {
				conn.close();
			}
		}
		catch (Exception e) {
			
		}
	}
	
	public void queryUpdate(String query) {
		try {
			
		}
		catch (Exception e) {
			
		}
	}
	
	public void querySelect(String query) {
		try {
			
		}
		catch (Exception e) {
			
		}
	}
	
}
