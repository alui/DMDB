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
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

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
    public void setArtistsTable(TableView<Person> obs){
        artistsTable=obs;
    }
       
    public void setDirectorsTable(TableView<Person> obs){
        directorsTable=obs;
        
    }
    
    public void setTitlesTable(TableView<Title> obs){
        titlesTable=obs;
        
    }
    
       
    private TableView<Person> artistsTable;
    private TableView<Title> titlesTable;
    private TableView<Person> directorsTable;
    
            
    private PreparedStatement pts;
    private PreparedStatement directorsPrepared;
    private PreparedStatement artistsPrepared;
    private PreparedStatement titlesPrepared;
    
    
    //For OTher controllers
    public void setTitlesComboBox(ComboBox<Title> obs){
        titlesComboBox=obs;
        
    }
    private ComboBox<Title> titlesComboBox;
    private PreparedStatement titlesComboPrepared;
    
    
     public void setPersonComboBox(ComboBox<Person> obs){
        personComboBox=obs;
        
    }
     
    private ComboBox<Person> personComboBox;
    private PreparedStatement personComboPrepared;
    
    //FOR RELAODING PURPOSES ONLY
    private String r_directorsPrepared;
    private String r_artistsPrepared;
    private String r_titlesPrepared;
              
    
    private boolean shouldSleep;
            
    @Override
    public void run() {
         shouldSleep = true;
        
        try {
            
            //STEP 1: Register JDBC driver
              Class.forName(DbJDBC);
            
            //STEP 2: Open a connection
            System.out.println("Connecting to database in SQL Thread");
            
            conn = DriverManager.getConnection(DbURL, DbUser, DbPassword);
            
                
        
        }catch (ClassNotFoundException | SQLException ex) {
               System.out.println(ex);
        } 
        
        while(true){
               try {
                   
               if(shouldSleep){
                       SQLThread.sleep(100);
                       continue;
               }
               } catch (InterruptedException ex) {
                   Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
               }
                
          
               

            if(pts!=null){
               try {
                   pts.execute();
               } catch (SQLException ex) {
                   Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
               }
               finally{
//            
                    try{
                        if(pts!=null)
                            pts.close();
                        pts=null;
                    }catch(SQLException se2){
                        pts=null;
                    }
                }
            }
            if(titlesComboPrepared!=null){
                 ObservableList<Title> innerObsk = FXCollections.observableArrayList();
                 ResultSet rs;
                 try {
                     rs = titlesComboPrepared.executeQuery();
                    while (rs.next()) {

                        Integer ID = rs.getInt("TitleID");
                        String firstName = rs.getString("Name");
                        String summary = rs.getString("Summary");

                        String dateString = rs.getString("ReleaseDate");

                        java.sql.Date d;
                        d =  java.sql.Date.valueOf(dateString);

                        String genere = rs.getString("Genere");

                        Title r = new Title(ID, firstName,summary,d,genere);
                        innerObsk.add(r);  
                    }
                    Platform.runLater(() -> {

                        titlesComboBox.getItems().removeAll(titlesComboBox.getItems());
                        System.out.println("\n\nPrintin" + innerObsk);
                        System.out.println("Later" + innerObsk);
                        innerObsk.removeAll(titlesTable.getItems());
                        titlesComboBox.getItems().addAll(innerObsk);

                         if(titlesComboBox.getItems().size()>0)
                                titlesComboBox.show();
                            else
                                titlesComboBox.hide();

                   });



                } catch (SQLException ex) {
                           Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);

                }finally{

                    try{
                        if(titlesComboPrepared!=null)
                            titlesComboPrepared.close();
                        titlesComboPrepared=null;
                    }catch(SQLException se2){  
                        titlesComboPrepared=null;

                    }
                }    
                
            }
            
            if(personComboPrepared!=null)
            {
                 ObservableList<Person> innerObsk = FXCollections.observableArrayList();
             ResultSet rs;
             try {
                 rs = personComboPrepared.executeQuery();
                   
             
                while (rs.next()) {
                    Integer id;
                    try{
                        id = rs.getInt("ArtistID");
                    }catch(SQLException e){
                        id = rs.getInt("DirectorID");
                    }

                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    String biogra = rs.getString("Biography");
                    String dateString = rs.getString("BirthDate");
                    java.sql.Date d;


                    d =  java.sql.Date.valueOf(dateString);
                    Person r = new Person(id, firstName,lastName,biogra,d);
                     innerObsk.add(r);   
                }
                Platform.runLater(() -> {

                    personComboBox.getItems().removeAll(personComboBox.getItems());
                    
                    personComboBox.getItems().addAll(innerObsk);

                     if(personComboBox.getItems().size()>0)
                            personComboBox.show();
                        else
                            personComboBox.hide();

                   });
                } catch (SQLException ex) {
                           Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);

                }finally{

                    try{
                        if(personComboPrepared!=null)
                            personComboPrepared.close();
                        personComboPrepared=null;
                    }catch(SQLException se2){  
                        personComboPrepared=null;

                    }
                }    
                
            }
            //personComboPrepared
            
            if(artistsPrepared!=null){
             ObservableList<Person> innerObs = FXCollections.observableArrayList();
             ResultSet rs;
                try {  
                    rs = artistsPrepared.executeQuery();
                    while (rs.next()) {

                        Integer personID = rs.getInt("ArtistID");
                        String firstName = rs.getString("FirstName");
                        String lastName = rs.getString("LastName");
                        String biogra = rs.getString("Biography");
                        String dateString = rs.getString("BirthDate");
                        java.sql.Date d;
                        d =  java.sql.Date.valueOf(dateString);
                        Person r = new Person(personID, firstName,lastName,biogra,d);
                         innerObs.add(r);  
                    }
                    Platform.runLater(() -> {
                        artistsTable.getSelectionModel().clearSelection();
                        artistsTable.getItems().removeAll(artistsTable.getItems());       
                        artistsTable.getItems().addAll(innerObs);
                        artistsTable.setDisable(false);
                       });
                
                } catch (SQLException ex) {
                           Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);

                }finally{

                    try{
                        if(artistsPrepared!=null)
                            artistsPrepared.close();
                        artistsPrepared=null;
                    }catch(SQLException se2){  
                        artistsPrepared=null;

                    }
             
            }
        
              
             
            if(directorsPrepared!=null)
            {
             ObservableList<Person> innerDirObs = FXCollections.observableArrayList();
             
             
             ResultSet rsDir;
             try {
                  
                       
                       rsDir = directorsPrepared.executeQuery();
                   
             
            while (rsDir.next()) {
                
                Integer personID = rsDir.getInt("DirectorID");
                String firstName = rsDir.getString("FirstName");
                String lastName = rsDir.getString("LastName");
                String biogra = rsDir.getString("Biography");
                String dateString = rsDir.getString("BirthDate");
                java.sql.Date d;


                d =  java.sql.Date.valueOf(dateString);
            
                
                Person r = new Person(personID, firstName,lastName,biogra,d);
                innerDirObs.add(r);  
            }
            
            
                 
            
                    Platform.runLater(() -> {
                        directorsTable.getSelectionModel().clearSelection();
                        directorsTable.getItems().removeAll(directorsTable.getItems());       
                        directorsTable.getItems().addAll(innerDirObs);
                        
                        directorsTable.setDisable(false);
                       });
                
                 
                 
            } catch (SQLException ex) {
                       Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
             
            }
             finally{
            
            try{
                if(directorsPrepared!=null)
                    directorsPrepared.close();
                directorsPrepared=null;
            }catch(SQLException se2){  
                directorsPrepared=null;
                
            }
                }    
                
            }
            }
                
            
                
            if(titlesPrepared!=null)
            {
             ObservableList<Title> innerObsk = FXCollections.observableArrayList();
             
             
             ResultSet rs;
             try {
                  
                       
                       rs = titlesPrepared.executeQuery();
                   
             
            while (rs.next()) {
                
            Integer ID = rs.getInt("TitleID");
            String firstName = rs.getString("Name");
            String summary = rs.getString("Summary");
            
            String dateString = rs.getString("ReleaseDate");
            
            java.sql.Date d;
            d =  java.sql.Date.valueOf(dateString);
            
            String genere = rs.getString("Genere");
            
              Title r = new Title(ID, firstName,summary,d,genere);
              innerObsk.add(r);  
            }
            
            
                 
            
                    Platform.runLater(() -> {
                        titlesTable.getSelectionModel().clearSelection();
                        titlesTable.getItems().removeAll(titlesTable.getItems());
                        titlesTable.getItems().addAll(innerObsk);
                        
                        titlesTable.setDisable(false);
                       });
                
                 
                 
            } catch (SQLException ex) {
                       Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
             
            }
             finally{
            
            try{
                if(titlesPrepared!=null)
                    titlesPrepared.close();
                titlesPrepared=null;
            }catch(SQLException se2){  
                titlesPrepared=null;
                
            }
                }    
                
            }
                
              //Simple executeo
           
            
                shouldSleep= true;
            }
        
    }
     
    public void tryAgain(){
//        shouldSleep = false;
        if(shouldSleep==false)
            shouldSleep=true;
        else if(shouldSleep==true)
            shouldSleep=false;
            
    }
    public void close(){
        
         
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
            }
    }
    
            
    public void prepareSelectParticipatingTitlesForDirector(Register r)
    {
        try{
         titlesPrepared = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Titles T\n" +
                    "WHERE T.TitleID in ( SELECT A.TitleID FROM Dirigio A WHERE A.DirectorID=?);");     
                    titlesPrepared.setInt(1,r.getID()); 
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  

    }
    public void prepareSelectParticipatingTitlesForArtist(Register r)
    {
        
        try{
         titlesPrepared = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Titles T\n" +
                    "WHERE T.TitleID in ( SELECT A.TitleID FROM Actuo A WHERE A.ArtistID=?);");     
                    titlesPrepared.setInt(1,r.getID()); 
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
        
    }
    public void prepareStatementSelectTitles(String search){
        r_titlesPrepared= search;
        try{
         titlesPrepared = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ? OR Genere LIKE ?");     
                    titlesPrepared.setString(1,search+"%"); 
                    titlesPrepared.setString(2,search+"%"); 
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
     public void prepareStatementSelectComboTitles(String search){
        try{
         titlesComboPrepared = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ? OR Genere LIKE ?");     
                    titlesComboPrepared.setString(1,search+"%"); 
                    titlesComboPrepared.setString(2,search+"%"); 
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
      public void prepareStatementSelectComboPersons(String search){
        try{
         personComboPrepared = conn.prepareStatement("SELECT * FROM  Directors  WHERE FirstName LIKE ? OR LastName LIKE ? ");     
                    personComboPrepared.setString(1,search+"%");  
                    personComboPrepared.setString(2,search+"%");  
//                    personComboPrepared.setString(3,search+"%");  
//                    personComboPrepared.setString(4,search+"%"); 
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
    public void prepareStatementSelectDirectors(String search){
           r_directorsPrepared  =search;
        try{
        
            directorsPrepared = conn.prepareStatement("SELECT * FROM Directors WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
                    directorsPrepared.setString(1,search+"%"); 
                    directorsPrepared.setString(2,search+"%");
                 
                    
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
    public void prepareStatementSelectArtists(String search){
        r_artistsPrepared = search;
        try{
            artistsPrepared = conn.prepareStatement("SELECT * FROM Artists WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
               
                    artistsPrepared.setString(1,search+"%"); 
                    artistsPrepared.setString(2,search+"%");
                    
                
           
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
        
        
    }
    public void reload() {
        
        if(r_artistsPrepared!=null)
            prepareStatementSelectArtists(r_artistsPrepared);
        if(r_directorsPrepared!=null)
            prepareStatementSelectDirectors(r_directorsPrepared);
        if(r_titlesPrepared!=null)
            prepareStatementSelectTitles(r_titlesPrepared);
        
               
    }
    public void insertRelacionActuo(Register art, Register title){
         try{
    
         pts = conn.prepareStatement("INSERT INTO Actuo (ArtistID, TitleID)\n" +
        "VALUES (?,?)");   
                    pts.setInt(1,art.getID());
                    pts.setInt(2,title.getID());
                    
                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
    }
    
    public void insertRelacionDirigio(Register dir, Register title){
        
        try{
    
         pts = conn.prepareStatement("INSERT INTO Dirigio (DirectorID, TitleID)\n" +
        "VALUES (?,?)");   
                    pts.setInt(1,dir.getID());
                    pts.setInt(2,title.getID());
//                    pts.setString(3,r.getBiography());
//
//                    pts.setString(4,r.getBirthDate().toString());
//                    
//          pstmt.execute();
                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        
        
    }
    public void insertPersonKind(Person r,String kindName) {
                  
//        PreparedStatement pstmt = null;
        try{
    
         pts = conn.prepareStatement("INSERT INTO "+kindName +"s (FirstName, LastName, Biography,BirthDate)\n" +
        "VALUES (?,?,?,?)");   
                    pts.setString(1,r.getFirstName());
                    pts.setString(2,r.getLastName());
                    pts.setString(3,r.getBiography());

                    pts.setString(4,r.getBirthDate().toString());
                    
//          pstmt.execute();
                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        
        
    }

    public void updatePersonKind(Person r,String kindName) {
                  
//        PreparedStatement pstmt = null;
        try{
    
         pts = conn.prepareStatement(
                 "UPDATE "+ kindName+ "s \n"+
            "SET FirstName= ?, LastName= ?, Biography=?, BirthDate = ? \n"+
           " WHERE "+kindName+"ID= ? "); 
         
                    pts.setString(1,r.getFirstName());
                    pts.setString(2,r.getLastName());
                    pts.setString(3,r.getBiography());
                    pts.setString(4,r.getBirthDate().toString());
                    pts.setInt(5,r.getPersonID());
                    
//          pstmt.execute();
                    
                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
//        finally{
//            
//            try{
//                if(pstmt!=null)
//                    pstmt.close();
//            }catch(SQLException se2){
//                
//            }
//        }  
        
        
    }
    
   

    public void updateTitle(Title r) {
          PreparedStatement pstmt = null;
        try{
    
          pts = conn.prepareStatement(
                 "UPDATE Titles \n"+
            "SET Name= ?, Summary= ?, ReleaseDate = ?, Genere=? \n"+
           " WHERE TitleID= ? "); 
          
                    pts.setString(1,r.getName());
                    pts.setString(2,r.getSummary());
                    pts.setString(3,r.getReleaseDate().toString());
                    pts.setString(4,r.getGenere());
                    pts.setInt(5,r.getTitleID());
                    
//                  
                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        
    }

    
    public void insertTitle(Title r) {
//        PreparedStatement pstmt = null;
        try{
    
         pts = conn.prepareStatement("INSERT INTO Titles (Name, Summary,ReleaseDate,Genere)\n" +
        "VALUES (?,?,?,?)");   
                    pts.setString(1,r.getName());
                    pts.setString(2,r.getSummary());
                    pts.setString(3,r.getReleaseDate().toString());
                    pts.setString(4,r.getGenere());

                    shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }
     }  
        

    
    
    public void prepareStatementDelete(String kindName, Register r )
    {
        try{
         pts = conn.prepareStatement("DELETE FROM "+kindName+"s \n"+
        "WHERE "+kindName +"ID =?"); 
         pts.setInt(1,r.getID());  
        shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }

//    public void prepareMassiveDeleteActuo(List<Register> s)
//    {
//        try{
//            
//            
//            String preSQL = "DELETE FROM Actuo \n"+
//        "WHERE ";
//            String middleSQL ="";
//            for(Register r : s)
//            {
////                Register n = (Register)s; 
//                middleSQL+= ("ArtistID =? OR ");
//                
//            }
//            middleSQL+=("ArtistID =? ; \n" );
//            
//            preSQL+=middleSQL;
////            preSQL+="DELETE FROM Actuo \n"+
////        "WHERE "+ middleSQL;
//            
//         pts = conn.prepareStatement(preSQL); 
//         
//             int i = 1;
//             Register last = null;
//            for(Register r : s){
////                Register n = (Register)s; 
//               pts.setInt(i,r.getID());
//               last = r;
//                i++;
//            }
//            
//               pts.setInt(i++,last.getID());
//               
//               shouldSleep=false;
//        }
//        catch(SQLException se){
//                 System.out.println(se);
//        } 
//               
//    }
    
    public String prepareSQLDelete(String tableName, String column ,List<Register> s){
        
            
            String preSQL = "DELETE FROM "+tableName+" \n"+
        "WHERE ";
            String middleSQL ="";
            
            Register last = null;
            for(Register r : s)
            {

                middleSQL+= (column +"ID ="+r.getID()+" OR ");
                last = r;   
            }
            middleSQL+=(column +"ID ="+last.getID() +";\n" );
            
        return (preSQL+middleSQL);
    }
    public void prepareMassiveDelete(String pp)
    {
            PreparedStatement delS = null;
        try{
//            
            
            
         delS = conn.prepareStatement(pp);
         delS.execute();
                       
                   } catch (SQLException ex) {
                       Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
                   }
      finally{
//            
            try{
                if(delS!=null)
                    delS.close();
                
            }catch(SQLException se2){
                
            }
        }
    }
    
     public void prepareDeleteRelacionActuo(Register r ,Register s)
    {
        try{
         pts = conn.prepareStatement("DELETE FROM Actuo \n"+
        "WHERE TitleID =? AND ArtistID=?"); 
         pts.setInt(1,r.getID()); 
         pts.setInt(2,s.getID()); 
        shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
     public void prepareDeleteRelacionDirigio(Register r ,Register s)
    {
        try{
         pts = conn.prepareStatement("DELETE FROM Dirigio \n"+
        "WHERE TitleID =? AND DirectorID=?"); 
         pts.setInt(1,r.getID()); 
         pts.setInt(2,s.getID()); 
        shouldSleep=false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
     
     public Person resloveArtistID(Person r){
         
          
          
          
             
             Person good =null;
             ResultSet rs;
             
              PreparedStatement pstmt=null;
              
              PreparedStatement insertPST=null;
         try {
                  
                        insertPST = conn.prepareStatement("INSERT INTO Artists (FirstName, LastName, Biography,BirthDate)\n" +
        "VALUES (?,?,?,?)");   
                    insertPST.setString(1,r.getFirstName());
                    insertPST.setString(2,r.getLastName());
                    insertPST.setString(3,r.getBiography());
                    insertPST.setString(4,r.getBirthDate().toString());
                    
                        insertPST.execute();
                                
                       pstmt = conn.prepareStatement("SELECT * FROM Artists WHERE FirstName= ? ");
                       pstmt.setString(1,r.getFirstName()); 
                       rs = pstmt.executeQuery();
                       
                   
            while (rs.next()) {
                
                Integer personID = rs.getInt("ArtistID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String biogra = rs.getString("Biography");
                String dateString = rs.getString("BirthDate");
                java.sql.Date d;


                d =  java.sql.Date.valueOf(dateString);
            
                good = new Person(personID, firstName,lastName,biogra,d);
                  
            }
            
            
                 
            
                 
            } catch (SQLException ex) {
                       Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
             
            }
             finally{
            
            try{
                if(pstmt!=null)
                    pstmt.close();
                
            }catch(SQLException se2){  
                
            }
             
            }
             
             return good;
         
     }
       
               
     public Person resolveDirectorID(Person r){
         
          
          
          
             
             Person good =null;
             ResultSet rs;
             
              PreparedStatement pstmt=null;
              
              
              PreparedStatement insertPST=null;
         try {
                  
                        insertPST = conn.prepareStatement("INSERT INTO Directors (FirstName, LastName, Biography,BirthDate)\n" +
        "VALUES (?,?,?,?)");   
                    insertPST.setString(1,r.getFirstName());
                    insertPST.setString(2,r.getLastName());
                    insertPST.setString(3,r.getBiography());
                    insertPST.setString(4,r.getBirthDate().toString());
                    
                        insertPST.execute();
                                
                       pstmt = conn.prepareStatement("SELECT * FROM Directors WHERE FirstName=? ");
                       pstmt.setString(1,r.getFirstName()); 
                       rs = pstmt.executeQuery();
                   
            while (rs.next()) {
                
                Integer personID = rs.getInt("DirectorID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String biogra = rs.getString("Biography");
                String dateString = rs.getString("BirthDate");
                java.sql.Date d;


                d =  java.sql.Date.valueOf(dateString);
            
                good = new Person(personID, firstName,lastName,biogra,d);
                  
            }
            
            
                 
            
                 
            } catch (SQLException ex) {
                       Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
             
            }
             finally{
            
            try{
                if(pstmt!=null)
                    pstmt.close();
                
            }catch(SQLException se2){  
                
            }
             
            }
             
             return good;
         
     }

    public void prepareStatementAdvancedPeopleSearch(String firstName, String lastName, Date sqlDate1, Date sqlDate2, Person realPerson) {
        try{
                String sql;
            {
                int c = 1;
                
                sql ="SELECT * FROM Artists WHERE ";
                        
                        if(firstName!=null && lastName!=null ){
                            sql+=" FirstName LIKE ? AND LastName LIKE ? ";
                            
                        }
                        else if(firstName!=null)
                        {
                            sql+=" FirstName LIKE ? ";
                        }else if(lastName!=null ){
                            sql+=" LastName LIKE ? ";
                        
                        }
                        
                    if(realPerson!=null){
                            sql+=
                                " AND ArtistID in \n" +
                                "( \n" +
                                "SELECT ArtistID \n" +
                                "FROM Actuo \n" +
                                "INNER JOIN Dirigio \n" +
                                "ON Dirigio.TitleID=Actuo.TitleID AND Dirigio.DirectorID = ? ";

                         if(sqlDate1!=null && sqlDate2!=null)     {      
                                   sql += ""
                                    + "AND Actuo.TitleID in \n" +
                                    "(\n" +
                                    "SELECT T.TitleID FROM Titles T WHERE  T.ReleaseDate BETWEEN ? AND ? \n" +
                                    ")\n";
                           }
                         
                    sql+=")";
                
                    }
                    
                    System.out.println(sql);
               artistsPrepared = conn.prepareStatement(sql);
               
               if(firstName!=null){
               artistsPrepared.setString(c++,firstName+"%"); 
               }
               if(lastName!=null)
               artistsPrepared.setString(c++,lastName+"%"); 
               
               if(realPerson!=null)
               {
                   artistsPrepared.setInt(c++,realPerson.getID()); 
                   if(sqlDate1!=null && sqlDate2!=null){
                       artistsPrepared.setString(c++,sqlDate1.toString()); 

                       artistsPrepared.setString(c++,sqlDate2.toString()); 
                   }
               }
               
               
            }
            
           
//                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
    
}
        
    

 