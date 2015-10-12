/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Thread;

import dmdb.Registers.Person;
import dmdb.Registers.Title;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alfonso
 */
public class SQLThread extends Thread{
    
    
    //
    private final String DbPassword;
    private final String DbUser;
    
    private final String DbURL;
    private final String DbJDBC;
    
    
    //Main Connection
     Connection conn;
     
//     
//     //The thread will be mutable to define which table list will be updated.
//     //For example, when I load another controller onto the main controller, it might have different artists list.
//     //I am also thiking of reciving the list-to-update at run time.
//    ObservableList<Person> artistsObsList;
//    ObservableList<Person> directorsObsList;
//    ObservableList<Person> titleObsList;
//    
//    //Setters for the mmutable
    
    
    public SQLThread(String user,String pass, String URLDb, String jdbc ){
        DbPassword = pass;
        DbUser = user;
        DbURL = URLDb;
        DbJDBC = jdbc;
        
        
    }
            
            
    @Override
     public void run() {
        
        try {
            
            //STEP 1: Register JDBC driver
              Class.forName(DbJDBC);
            
            //STEP 2: Open a connection
            System.out.println("Connecting to database in SQL Thread");
            
            conn = DriverManager.getConnection(DbURL, DbUser, DbPassword);
                
        
        }catch (ClassNotFoundException | SQLException ex) {
               System.out.println(ex);
        }
    }
     
    public void close(){
        
         
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
            }
    }
    
    public void updateArtistsSimpleSearch(ObservableList<Person> obs,String search)
    {
        
        obs.removeAll(obs);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
    
         pstmt = conn.prepareStatement("SELECT * FROM Artists WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
               
                    pstmt.setString(1,search); 
                    pstmt.setString(2,search);
                    
          rs = pstmt.executeQuery();
             
            while (rs.next()) {
                
            Integer personID = rs.getInt("ArtistID");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String biogra = rs.getString("Biography");
            Date date = rs.getDate("BirthDate");
            
            
              Person r = new Person(personID, firstName,lastName,biogra,date);
              obs.add(r);  
            }
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        finally{
            
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
        }            
    }
        
    
     public void updateDirectorsSimpleSearch(ObservableList<Person> obs,String search)
    {
        
        obs.removeAll(obs);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
    
         pstmt = conn.prepareStatement("SELECT * FROM Directors WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
               
                    pstmt.setString(1,search); 
                    pstmt.setString(2,search);
                    
          rs = pstmt.executeQuery();
             
            while (rs.next()) {
                
            Integer personID = rs.getInt("DirectorID");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String biogra = rs.getString("Biography");
            Date date = rs.getDate("BirthDate");
            
            
              Person r = new Person(personID, firstName,lastName,biogra,date);
              obs.add(r);  
            }
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        finally{
            
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
        }            
    }
          
     
    public void updateTitlesSimpleSearch(ObservableList<Title> obs,String search)
    {
        
        obs.removeAll(obs);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
    
         pstmt = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ?");     
               
                    pstmt.setString(1,search); 
                    
                    
          rs = pstmt.executeQuery();
             
            while (rs.next()) {
                
            Integer ID = rs.getInt("TitleID");
            String firstName = rs.getString("Name");
//            String lastName = rs.getString("LastName");
//            String biogra = rs.getString("Biography");
            Date date = rs.getDate("ReleaseDate");
            
            
              Title r = new Title(ID, firstName,date);
              obs.add(r);  
            }
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        finally{
            
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
        }            
    }
}


        
    
//    
//       
//             pstmt = conn.prepareStatement("SELECT * FROM Artists WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
////               
////                    pstmt.setString(1,search); 
////                    pstmt.setString(2,search);
//                    
//             rs = pstmt.executeQuery();
//            while (rs.next()) {
//                
//              
//            Integer personID = rs.getInt("ArtistID");
//            String firstName = rs.getString("FirstName");
//            String lastName = rs.getString("LastName");
//            String biogra = rs.getString("Biography");
//            Date date = rs.getDate("BirthDate");
//            }
            
 
     
    
    
