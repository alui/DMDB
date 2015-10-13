
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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextField;
import javafx.scene.control.TableView;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
//import javafx.scene.control.MenuButton;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**
 *
 * @author Alfonso
 */
public class MainViewController implements Initializable, RegisterDelegate {
    
    
    
    //Most important panes.
    @FXML 
    private AnchorPane mainPane;
    
    @FXML
    private BorderPane searchBorderPane;
    
    @FXML
    private TabPane tabPane;
    
    
    //Controls.
    @FXML
    private TextField searchBox;
    @FXML
    private ChoiceBox choiceBox;
    
    
    @FXML 
    private Tab tabTitles;
    
            
    @FXML 
    private Tab tabArtists;
    @FXML 
    private Tab tabDirectors;
    
    
    private Node lastTemporaryNode;

    
//    
//    @FXML 
//    private MenuButton newRegister;
////    

    //Tables.
//    @FXML
    private TableView<Person> tableArtists;
//
//    
    @FXML
    private TableView tableTitles;
    
    @FXML 
    private TableColumn<Title,String> movieColumnTitle;
//    
//    @FXML
    private TableView<Person> tableDirectors;
    
    
    private SQLThread sqlThread;
    
    
    
    
//    //Conneciton to the main Data Base.
//    private  Connection DBConnection;

    
    
        // JDBC driver name and database URL
//        static final String JDBC_DRIVER = "org.sqlite.JDBC";
//        static final String DB_URL = "jdbc:sqlite:movie.db";
//        
//        //  Database credentials
//        static final String USER = "username";
//        static final String PASS = "password";
    
    
    
    //Esto no deberia estar aqui... pero me ahorra tiempo.
    static final protected String DbURL = "jdbc:sqlite:movie.db";
    static final protected String DbPassword= "password";
    static final protected String DbUser ="username";
    static final protected String DbJDBC ="org.sqlite.JDBC";  
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //Set up Sql Thread.
        sqlThread = new SQLThread( DbUser,DbPassword,DbURL,DbJDBC );
        sqlThread.start();
        //The DBConneciton will be performed by this mainViewController.
//        connectDB();
//        connect the TableViews.
        connectTableViews();
        //Perfomred by the FXML view.
        
        
        
        //Set choiseBox Ready.
        choiceBox.setItems(FXCollections.observableArrayList("All", "Artists", "Titles", "Directors"));
        choiceBox.getSelectionModel().selectFirst();
//        
//        
        //The AdvanceSearchController should be loaded before hand, with the DBconneciton, and so its delegate.
        //The ApplicationDelegate will be more focused on to dealing with the system.
        //Code before this is to prepare independecy for the other methods.
        //------------------------***------------------//
        
        
        
        
        
        //This applies to both search engines.
        
        //The tableViews ObservableList i think will have to be syncronized. Since I will be modifying it from multiple threads.
        //Seraches have to be cancelable. Twice the secutiry.
        
        //******Set listener for search button and <Enter>
        searchBox.setOnAction(e -> performSimpleSearch());
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
//        newRegister.setOnAction(e -> newRegisterAction() );
            // NewRegisterInterface.
            // At the center of the application, should diable -not pause- any other button.
            //Will probably new interface to communicate back NewRegisterInterface.
            //Setting the DBconnection will be done dynamically.
            //And so will it be done for the NewSpeficicReigsterController. (Movies, Artists, Director).
            //Inside a thread.
            //Here The DBConnection will have to be synconized, since im going to modify it.
        
        //*****Set Listener for the Delete Button.
            //With Alert window.
            //On confirmation it will acces the syncronized DBConnection.
        
            //Inside a thread will performAll necesary Deletions.
            //After that, the latest serach statment will be sent again. To update the tableViews.
        
        
        //*****Set Listener for Edit Button.
            //HAVE TO LEARN MORE ABOUT TABLEVIEW EDITING AND SQL UPDATE.
            //Im thinking not to have a editing button in this controller.
            //But instead allow double clicking over table cells, to open a new/editing register.
        
    
    }
    
//    SearchDirectorsTask
    private void performSimpleSearch(){
        
         
        String s = (String) choiceBox.getSelectionModel().selectedItemProperty().get();
        ObservableList<Tab> tabs =  tabPane.getTabs();
        switch (s) {
            case "All":
//                
                if (!tabs.contains(tabTitles))
                    tabPane.getTabs().add(tabTitles);                
                if (!tabs.contains(tabArtists))
                    tabPane.getTabs().add(tabArtists);
                if (!tabs.contains(tabDirectors))
                    tabPane.getTabs().add(tabDirectors);
                 
                sqlThread.selectArtists(tableArtists.getItems(), searchBox.getText());
                sqlThread.selectDirectors(tableDirectors.getItems(), searchBox.getText());
                sqlThread.selectTitles(tableTitles.getItems(), searchBox.getText());
                
                break;
            case "Titles":
                if (!tabs.contains(tabTitles))
                    tabPane.getTabs().add(tabTitles);
                tabPane.getTabs().remove(tabArtists);
                tabPane.getTabs().remove(tabDirectors);
                
                sqlThread.selectTitles(tableTitles.getItems(), searchBox.getText());
                
                
                break;
            case "Directors":
                
                if (!tabs.contains(tabDirectors))
                    tabPane.getTabs().add(tabDirectors);
                tabPane.getTabs().remove(tabTitles);
                tabPane.getTabs().remove(tabArtists);
                
                sqlThread.selectDirectors(tableDirectors.getItems(), searchBox.getText());
                
                break;
            case "Artists":
                
                if (!tabs.contains(tabArtists))
                    tabPane.getTabs().add(tabArtists);
                tabPane.getTabs().remove(tabTitles);
                tabPane.getTabs().remove(tabDirectors);
                
                sqlThread.selectArtists(tableArtists.getItems(), searchBox.getText());

                
                break;
        }
//            
        
            
        
        
        
    }
    
   
    
    private void connectTableViews(){
        
        
        //+++++++++ARTTISTS
          tableArtists= new TableView<>();
          
          
    TableColumn<Person,String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    
    TableColumn<Person,String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
    
    
    TableColumn<Person,String> dateCol = new TableColumn<>("BirthDate");
    dateCol.setCellValueFactory(new PropertyValueFactory("birthDate"));
 
        tableArtists.getColumns().setAll(firstNameCol, lastNameCol,dateCol);
        
        ObservableList<Person> obs = FXCollections.observableArrayList();
//        Person r = new Person("Sam","Wrothington");
               
                
//        obs.add(r);
        tableArtists.setItems(obs);
        tabArtists.setContent(tableArtists);
        
        
        //+++++++++DIRECTORS
        
        tableDirectors= new TableView<>();
          
    firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    
   lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
 
        tableDirectors.getColumns().setAll(firstNameCol, lastNameCol);
        
//        ObservableList<Person> obs = FXCollections.observableArrayList();
//        Person r = new Person("Sam","Wrothington");
//               
                
//        obs.add(r);
//        tableDirectors.setItems(obs);
        tabDirectors.setContent(tableDirectors);
        
        
        //+++++++++TTLES
//        movieColumnTitle = new TableColumn<>("Name");
        
    movieColumnTitle.setCellValueFactory(new PropertyValueFactory("name"));
    
    
        
        
      
        
        
    }

    public void newArtistRegister() throws IOException {
            
          

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewArtist.fxml"));     

        AnchorPane n = (AnchorPane)fxmlLoader.load(); 
        NewArtistController controller = fxmlLoader.<NewArtistController>getController();
        controller.setRegisterDelegate(this);
        controller.setSQLThread(sqlThread);

        lastTemporaryNode = n;
        mainPane.getChildren().remove(searchBorderPane);
        mainPane.getChildren().add(n);
        
        
        
        
        
    }

    
    //Methods from new register
    @Override
    public void close() {
        
        if(lastTemporaryNode!=null)
        {
            mainPane.getChildren().remove(lastTemporaryNode);
        }
        mainPane.getChildren().add(searchBorderPane);
        
        
    }

//    @Override
//    public void doneEditing(Register r) {
//        
//    }
//    
//     @Override
//    public void createNewRegister(Register r) {
//        
//    }
    
    
    
}
