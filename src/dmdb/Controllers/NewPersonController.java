
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 *
 * @author Alfonso
 */
public class NewPersonController implements Initializable {
    
    private boolean isDirector;
    private Person oldPerson = null;
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
        if(ld==null)
            return;
        java.sql.Date sqlDate = null;
//        if(ld != null)
        {Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
             sqlDate = new java.sql.Date(d.getTime());
             
        }
        
        
        int id = 0;
        if(oldPerson!=null)
            id = oldPerson.getPersonID();
       Person p = new Person(id,firstName.getText(),lastName.getText(),biography.getText(),sqlDate); 
       
       String kindName = "Artist";
       if(isDirector)
           kindName = "Director";
        
        if(oldPerson!=null)
        {
            sqlThread.updatePersonKind(p,kindName);
        }else 
            sqlThread.insertPersonKind(p,kindName);

         
       if(delegate!=null)
            delegate.close(); 
       sqlThread =null;
    }
     
     public void delete(){
         
         
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        date.setValue(LocalDate.of(2000, Month.JANUARY, 1));
    }

    
    
    
    
    
    
    
    void setRegisterDelegate(MainViewController aThis) {
        
        delegate=aThis;
       
    }

    void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
    }

    void setPerson(Person p) {
        
        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        biography.setText(p.getBiography());
        date.setValue(p.getBirthDate().toLocalDate());
        oldPerson = p;
        
        
    }

    void setIsDirector(boolean director) {
        this.isDirector = director;
    }

    
}
