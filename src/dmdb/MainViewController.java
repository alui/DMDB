/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextField;
import javafx.scene.control.TableView;

/**
 *
 * @author Alfonso
 */
public class MainViewController implements Initializable {
    
    
    //Controls.
    @FXML
    private TextField searchBox;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private ChoiceBox choiceBox;
    
    @FXML
    private Button advancedSearch;
    
    //Tables.
    @FXML
    private TableView tableArtists;

    
    @FXML
    private TableView tableTitles;
    
    @FXML
    private TableView tableDirectors;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //The AdvanceSearchController should be loaded before hand, with the DBconneciton, and so its delegate.
        //The connection to the dataBase will be performed by this mainViewController.
        
        //Code before this is to prepare independecy for the other methods.
        //------------------------***------------------//
        
        
        //The ApplicationDelegate will be more focused on to dealing with the system.
        
        
        //This applies to both search engines.
        //The tableViews ObservableList i think will have to be syncronized. Since I will be modifying it from multiple threads.
        //Seraches have to be cancelable. Twice the secutiry.
        
        //******Set listener for search button.
            //With that text search for Movies, Directors, Artists in same service. Hence im going to be using multiple simple searches.
        
            //Such service shall update the tables.
            //It has to know about: DBConnection, choiseBox, tableViews, searchText.
            //And be cancelable for new searches.
            //It will only do the search for the current choiseBox.
        
        //*****Set listener for Advanced SearchButton.
            //This will load the AdvanceSerachController On top of the root view BorderPane at the center. Disabling the top bar.
            //There will be a thight cmounication between these two controllers. In fact, an interface will do. For the AdvaceSearchController will ask for in AdvancedSerachInterface;
                    //*where to update to. (Which table View).
                    //*And when is done searching. So the controller can be hidden.
        

                    //*Aside from the AdvancedSerachInterface. This main controller has to be able to cancel any searches getting performed by ASController.
                    //Just as it would with a simple serach.
        
        //*****Set listener for New Button.
        
            // NewRegisterInterface.
            // At the center of the application, should diable -not pause- any other button.
            //Will probably new interface to communicate back NewRegisterInterface.
            //Setting the DBconnection will be done dynamically.
            //And so will it be done for the NewSpeficicReigsterController. (Movies, Artists, Director).
            //Inside a thread.
            //Here The DBConnection will have to be synconized, since im going to modify it.
        
        //*****Set Listeer for the Delete Button.
            //With Alert window.
            //On confirmation it will acces the syncronized DBConnection.
        
            //Inside a thread will performAll necesary Deletions.
            //After that, the latest serach statment will be sent again. To update the tableViews.
        
        
        //*****Set Listener for Edit Button.
            //HAVE TO LEARN MORE ABOUT TABLEVIEW EDITING AND SQL UPDATE.
        
        
        
        
        
        
    }
    
}
