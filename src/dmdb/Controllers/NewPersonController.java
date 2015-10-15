
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;

import dmdb.Registers.Person;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


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
    
    
    
    public void cancel(){
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

         
       cancel();
    }
     
     public void delete(){
         
         List items =  new ArrayList (titlesTable.getSelectionModel().getSelectedItems());  
                    titlesTable.getItems().removeAll(items);
//                    for(Object p : items ){
////                        sqlThread.prepareStatementDelete("Title", (Title) p);
//                    }
                    
                    titlesTable.getSelectionModel().clearSelection();
         
         
     }
    
    @FXML
    private ComboBox<Title> titlesComboBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.updateCellFactoryForTables();
       
    } 

    public void selectComboItem(){
        
        Object o = titlesComboBox.getValue();
        if(o instanceof Title)
        {   Title t = (Title)o;
        ;
            if(!titlesTable.getItems().contains(t))
                titlesTable.getItems().add(t);
            updateComboList(t.toString());
        }
        
    }
    private void updateComboList(String s){
        if(s!=null)
            sqlThread.prepareStatementSelectComboTitles(s);
        
    }
    
    public void updateComboList(){
        
        
        String s = titlesComboBox.getEditor().getText();
        updateComboList(s);
        
        
//        Title t = titlesComboBox.getSelectionModel().getSelectedItem();
//        
////        titlesTable.getItems().removeAll(titlesTable.getItems());
//        titlesTable.getItems().add(t);
//        System.out.println("Update" + s);
    }
    
    private void updateCellFactoryForTables(){
     t_nameCol.setCellValueFactory(new PropertyValueFactory("name"));
    t_relDateCol.setCellValueFactory(new PropertyValueFactory("releaseDate"));
    t_genereCol.setCellValueFactory(new PropertyValueFactory("genere"));
    t_summaryCol.setCellValueFactory(new PropertyValueFactory("summary"));
    
//    titlesComboBox.setCe
    
//    titlesTable.setOnMousePressed((MouseEvent event) -> {
//            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
//                Node node = ((Node) event.getTarget()).getParent();
//                TableRow row;
//                if (node instanceof TableRow) {
//                    row = (TableRow) node;
//                } else {
//                    // clicking on text part
//                    row = (TableRow) node.getParent();
//                }
////                try {
//////                    editTitleRegister((Title)row.getItem());
////                } catch (IOException ex) {
////                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//          });
        titlesTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
    }
    
    
    
    
    void setRegisterDelegate(MainViewController aThis) {
        
        delegate=aThis;
       
    }

    void setSQLThread(SQLThread sqlThread) {
        this.sqlThread = sqlThread;
        
        sqlThread.setTitlesComboBox(titlesComboBox);
        sqlThread.setTitlesTable(titlesTable);
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
