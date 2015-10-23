

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;


import dmdb.Registers.Person;
import javafx.fxml.FXML;
import dmdb.Registers.Title;
import dmdb.Thread.SQLThread;
import java.net.URL;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Alfonso
 */

public class NewTitleController implements Initializable {
    
    
    private Title oldTitle = null;
    private RegisterDelegate delegate;
    private SQLThread sqlThread;
    
    @FXML
    private ComboBox directorCombo;
    
    @FXML
    private ComboBox artistsCombo;
    
    
    @FXML
    private TextField name;
    
    @FXML
    private TextArea biography;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private ChoiceBox genereBox;
    
    
    @FXML
    private TableView<Person> artistsTable;
    
    @FXML
    private TableColumn<Person,String> a_firstNameCol;
    @FXML
    private TableColumn<Person,String> a_lastNameCol;
    
    
    @FXML
    private TableView<Person> directorsTable;
    @FXML
    private TableColumn<Person,String> d_firstNameCol;
    @FXML
    private TableColumn<Person,String> d_lastNameCol;
    
    
    
    private ObservableList<Person> directorsObs;
    
    private ObservableList<Person> toDeleteDirectorsObs;
    
    private ObservableList<Person> artistsObs;
    
    private ObservableList<Person> toDeleteArtistsObs;
    
    private boolean isNewTitle;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
        genereBox.setItems(FXCollections.observableArrayList("Adventure","Comedy","Crime","Fantasy","Historical","Mystery","Paranoid","Political","Romance","Saga","Satire","Science fiction","Thriller"));

        date.setValue(LocalDate.of(2000, Month.JANUARY, 1));
        
         directorsObs = FXCollections.observableArrayList();
         artistsObs = FXCollections.observableArrayList();
         
         toDeleteDirectorsObs = FXCollections.observableArrayList();
         toDeleteArtistsObs = FXCollections.observableArrayList();
         isNewTitle=true;
        
    
    
        setCellFactoryForColumns();
        
    }
    
    
    public void save (){
        this.innerSave();
        
        
    }
    private void innerSave(){
        
        LocalDate ld = date.getValue();
        if(name.getText()==null || name.getText().equals("") || ld == null)
             return;

         
        java.sql.Date sqlDate;
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        sqlDate = new java.sql.Date(d.getTime());
        String genereString = (String) genereBox.getSelectionModel().selectedItemProperty().get();
        
        
        Title t;
       
       if(isNewTitle){
           
            t= new Title(0,name.getText(),biography.getText(),sqlDate,genereString);             
            t = sqlThread.offResolveTitleID(t);
            delegate.newMovie(t);
            
       }else{
            t = new Title(oldTitle.getTitleID(),name.getText(),biography.getText(),sqlDate,genereString);    
            sqlThread.addUpdateTitle(t);
            delegate.editedMovie(t);
       }
       
       for(Person director : directorsObs)
            sqlThread.addRelacionDirigio(director, t);
       for(Person artist : artistsObs)
           sqlThread.addRelacionActuo(artist, t);
       
       
       
       for(Person director : toDeleteDirectorsObs)
           sqlThread.addDeleteRelacionDirigio(t,director);
       
       for(Person artist : toDeleteArtistsObs)
           sqlThread.addDeleteRelacionActuo(t, artist);
       
       sqlThread.wakeQueue();
       
         
       if(delegate!=null)
            delegate.close(); 
       sqlThread =null;
    
    }
    
    public void cancel(){
        if(delegate!=null)
            delegate.close();   
        sqlThread =null;
    }
    
     
    public void deleteDirector(){
        
         List items =  new ArrayList (directorsTable.getSelectionModel().getSelectedItems());  
                    directorsTable.getItems().removeAll(items);
                    
                    for(Object p : items ){
                        if(directorsObs.contains((Person)p))
                            directorsObs.remove((Person)p);
                        else
                            
                            toDeleteDirectorsObs.add((Person)p);
                            

                    }
                    
                    directorsTable.getSelectionModel().clearSelection();
         
         
     }
    
    public void deletedArtist(){

         List items =  new ArrayList (artistsTable.getSelectionModel().getSelectedItems());  
                    artistsTable.getItems().removeAll(items);
                    
                    for(Object p : items ){
                        
                        if(artistsObs.contains((Person)p))
                            artistsObs.remove((Person)p);
                        else
                            toDeleteArtistsObs.add((Person)p);

                    }
                    
                    artistsTable.getSelectionModel().clearSelection();
    }
    
    
    public void setRegisterDelegate(RegisterDelegate aThis) {
        
        delegate=aThis;
       
    }

    public void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
        
        
        
        sqlThread.setArtistsComboBox(artistsCombo);
        sqlThread.setArtistsTable(artistsTable);
        
        sqlThread.setDirectorComboBox(directorCombo);
        sqlThread.setDirectorsTable(directorsTable);
        
        
        
        
    }
    public void setTitle(Title t){
        
        oldTitle = t;
        if(t != null)
        {
            isNewTitle =false;
            name.setText(t.getName());
            biography.setText(t.getSummary());
            date.setValue(t.getReleaseDate().toLocalDate());
            genereBox.getSelectionModel().select(t.getGenere());
            
            sqlThread.selectDirectorsForTitle(oldTitle);
            sqlThread.selectArtistsForTitle(oldTitle);
            
            
        }else
        {
            isNewTitle=true;
            
            
        }
        
        
    }
    
    public void selectDirectorComboItem(){
        Object o = directorCombo.getValue();
//        titlesComboBox.getEditor().selectAll();
        
        if(o instanceof Person)
        {   Person  p = (Person)o;
        
            
            if(toDeleteDirectorsObs.contains(p))
                toDeleteDirectorsObs.remove(p);
            
            if(!directorsTable.getItems().contains(p))
            {
                directorsObs.add(p);
                directorsTable.getItems().add(p);
                
            }
        }
        
    }
    
    
    public void selectArtistComboItem(){
        Object o = artistsCombo.getValue();
        
        if(o instanceof Person)
        {   Person  p = (Person)o;
        
        
            if(toDeleteArtistsObs.contains(p))
                toDeleteArtistsObs.remove(p);
            
            if(!artistsTable.getItems().contains(p))
            {
                artistsObs.add(p);
                artistsTable.getItems().add(p);
            }
        }
        
    }
    
    
    private void updateDirectorComboList(String s){
        if(s!=null)
            sqlThread.selectComboDirectors(s);
    }
    
    public void updateDirectorComboList(){
        String s = directorCombo.getEditor().getText();
        updateDirectorComboList(s);
    }
    
    private void updateArtistComboList(String s){
        if(s!=null)
            sqlThread.selectComboArtists(s);
    }
    
    public void updateArtistComboList(){
        String s = artistsCombo.getEditor().getText();
        updateArtistComboList(s);
    }
    
    
    private void setCellFactoryForColumns(){
        
        a_firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        a_lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
        
        
        d_firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        d_lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
        
        artistsTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
        directorsTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
    }
    
}
