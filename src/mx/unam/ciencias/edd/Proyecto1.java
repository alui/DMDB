package mx.unam.ciencias.edd;


import java.sql.*;

/**
 * Proyecto 1: Graficador.
 */
public class Proyecto1 {
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    static final String DB_URL = "jdbc:sqlite:movie.db";
    
    //  Database credentials
    static final String USER = "username";
    static final String PASS = "password";
    
    public static void main(String[] args)
    {
//        
        Connection conn = null;
        Statement stmt = null;
        
    
    
        try{
            
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            //STEP 4: Execute a query
            
            stmt = conn.createStatement();
            String sql = "CREATE TABLE COMPANY " +
            "(ID INT PRIMARY KEY     NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " AGE            INT     NOT NULL, " +
            " ADDRESS        CHAR(50), " +
            " SALARY         REAL)";
            
            
            stmt.executeUpdate(sql);
            
            System.out.println("Database created successfully...");
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }
   
}
//class JDBCExample {
//    // JDBC driver name and database URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost/";
//    
//    //  Database credentials
//    static final String USER = "username";
//    static final String PASS = "password";
//    
//    public static void JDBCExample() {
//    }//end main
//}//end JDBCExample

