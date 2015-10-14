

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;


import javafx.fxml.FXML;
import dmdb.Registers.Title;
import dmdb.Thread.SQLThread;
import java.net.URL;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Alfonso
 */

public class NewTitleController implements Initializable {
    
    
    private Title oldTitle = null;
    private MainViewController delegate;
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
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
        genereBox.setItems(FXCollections.observableArrayList("Adventure","Comedy","Crime","Fantasy","Historical","Mystery","Paranoid","Political","Romance","Saga","Satire","Science fiction","Thriller"));

        date.setValue(LocalDate.of(2000, Month.JANUARY, 1));
        
        
    }
    
    public void delete(){}
    public void save(){
        
        LocalDate ld = date.getValue();
        
        if(name.getText()==null || name.getText().equals("") || ld == null)
             return;

         
        java.sql.Date sqlDate;
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        sqlDate = new java.sql.Date(d.getTime());
             
        String genereString = (String) genereBox.getSelectionModel().selectedItemProperty().get();
        
        
        
        int id = 0;
        if(oldTitle!=null)
            id = oldTitle.getTitleID();
       Title p = new Title(id,name.getText(),biography.getText(),sqlDate,genereString); 
       
        
        if(oldTitle!=null)
        {
            sqlThread.updateTitle(p);
        }else 
            sqlThread.insertTitle(p);

         
       if(delegate!=null)
            delegate.close(); 
       sqlThread =null;
    
    }
    
    public void cancel(){
        if(delegate!=null)
            delegate.close();   
        sqlThread =null;
    }
    
     
    public void setRegisterDelegate(MainViewController aThis) {
        
        delegate=aThis;
       
    }

    public void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
    }
    public void setTitle(Title t){
        oldTitle = t;
        
         
        name.setText(t.getName());
        biography.setText(t.getSummary());
        date.setValue(t.getReleaseDate().toLocalDate());
        genereBox.getSelectionModel().select(t.getGenere());
        
        
    }
    
}
