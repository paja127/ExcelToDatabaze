import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInput {

	public static Connection connection;
	
	
	public DatabaseInput() {
		

	}

	public static Connection connection() {

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			OwnLogger.logging("Oracle JDBC Driver not found.");

			//return;

		}

		Connection connection = null;

		try{
			connection = DriverManager.getConnection(PropertiesClass.provide("database"),
					PropertiesClass.provide("dbuser"), PropertiesClass.provide("dbpassword"));
			
					
		} catch (SQLException e) {

			
			System.out.println("Pripojeni k databazi selhalo! "+ e.getMessage());
			System.out.println("Prosim zkontrolujte konfiguracni soubor 'configuration.properties' ci zda databaze bezi.");
			OwnLogger.logging("Connection to " + PropertiesClass.provide("database") + " not established\n" + e.getMessage());
			

			//return;

		}

		if (connection != null) {
			OwnLogger.logging("Connection to " + PropertiesClass.provide("database") + " was successfly created.");
			System.out.println("Pripojeni k databazi bylo uspesne");
		} else {
			//empty
			
		}
		
		return connection;
	}
	
	public static void insertValues(String command) throws SQLException{
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(command);	
	}
	public static int getIntValues(String command) throws SQLException{
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(command);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		return columnsNumber;
	}
	public static void closeConnection(){
		try{
			connection.close();
			System.out.println("Spojeni s databazi bylo uspesne ukonceno.");
			OwnLogger.logging("Conection to the database has been close correctly.");
		}catch(Exception e){
			System.out.println("Spojeni s databazi nebylo ukonceno korektne.");
			OwnLogger.logging("Connection was not closed correctly.");
		}
	}



}