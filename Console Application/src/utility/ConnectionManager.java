package utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionManager {
 public static Connection getConnection() throws ClassNotFoundException {
   String DB_URL = "jdbc:mysql://localhost:3306/library";
   String DB_USER = "root";
   String DB_PASS = "Talktime2003";
	      try {
	    	  Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
	    	  if(con!=null) {
	    		  return con;
	    	  }
	      }catch(SQLException e) {
	    	  System.out.println("unable to establish");
	    	  e.printStackTrace();
	      }
		return null;
	}

}
