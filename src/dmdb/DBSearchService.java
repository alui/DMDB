/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb;

import javafx.concurrent.Service;
import java.sql.*;


/**
 *
 * @author Alfonso
 */
public abstract  class DBSearchService<T> extends Service<T> {
    
      
    static final protected String DbURL = "jdbc:sqlite:movie.db";
    static final protected String DbPassword= "password";
    static final protected String DbUser ="username";
    static final protected String DbJDBC ="org.sqlite.JDBC";  
    
    
    protected Connection DBConnection = null;
    protected Statement statement = null;
    
    public DBSearchService(){
        super();
         try{
            
            //STEP 2: Register JDBC driver
            Class.forName(DbJDBC);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            DBConnection = DriverManager.getConnection(DbURL, DbUser, DbPassword);
            
            
            statement = DBConnection.createStatement();
            
            System.out.println("Database Connected.");
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(ClassNotFoundException e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
         
         
//         finally{
//            //finally block used to close resources
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }// nothing we can do
//            try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
//        }//end try
        
    
    };

    
    @Override
    protected void cancelled() {
        
        
            clearConnection();
//        super.cancelled();
    }
    @Override
    protected void failed() {
        
        
            clearConnection();
//        super.cancelled();
    }
    
    @Override
    protected void succeeded() {

            clearConnection();
//        super.cancelled();
    }
 
    private void clearConnection(){
        
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
                
            }// nothing we can do
            
            
            
            try{
                if(DBConnection!=null)
                    DBConnection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        
    }
    
    
}
