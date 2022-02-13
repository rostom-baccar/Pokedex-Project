/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresql;
/**
 *
 * @author auria
 */
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PostgreSQL {
    
private static int idPokemon;
private static int idAttaque;
private static int idPok, idAtt;


private Connection c = null;
private Statement stmt = null;


  
  public static void main(String args[]) {
      Connection c = null;
      Statement stmt = null;
      
      PostgreSQL bd = new PostgreSQL();
      //ajout
        
      try{
          ResultSet idP = bd.execReq ("SELECT id FROM pokemon ORDER BY id DESC limit 1;");
          while (idP.next()) {
              idPokemon = idP.getInt("id")+1;
          }
          ResultSet idA = bd.execReq("SELECT id FROM attaque ORDER BY id DESC limit 1;" );
          while ( idA.next()) {
              idAttaque = idA.getInt("id")+1;
          }
                
          Accueil.Accueil(stmt, bd, idPokemon, idAttaque);
    
          idP.close();
          idA.close();
          bd.closeDB();
      } catch(SQLException throwables) {
          throwables.printStackTrace();
      }   
      {
      System.out.println("Opened database successfully");
      }
}

  
  public PostgreSQL() {
   try {
         Class.forName("org.postgresql.Driver");
         this.c = DriverManager
            .getConnection("jdbc:postgresql://localhost:5433/pokedex",
            "postgres", "sparta");
         this.stmt=c.createStatement();
   } catch (Exception e){
        e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
}
    

public void closeDB(){
    try{
        stmt.close();
        c.close();
    } catch(Exception e){
        e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
    }
}


public ResultSet execReq(String req){
    try {
        ResultSet result = stmt.executeQuery(req);
        return result;
    } catch (SQLException throwables) {
         throwables.printStackTrace();
        return null;
    }
    
}

}

