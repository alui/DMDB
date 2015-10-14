/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Thread;

import dmdb.Registers.Person;
import dmdb.Registers.Register;
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
    
    public void selectArtists(ObservableList<Person> obs,String search)
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
            String dateString = rs.getString("BirthDate");
            java.sql.Date d;
//            java.sql.Date;
//            long longDate = rs.getLong("BirthDate");
            d =  java.sql.Date.valueOf(dateString);
            
            
//            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            
            
              Person r = new Person(personID, firstName,lastName,biogra,d);
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
        
    
     public void selectDirectors(ObservableList<Person> obs,String search)
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
            String dateString = rs.getString("BirthDate");
            java.sql.Date d;
//            java.sql.Date;
//            long longDate = rs.getLong("BirthDate");
            d =  java.sql.Date.valueOf(dateString);
            
            
              Person r = new Person(personID, firstName,lastName,biogra,d);
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
          
     
    public void selectTitles(ObservableList<Title> obs,String search)
    {
        
        obs.removeAll(obs);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
    
         pstmt = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ? OR Genere LIKE ?");     
               
                    pstmt.setString(1,search); 
                    
                    pstmt.setString(2,search); 
                    
                    
          rs = pstmt.executeQuery();
             
            while (rs.next()) {
                
            Integer ID = rs.getInt("TitleID");
            String firstName = rs.getString("Name");
//            String lastName = rs.getString("LastName");
            String summary = rs.getString("Summary");
            
            String dateString = rs.getString("ReleaseDate");
            
            java.sql.Date d;
            d =  java.sql.Date.valueOf(dateString);
            
            String genere = rs.getString("Genere");
            
              Title r = new Title(ID, firstName,summary,d,genere);
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

    public void insertPersonKind(Person r,String kindName) {
                  
        PreparedStatement pstmt = null;
        try{
    
         pstmt = conn.prepareStatement("INSERT INTO "+kindName +"s (FirstName, LastName, Biography,BirthDate)\n" +
        "VALUES (?,?,?,?)");   
                    pstmt.setString(1,r.getFirstName());
                    pstmt.setString(2,r.getLastName());
                    pstmt.setString(3,r.getBiography());
//                    if(r.getBirthDate()!=null)
                    pstmt.setString(4,r.getBirthDate().toString());
//                    pstmt.setString(4,r);
//                    else 
//                    pstmt.setString(4,"");
                    
          pstmt.execute();
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
            
//            try{
//                if(rs!=null)
//                    rs.close();
//            }catch(SQLException se){
//            }
        }  
        
        
    }


//UPDATE Customers
//SET ContactName='Alfred Schmidt', City='Hamburg'
//WHERE CustomerName='Alfreds Futterkiste';
    public void updatePersonKind(Person r,String kindName) {
                  
        PreparedStatement pstmt = null;
        try{
    
         pstmt = conn.prepareStatement(
                 "UPDATE "+ kindName+ "s \n"+
            "SET FirstName= ?, LastName= ?, Biography=?, BirthDate = ? \n"+
           " WHERE "+kindName+"ID= ? "); 
         
                    pstmt.setString(1,r.getFirstName());
                    pstmt.setString(2,r.getLastName());
                    pstmt.setString(3,r.getBiography());
                    pstmt.setString(4,r.getBirthDate().toString());
                    pstmt.setInt(5,r.getPersonID());
                    
          pstmt.execute();
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
            
//            try{
//                if(rs!=null)
//                    rs.close();
//            }catch(SQLException se){
//            }
        }  
        
        
    }
    
    public void deleteKindOfPerson(String kindName, Person r )
    {
        PreparedStatement pstmt = null;
        try{
    
         pstmt = conn.prepareStatement("DELETE FROM "+kindName+"s \n"+
        "WHERE "+kindName +"ID =?"); 
         
//                    pstmt.setString(1,r.getFirstName());
//                    pstmt.setString(2,r.getLastName());
//                    pstmt.setString(3,r.getBiography());
//                    pstmt.setString(4,r.getBirthDate().toString());
                    pstmt.setInt(1,r.getPersonID());
                    
          pstmt.execute();
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
            
//            try{
//                if(rs!=null)
//                    rs.close();
//            }catch(SQLException se){
//            }
        }  
        
        
    }

    public void updateTitle(Title r) {
          PreparedStatement pstmt = null;
        try{
    
          pstmt = conn.prepareStatement(
                 "UPDATE Titles \n"+
            "SET Name= ?, Summary= ?, ReleaseDate = ?, Genere=? \n"+
           " WHERE TitleID= ? "); 
          
                    pstmt.setString(1,r.getName());
                    pstmt.setString(2,r.getSummary());
                    pstmt.setString(3,r.getReleaseDate().toString());
                    pstmt.setString(4,r.getGenere());
                    pstmt.setInt(5,r.getTitleID());
//                    else 
//                    pstmt.setString(4,"");
                    
          pstmt.execute();
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
        
    }

    }public void insertTitle(Title r) {
        PreparedStatement pstmt = null;
        try{
    
         pstmt = conn.prepareStatement("INSERT INTO Titles (Name, Summary,ReleaseDate,Genere)\n" +
        "VALUES (?,?,?,?)");   
                    pstmt.setString(1,r.getName());
                    pstmt.setString(2,r.getSummary());
                    pstmt.setString(3,r.getReleaseDate().toString());
                    pstmt.setString(4,r.getGenere());
//                    else 
//                    pstmt.setString(4,"");
                    
          pstmt.execute();
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
            
//            try{
//                if(rs!=null)
//                    rs.close();
//            }catch(SQLException se){
//            }
        }  
        
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            
 
     
    
    
