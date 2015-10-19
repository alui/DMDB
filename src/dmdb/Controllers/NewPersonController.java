
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;

import dmdb.Registers.Person;
import dmdb.Registers.Register;
import dmdb.Registers.Title;
import dmdb.Thread.SQLThread;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;


/**
 *
 * @author Alfonso
 */
public class NewPersonController implements Initializable {
    
    private boolean isDirector;
    private Person oldPerson = null;
    private RegisterDelegate delegate;
    private SQLThread sqlThread;
    
    
    @FXML
    private TextField firstName;
    
    
    @FXML
    private TextField lastName;
    
    @FXML
    private TextArea biography;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private TableView titlesTable;
    
             @FXML 
             private TableColumn<Title,String> t_nameCol;
             @FXML 
             private TableColumn<Title,String> t_relDateCol;
             
             @FXML 
             private TableColumn<Title,String> t_summaryCol;
             
             @FXML 
             private TableColumn<Title,String> t_genereCol;
    
    
             private ObservableList<Title> dummyList;
             private boolean isDummyPerson;
             
    @FXML
    private ComboBox<Title> titlesComboBox;
    
    public void cancel(){
        
        //Remove all aditions in private Items
       String kindName = "Artist";
       if(isDirector)
           kindName = "Director";
       if(isDummyPerson)
        sqlThread.prepareStatementDelete(kindName, oldPerson);
        
        //Delete created person if its dummy.
         for(Title p : dummyList ){
                        if(isDirector)
                            sqlThread.prepareDeleteRelacionDirigio((Title) p,oldPerson);
                        else
                            sqlThread.prepareDeleteRelacionActuo((Title) p,oldPerson);
             }
         
        
        if(delegate!=null)
            delegate.close();   
        sqlThread =null;
        
        
    }
    public void save(){
         
        
        LocalDate ld = date.getValue();
         if(firstName.getText()==null || firstName.getText().equals("")||ld==null)
             return;

        java.sql.Date sqlDate;
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
             sqlDate = new java.sql.Date(d.getTime());
         
       
             
//             
//             int id = 0;
//             if(isDummyPerson)
//             {
//                 id = oldPerson.getID();
//             }
//             
       Person p = new Person(oldPerson.getID() ,firstName.getText(),lastName.getText(),biography.getText(),sqlDate); 
       
       String kindName = "Artist";
       if(isDirector)
           kindName = "Director";
//       
//       if(isDummyPerson)
//            sqlThread.insertPersonKind(p,kindName);
//       else
            sqlThread.updatePersonKind(p,kindName);
       
      
       
       

         
        if(delegate!=null)
            delegate.close();   
        sqlThread =null;
    }
     
     public void delete(){
         
         List items =  new ArrayList (titlesTable.getSelectionModel().getSelectedItems());  
                    titlesTable.getItems().removeAll(items);
                    for(Object p : items ){
                        if(dummyList.contains((Title)p))
                            dummyList.remove((Title)p);
                                
                        if(isDirector)
                            sqlThread.prepareDeleteRelacionDirigio((Title) p,oldPerson);
                        else
                            sqlThread.prepareDeleteRelacionActuo((Title) p,oldPerson);
                    }
                    
                    titlesTable.getSelectionModel().clearSelection();
         
         
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.setValue(LocalDate.of(2000, Month.JANUARY, 1));
        this.updateCellFactoryForTables();
        
         dummyList= FXCollections.observableArrayList();
         isDummyPerson = true;
         titlesComboBox.setOnAction(e->{
             
             selectComboItem();
         }
             
         );
       
    } 

    public void selectComboItem(){
        
        Object o = titlesComboBox.getValue();
//        titlesComboBox.getEditor().selectAll();
        
        if(o instanceof Title)
        {   Title t = (Title)o;
        
            if(!titlesTable.getItems().contains(t))
            {
                dummyList.add(t);
                
                
                if(isDirector)
                {
                    sqlThread.insertRelacionDirigio(oldPerson,t);
                    sqlThread.prepareSelectParticipatingTitlesForDirector(oldPerson);
                }
                else
                {
                    sqlThread.insertRelacionActuo(oldPerson,t);
                    sqlThread.prepareSelectParticipatingTitlesForArtist(oldPerson);
                }
                
        
                
            }
                //
//                titlesTable.getItems().add(t);
//            updateComboList(t.toString());
        }
        
    }
    private void updateComboList(String s){
        if(s!=null)
            sqlThread.prepareStatementSelectComboTitles(s);
        
    }
    
    public void updateComboList(){
        
        
        String s = titlesComboBox.getEditor().getText();
        updateComboList(s);
        
        
        
    }
    
    private void updateCellFactoryForTables(){
     t_nameCol.setCellValueFactory(new PropertyValueFactory("name"));
    t_relDateCol.setCellValueFactory(new PropertyValueFactory("releaseDate"));
    t_genereCol.setCellValueFactory(new PropertyValueFactory("genere"));
    t_summaryCol.setCellValueFactory(new PropertyValueFactory("summary"));
    
//        titlesTable.getSelectionModel().setSelectionMode(
//                 SelectionMode.MULTIPLE
//        );
    }
    
    
    
    
    void setDelegate(MainViewController aThis) {
        
        delegate=aThis;
       
    }

    void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
        
        sqlThread.setTitlesComboBox(titlesComboBox);
        sqlThread.setTitlesTable(titlesTable);
        
//        sqlThread.prepareSelectParticipatingTitles(null);
    }
    
    

    void setPerson(Person p) {
        
        if(p==null){
            isDummyPerson = true;
//         
        java.sql.Date sqlDate;
        LocalDate ld = LocalDate.of(2000, Month.MARCH, 1);
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
             sqlDate = new java.sql.Date(d.getTime());
             
             
            Person k = new Person(-1,"","","",sqlDate);
            
            
            if(isDirector)
                
               oldPerson =  sqlThread.resolveDirectorID(k);
            else
                
               oldPerson =  sqlThread.resloveArtistID(k);
                
                
            //MAKE IT NEW.
            
            //GET IT BACK WITH ITS NEW ID...
        }
        else{
            
            isDummyPerson = false;
            
            firstName.setText(p.getFirstName());
            lastName.setText(p.getLastName());
            biography.setText(p.getBiography());
            date.setValue(p.getBirthDate().toLocalDate());
            oldPerson = p;
        
            if(isDirector)
                sqlThread.prepareSelectParticipatingTitlesForDirector(p);
            else
                sqlThread.prepareSelectParticipatingTitlesForArtist(p);
        }
        
    }

    void setIsDirector(boolean director) {
        this.isDirector = director;
    }

    
}
