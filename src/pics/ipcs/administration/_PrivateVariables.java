/*
 * Rename this file to PrivateVariable.java (Just remove the underscore at the beginning of the name) 
 * Also insert the database name, sql username and sql password
 */

package pics.ipcs.administration;

public class _PrivateVariables {
	private static String CON_DB_USER_SELECT = ""; //MODIFY THIS
	private static String PSWD_DB_USER_SELECT = "";  //MODIFY THIS
	private static String DB_NAME = "";  //MODIFY THIS
	
	public static String getDbUser () {
		return CON_DB_USER_SELECT;
	}
	
	public static String getDbPswd () {
		return PSWD_DB_USER_SELECT;
	}
	
	public static String getDbName() {
		return DB_NAME;
	}
}
