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
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Alfonso
 */
public class AdvancedSearchViewController implements Initializable {
    
    
    
    private SearchDelegate delegate;
    private SQLThread sqlThread;
    
    
    @FXML
    private TextField peopleLabel1;
    
    @FXML
    private TextField peopleLabel2;
    
    @FXML
    private ComboBox<Person> peopleComboBox1;
    
            
    @FXML
    private Label peopleComboBoxLabel1;
    private Person realPerson;
    
    
    @FXML
    private DatePicker peopleDatePicker1;
    
    @FXML
    private DatePicker peopleDatePicker2;
    
    
    public void searchPeople(){
        
        
        String firstName = peopleLabel1.getText();
        String lastName = peopleLabel2.getText();
        
        LocalDate ld1 = peopleDatePicker1.getValue();
        LocalDate ld2 = peopleDatePicker2.getValue();
        
        java.sql.Date sqlDate1 = null,sqlDate2 = null;
        Date d;
        if(ld1!=null){
         d = Date.from(ld1.atStartOfDay(ZoneId.systemDefault()).toInstant());
         
        sqlDate1 = new java.sql.Date(d.getTime());
        }
        if(ld2!=null){
        
           d = Date.from(ld2.atStartOfDay(ZoneId.systemDefault()).toInstant());
            sqlDate2 = new java.sql.Date(d.getTime());
        }
                
        sqlThread.selectArtistsAdvancedSearch(firstName,lastName,sqlDate1,sqlDate2,realPerson);
        delegate.artistSearch();
        cancel();
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        peopleDatePicker1.setValue(LocalDate.of(1999, Month.JANUARY, 1));
        
        peopleDatePicker2.setValue(LocalDate.of(2015, Month.JANUARY, 1));
        
        
        
        
    }
    
    
   private void updateComboList(String s){
        if(s!=null)
            sqlThread.selectComboDirectors(s);
        
    }
    
    public void updateComboList(){
        
        
//        System.out.println("Welcome");
        String s = peopleComboBox1.getEditor().getText();
        updateComboList(s);
        
        
        
    }
    
    

    public void selectComboItem(){
        Object o = peopleComboBox1.getValue();
//        titlesComboBox.getEditor().selectAll();
        
        if(o instanceof Person)
        {   Person t = (Person)o;
            realPerson = t;
            peopleComboBoxLabel1.setText(realPerson.toString());
            
        }
        
    }
    public void clear(){
        
            peopleComboBoxLabel1.setText("");
            peopleComboBox1.setValue(null);
            realPerson =null;
            
        
    }
            
    public void cancel(){
        
        if(delegate!=null)
            delegate.closeAdvancedSearch();   
        sqlThread =null;
    }

    void setDelegate(MainViewController aThis) {
        
        delegate = aThis;
    }

    void setSQLThread(SQLThread sqlThread) {
        this.sqlThread=sqlThread;
        
        sqlThread.setDirectorComboBox(peopleComboBox1);
    }
    
}
