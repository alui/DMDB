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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    
            
    private PreparedStatement selectDirectorsForTable;
    private PreparedStatement selectArtistsForTable;
    private PreparedStatement selectTitlesForTable;
    private Queue<PreparedStatement> listPts;
    
    //For OTher controllers
    public void setTitlesComboBox(ComboBox<Title> obs){
        titlesComboBox=obs;
        
    }
    private ComboBox<Title> titlesComboBox;
    private PreparedStatement selectTitlesForComboBox;
    
    
     public void setDirectorComboBox(ComboBox<Person> obs){
        directorComboBox=obs;
        
    }
     
    private ComboBox<Person> directorComboBox;
    private PreparedStatement selectDirectorsForComboBox;
    
    
     public void setArtistsComboBox(ComboBox<Person> obs){
        artistsComboBox=obs;
        
    }
     
    private ComboBox<Person> artistsComboBox;
    private PreparedStatement selectArtistsForComboBox;
    
    //FOR RELAODING PURPOSES ONLY
    private String r_directorsPrepared;
    private String r_artistsPrepared;
    private String r_titlesPrepared;
              
    
    private boolean shouldSleep;
            
    @Override
    public void run() {
         shouldSleep = true;
         
         listPts = new LinkedList<PreparedStatement>() {};
         
        
        try {
            
            //STEP 1: Register JDBC driver
              Class.forName(DbJDBC);
            
            //STEP 2: Open a connection
            System.out.println("Connecting to database in SQL Thread");
            
            conn = DriverManager.getConnection(DbURL, DbUser, DbPassword);
            
                
        
        }catch (ClassNotFoundException | SQLException ex) {
               System.out.println(ex);
        } 
        
                this.selectArtists("");
                this.selectDirectors("");
                this.selectTitles("");
        
        while(true){
               try {
                   
               if(shouldSleep){
                       SQLThread.sleep(100);
                       continue;
               }
               } catch (InterruptedException ex) {
                   Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
               }
                
          
             
             PreparedStatement p;
             while( ( p = listPts.poll())!=null)
             {
               try {
                   p.execute();
               } catch (SQLException ex) {
               }
               finally{
                    try{
                        if(p!=null)
                            p.close();
                    }catch(SQLException se2){
                    }
                }
                 
             }
             
            if(selectTitlesForComboBox!=null){
                 ObservableList<Title> innerObsk = FXCollections.observableArrayList();
                 ResultSet rs;
                 try {
                     rs = selectTitlesForComboBox.executeQuery();
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
                        if(selectTitlesForComboBox!=null)
                            selectTitlesForComboBox.close();
                        selectTitlesForComboBox=null;
                    }catch(SQLException se2){  
                        selectTitlesForComboBox=null;

                    }
                }    
                
            }
            
            if(selectDirectorsForComboBox!=null)
            {
             ObservableList<Person> innerObsk = FXCollections.observableArrayList();
             ResultSet rs;
             try {
                 rs = selectDirectorsForComboBox.executeQuery();
                   
             
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
                    Person r = new Person(id, firstName,lastName,biogra,d,"Male");
                     innerObsk.add(r);   
                }
                Platform.runLater(() -> {
                    
                    directorComboBox.getItems().removeAll(directorComboBox.getItems());
                    innerObsk.removeAll(directorsTable.getItems());
                    directorComboBox.getItems().addAll(innerObsk);

                     if(directorComboBox.getItems().size()>0)
                            directorComboBox.show();
                        else
                            directorComboBox.hide();

                   });
                } catch (SQLException ex) {
                           Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);

                }finally{

                    try{
                        if(selectDirectorsForComboBox!=null)
                            selectDirectorsForComboBox.close();
                        selectDirectorsForComboBox=null;
                    }catch(SQLException se2){  
                        selectDirectorsForComboBox=null;

                    }
                }    
                
            }
            //personComboPrepared
            if(selectArtistsForComboBox!=null)
            {
                 ObservableList<Person> innerObsk = FXCollections.observableArrayList();
             ResultSet rs;
             try {
                 rs = selectArtistsForComboBox.executeQuery();
                   
             
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
                    Person r = new Person(id, firstName,lastName,biogra,d,"Male");
                     innerObsk.add(r);   
                }
                Platform.runLater(() -> {

                    artistsComboBox.getItems().removeAll(artistsComboBox.getItems());
                    
                    innerObsk.removeAll(artistsTable.getItems());
                    artistsComboBox.getItems().addAll(innerObsk);

                     if(artistsComboBox.getItems().size()>0)
                            artistsComboBox.show();
                        else
                            artistsComboBox.hide();

                   });
                } catch (SQLException ex) {
                           Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);

                }finally{

                    try{
                        if(selectArtistsForComboBox!=null)
                            selectArtistsForComboBox.close();
                        selectArtistsForComboBox=null;
                    }catch(SQLException se2){  
                        selectArtistsForComboBox=null;

                    }
                }    
                
            }
            
            if(selectArtistsForTable!=null){
             ObservableList<Person> innerObs = FXCollections.observableArrayList();
             ResultSet rs;
                try {  
                    rs = selectArtistsForTable.executeQuery();
                    while (rs.next()) {

                        Integer personID = rs.getInt("ArtistID");
                        String firstName = rs.getString("FirstName");
                        String lastName = rs.getString("LastName");
                        String biogra = rs.getString("Biography");
                        String dateString = rs.getString("BirthDate");
                        java.sql.Date d;
                        d =  java.sql.Date.valueOf(dateString);
                        Person r = new Person(personID, firstName,lastName,biogra,d,"Male");
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
                        if(selectArtistsForTable!=null)
                            selectArtistsForTable.close();
                        selectArtistsForTable=null;
                    }catch(SQLException se2){  
                        selectArtistsForTable=null;

                    }
             
            }
        
              
             
            if(selectDirectorsForTable!=null)
            {
             ObservableList<Person> innerDirObs = FXCollections.observableArrayList();
             
             
             ResultSet rsDir;
             try {
                  
                       
                       rsDir = selectDirectorsForTable.executeQuery();
                   
             
            while (rsDir.next()) {
                
                Integer personID = rsDir.getInt("DirectorID");
                String firstName = rsDir.getString("FirstName");
                String lastName = rsDir.getString("LastName");
                String biogra = rsDir.getString("Biography");
                String dateString = rsDir.getString("BirthDate");
                java.sql.Date d;


                d =  java.sql.Date.valueOf(dateString);
            
                
                Person r = new Person(personID, firstName,lastName,biogra,d,"Male");
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
                if(selectDirectorsForTable!=null)
                    selectDirectorsForTable.close();
                selectDirectorsForTable=null;
            }catch(SQLException se2){  
                selectDirectorsForTable=null;
                
            }
                }    
                
            }
            }
                
            
                
            if(selectTitlesForTable!=null)
            {
             ObservableList<Title> innerObsk = FXCollections.observableArrayList();
             
             
             ResultSet rs;
             try {
                  
                       
                       rs = selectTitlesForTable.executeQuery();
                   
             
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
                if(selectTitlesForTable!=null)
                    selectTitlesForTable.close();
                selectTitlesForTable=null;
            }catch(SQLException se2){  
                selectTitlesForTable=null;
                
            }
                }    
                
            }
                
              //Simple executeo
           
            
                shouldSleep= true;
            }
        
    }
    
    public void close(){
        
         
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
            }
    }
    
            
    public void selectTitlesForDirector(Register r)
    {
        try{
         selectTitlesForTable = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Titles T\n" +
                    "WHERE T.TitleID in ( SELECT A.TitleID FROM Dirigio A WHERE A.DirectorID=?);");     
                    selectTitlesForTable.setInt(1,r.getID()); 
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  

    }
    public void selectTitlesForArtist(Register r)
    {
        
        try{
         selectTitlesForTable = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Titles T\n" +
                    "WHERE T.TitleID in ( SELECT A.TitleID FROM Actuo A WHERE A.ArtistID=?);");     
                    selectTitlesForTable.setInt(1,r.getID()); 
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
        
    }
    
    public void selectArtistsForTitle(Register r)
    {
        
        try{
         selectArtistsForTable = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Artists T\n" +
                    "WHERE T.ArtistID in ( SELECT ArtistID FROM Actuo WHERE TitleID=?);");     
                    selectArtistsForTable.setInt(1,r.getID()); 
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
        
    }
      
    public void selectDirectorsForTitle(Register r)
    {
        
        try{
         selectDirectorsForTable = conn.prepareStatement("SELECT T.*\n" +
                                                "FROM Directors T\n" +
                    "WHERE T.DirectorID in ( SELECT DirectorID FROM Dirigio WHERE TitleID=?);");     
                    selectDirectorsForTable.setInt(1,r.getID()); 
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
        
    }
    public void selectTitles(String search){
        r_titlesPrepared= search;
        try{
         selectTitlesForTable = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ? OR Genere LIKE ?");     
                    selectTitlesForTable.setString(1,search+"%"); 
                    selectTitlesForTable.setString(2,search+"%"); 
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
     public void selectComboTitles(String search){
        try{
         selectTitlesForComboBox = conn.prepareStatement("SELECT * FROM Titles WHERE Name LIKE ? OR Genere LIKE ?");     
                    selectTitlesForComboBox.setString(1,search+"%"); 
                    selectTitlesForComboBox.setString(2,search+"%"); 
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
      public void selectComboDirectors(String search){
        try{
         selectDirectorsForComboBox = conn.prepareStatement("SELECT * FROM  Directors  WHERE FirstName LIKE ? OR LastName LIKE ? ");     
                    selectDirectorsForComboBox.setString(1,search+"%");  
                    selectDirectorsForComboBox.setString(2,search+"%");  
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
       public void selectComboArtists(String search){
        try{
         selectArtistsForComboBox = conn.prepareStatement("SELECT * FROM  Artists  WHERE FirstName LIKE ? OR LastName LIKE ? ");     
                    selectArtistsForComboBox.setString(1,search+"%");  
                    selectArtistsForComboBox.setString(2,search+"%");  
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        }  
    }
    public void selectDirectors(String search){
           r_directorsPrepared  =search;
        try{
        
            selectDirectorsForTable = conn.prepareStatement("SELECT * FROM Directors WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
                    selectDirectorsForTable.setString(1,search+"%"); 
                    selectDirectorsForTable.setString(2,search+"%");
                 
                    
              
                    
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
    public void selectArtists(String search){
        r_artistsPrepared = search;
        try{
            selectArtistsForTable = conn.prepareStatement("SELECT * FROM Artists WHERE FirstName LIKE ? OR LastName LIKE ? ");                   
               
                    selectArtistsForTable.setString(1,search+"%"); 
                    selectArtistsForTable.setString(2,search+"%");
                    
                
           
        this.shouldSleep = false;
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
        
        
    }
    public void reload() {
        
        if(r_artistsPrepared!=null)
            selectArtists(r_artistsPrepared);
        if(r_directorsPrepared!=null)
            selectDirectors(r_directorsPrepared);
        if(r_titlesPrepared!=null)
            selectTitles(r_titlesPrepared);
        
               
    }
    public void addRelacionActuo(Register art, Register title){
         try{
    
         PreparedStatement temporalPts = conn.prepareStatement("INSERT INTO Actuo (ArtistID, TitleID)\n" +
        "VALUES (?,?)");   
                    temporalPts.setInt(1,art.getID());
                    temporalPts.setInt(2,title.getID());
        listPts.add(temporalPts);
        }
        catch(SQLException se){
                 System.out.println(se);
        }
    }
    
    public void addRelacionDirigio(Register dir, Register title){
        
        try{
    
         PreparedStatement temporalPts = conn.prepareStatement("INSERT INTO Dirigio (DirectorID, TitleID)\n" +
        "VALUES (?,?)");   
                    temporalPts.setInt(1,dir.getID());
                    temporalPts.setInt(2,title.getID());
         listPts.add(temporalPts);
        }
        catch(SQLException se){
                 System.out.println(se);
        }
    }
    public void addInsertPersonOfKind(Person r,String kindName) {
                  
        PreparedStatement pstmt = null;
        try{
    
         pstmt = conn.prepareStatement("INSERT INTO "+kindName +"s (FirstName, LastName, Biography,BirthDate)\n" +
        "VALUES (?,?,?,?)");   
                    pstmt.setString(1,r.getFirstName());
                    pstmt.setString(2,r.getLastName());
                    pstmt.setString(3,r.getBiography());

                    pstmt.setString(4,r.getBirthDate().toString());
                    
//          pstmt.execute();
//                    shouldSleep=false;
                    listPts.add(pstmt);
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        
        
    }

    public void addUpdatePersonOfKind(Person r,String kindName) {
                  
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
                    
          listPts.add(pstmt);
                    
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
    
   

    public void addUpdateTitle(Title r) {
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
                    
                    listPts.add(pstmt);
                  
        }
        catch(SQLException se){
                 System.out.println(se);
        }
        
    }

    
    public void addInsertTitle(Title r) {
//        PreparedStatement pstmt = null;
        try{
    
         PreparedStatement tpts = conn.prepareStatement("INSERT INTO Titles (Name, Summary,ReleaseDate,Genere)\n" +
        "VALUES (?,?,?,?)");   
                    tpts.setString(1,r.getName());
                    tpts.setString(2,r.getSummary());
                    tpts.setString(3,r.getReleaseDate().toString());
                    tpts.setString(4,r.getGenere());
          listPts.add(tpts);
        }
        catch(SQLException se){
                 System.out.println(se);
        }
     }  
        

    
    
    public void addDelete(String kindName, Register r )
    {
        try{
         PreparedStatement tPts = conn.prepareStatement("DELETE FROM "+kindName+"s \n"+
        "WHERE "+kindName +"ID =?"); 
         tPts.setInt(1,r.getID());  
        listPts.add(tPts);
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
    
    static public String prepareDelete(String tableName, String column ,List<Register> s){
        
            
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
    public void addDelete(String pp)
    {
            PreparedStatement del = null;
        try{
         del = conn.prepareStatement(pp);
         listPts.add(del);
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
    
     public void addDeleteRelacionActuo(Register title ,Register s)
    {
        try{
         PreparedStatement temporalPts = conn.prepareStatement("DELETE FROM Actuo \n"+
                 
        "WHERE TitleID =? AND ArtistID=?"); 
         temporalPts.setInt(1,title.getID()); 
         temporalPts.setInt(2,s.getID()); 
         listPts.add(temporalPts);
         
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
     public void addDeleteRelacionDirigio(Register r ,Register s)
    {
        try{
         PreparedStatement temporalPts = conn.prepareStatement("DELETE FROM Dirigio \n"+
        "WHERE TitleID =? AND DirectorID=?"); 
         temporalPts.setInt(1,r.getID()); 
         temporalPts.setInt(2,s.getID()); 
         listPts.add(temporalPts);
        }
        catch(SQLException se){
                 System.out.println(se);
        } 
    }
     
     public Person offResloveArtistID(Person r){
         
          
          
          
             
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
            
                good = new Person(personID, firstName,lastName,biogra,d,"Male");
                  
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
       
               
     public Person offResolveDirectorID(Person r){
         
          
          
          
             
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
            
                good = new Person(personID, firstName,lastName,biogra,d,"Male");
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

    public void selectArtistsAdvancedSearch(String firstName, String lastName, Date sqlDate1, Date sqlDate2, Person realPerson) {
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
               selectArtistsForTable = conn.prepareStatement(sql);
               
               if(firstName!=null){
               selectArtistsForTable.setString(c++,firstName+"%"); 
               }
               if(lastName!=null)
               selectArtistsForTable.setString(c++,lastName+"%"); 
               
               if(realPerson!=null)
               {
                   selectArtistsForTable.setInt(c++,realPerson.getID()); 
                   if(sqlDate1!=null && sqlDate2!=null){
                       selectArtistsForTable.setString(c++,sqlDate1.toString()); 

                       selectArtistsForTable.setString(c++,sqlDate2.toString()); 
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

    public Title offResolveTitleID(Title r) {
         
          
         Title good =null;
         ResultSet rs;
         PreparedStatement pstmt=null;
         PreparedStatement insertPST=null;
         try {
                  
                    insertPST = conn.prepareStatement("INSERT INTO Titles (Name, Summary,ReleaseDate,Genere)\n" +
        "VALUES (?,?,?,?)");   
                    insertPST.setString(1,r.getName());
                    insertPST.setString(2,r.getSummary());
                    insertPST.setString(3,r.getReleaseDate().toString());
                    insertPST.setString(4,r.getGenere());
          
                    insertPST.execute();
                                
                   pstmt = conn.prepareStatement("SELECT * FROM Titles WHERE Name=? ");
                   pstmt.setString(1,r.getName()); 
                   rs = pstmt.executeQuery();
                   
            while (rs.next()) {
                
                Integer ID = rs.getInt("TitleID");
                String firstName = rs.getString("Name");
                String summary = rs.getString("Summary");

                String dateString = rs.getString("ReleaseDate");

                java.sql.Date d;
                d =  java.sql.Date.valueOf(dateString);

                String genere = rs.getString("Genere");

                  good  = new Title(ID, firstName,summary,d,genere);
                  

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

    public void wakeQueue() {
        this.shouldSleep = false;
        
    }
    
}
        
    

 