/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Thread;

//import javafx.collections.FXCollections;
import dmdb.Registers.Person;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.*;




/**
 *
 * @author Alfonso
 */
public class SearchDirectorsTask extends Task<Void> {
    
    static final protected String DbURL = "jdbc:sqlite:movie.db";
    static final protected String DbPassword= "password";
    static final protected String DbUser ="username";
    static final protected String DbJDBC ="org.sqlite.JDBC";  
    
    
    ObservableList<Person> obs;
    String  search;
    Connection conn;
    
    public SearchDirectorsTask(ObservableList<Person> r , String text, Connection c)
    {
        super();
        obs = r;
        conn=c;
        search=text;
    
        
    }
    
    @Override
    protected Void call() throws Exception {
                
        obs.removeAll(obs);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            
            //STEP 2: Register JDBC driver
            Class.forName(DbJDBC);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            
            conn = DriverManager.getConnection(DbURL, DbUser, DbPassword);
                
                  
             pstmt = conn.prepareStatement("SELECT * FROM Directors WHERE FirstName LIKE ? OR LastName LIKE ? ");

             pstmt.setString(1,search);
             pstmt.setString(2,search);
             rs = pstmt.executeQuery();
            
            while (rs.next()) {
                
               if(isCancelled())  {
                        obs.removeAll(obs);
                        break;
                     }
                
            Integer personID = rs.getInt("DirectorID");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String biogra = rs.getString("Biography");
            Date date = rs.getDate("BirthDate");
            
            System.out.println("\t" + new Date() );
            
            
        Person r = new Person(personID, firstName,lastName,biogra, date);
        obs.add(r);  
        
        }
   
           }catch (SQLException e) {
               System.out.println(e);
               
          }finally {
            
             try{
                if(pstmt!=null)
                    pstmt.close();
            }catch(SQLException se2){
                
            }// nothing we can do
            
            try{
                if(rs!=null)
                    rs.close();
            }catch(SQLException se){
                
            }
            
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
            }
        
            
        }
                
        
        
                
                //Perfomr sql statement with text.
                
                //In run later.
                //Remove all children from current obs, and then add all new items.
                
                
            
            //            stmt.executeUpdate(sql);
                
                return null;
       }
        
    
    
    
    
}   