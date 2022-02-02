import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQL {
	
	private Connection c = null;
	private Statement stmt = null;
	
	  public PostgreSQL() {
		   try {
		         Class.forName("org.postgresql.Driver");
		         this.c = DriverManager
		            .getConnection("jdbc:postgresql://localhost:5433/pokedex",
		            "postgres", "sparta");
		         this.stmt=(Statement) c.createStatement();
		   } catch (Exception e){
		        e.printStackTrace();
		         System.err.println(e.getClass().getName()+": "+e.getMessage());
		         System.exit(0);
		      }
		}

}
