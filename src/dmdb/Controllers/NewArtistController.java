
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;

import dmdb.Registers.Person;
import dmdb.Thread.SQLThread;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//
//import javafx.scene.control.Label;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//
//import javafx.scene.control.TextField;
//import javafx.scene.control.TableView;
//
//
//import java.sql.*;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.MenuButton;
//import javafx.scene.control.SingleSelectionModel;
//import javafx.scene.control.SplitPane;
//import javafx.scene.control.Tab;
//import javafx.scene.control.TabPane;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.cell.PropertyValueFactory;


/**
 *
 * @author Alfonso
 */
public class NewArtistController implements Initializable {
    
    private MainViewController delegate;
    private SQLThread sqlThread;
    
    
    @FXML
    private TextField firstName;
    
    
    @FXML
    private TextField lastName;
    
    @FXML
    private TextArea biography;
    
    @FXML
    private DatePicker date;
    
    
    public void cancel(){
        if(delegate!=null)
            delegate.close();   
        sqlThread =null;
    }
     public void save(){
         if(firstName.getText()==null || firstName.getText().equals(""))
             return;

         
        LocalDate ld = date.getValue();
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(d.getTime());
        System.out.print(sqlDate);
        
        
       Person p = new Person(0,firstName.getText(),lastName.getText(),biography.getText(),sqlDate); 
       
       sqlThread.insertArtist(p);

         
       if(delegate!=null)
            delegate.close(); 
       sqlThread =null;
    }
     
     public void delete(){
         
         
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    
    
    
    
    
    
    
    void setRegisterDelegate(MainViewController aThis) {
        
        delegate=aThis;
       
    }

    void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
    }
    
}
