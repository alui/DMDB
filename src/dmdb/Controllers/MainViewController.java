
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextField;
import javafx.scene.control.TableView;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**
 *
 * @author Alfonso
 */
public class MainViewController implements Initializable, RegisterDelegate,SearchDelegate {
    
    
    
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

    //Tables. ARTISTS
    @FXML
    private TableView<Person> artistsTable;
    
    @FXML
    private TableColumn<Person,String> a_firstNameCol;
    @FXML
    private TableColumn<Person,String> a_lastNameCol;
    @FXML
    private TableColumn<Person,String> a_birthDateCol;
    @FXML
    private TableColumn<Person,String> a_bioCol;


//
//    
    @FXML
    private TableView<Title> titlesTable;
    
             @FXML 
             private TableColumn<Title,String> t_nameCol;
             @FXML 
             private TableColumn<Title,String> t_relDateCol;
             
             @FXML 
             private TableColumn<Title,String> t_summaryCol;
             
             @FXML 
             private TableColumn<Title,String> t_genereCol;
             
//    
             
             
             
             
    @FXML
    private TableView<Person> directorsTable;
    @FXML
    private TableColumn<Person,String> d_firstNameCol;
    @FXML
    private TableColumn<Person,String> d_lastNameCol;
    @FXML
    private TableColumn<Person,String> d_birthDateCol;
    @FXML
    private TableColumn<Person,String> d_bioCol;
    
    
    
    
    private SQLThread sqlThread;
    
    //Esto no deberia estar aqui... pero me ahorra tiempo.
    static final protected String DbURL = "jdbc:sqlite:movie.db";
    static final protected String DbPassword= "password";
    static final protected String DbUser ="username";
    static final protected String DbJDBC ="org.sqlite.JDBC";  
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //Set up Sql Thread.
        sqlThread = new SQLThread( DbUser,DbPassword,DbURL,DbJDBC );
        sqlThread.setDaemon(true);
        sqlThread.setName("SQL-Thread");
        sqlThread.setArtistsTable(artistsTable);
        sqlThread.setDirectorsTable(directorsTable);
        sqlThread.setTitlesTable(titlesTable);
        sqlThread.start();
        
        //The DBConneciton will be performed by this mainViewController.
//        connectDB();
//        connect the TableViews.
        setCellFactoryForColumns();
        //Perfomred by the FXML view.
        
        
        
        //Set choiseBox Ready.
        choiceBox.setItems(FXCollections.observableArrayList("All", "Artists", "Titles", "Directors"));
        choiceBox.getSelectionModel().selectFirst();
//        
//        
//        The AdvanceSearchController should be loaded before hand, with the DBconneciton, and so its delegate.
//        The ApplicationDelegate will be more focused on to dealing with the system.
//        Code before this is to prepare independecy for the other methods.
        //------------------------***------------------//
        
        
        
        
        
        //This applies to both search engines.
        
        //The tableViews ObservableList i think will have to be syncronized. Since I will be modifying it from multiple threads.
        //Seraches have to be cancelable. Twice the secutiry.
        
        //******Set listener for search button and <Enter>
//        searchBox.setOnAction(e -> performSimpleSearch());
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
        
        
        //Show all
        
        
    }
    private void hello(String g, String h){}
    public void delete(){
//        synchronized(sqlThread){
         this.deleteRowsFromCurrentVisibleTable();
//        }
    }
    private void deleteRowsFromCurrentVisibleTable(){
        if(tabTitles.isSelected()){
                    List items =  new ArrayList (titlesTable.getSelectionModel().getSelectedItems());  
                    titlesTable.getItems().removeAll(items);
                    
//                    
                    sqlThread.addDelete(SQLThread.prepareDelete("Actuo", "Title", items));
                    sqlThread.addDelete(SQLThread.prepareDelete("Dirigio", "Title", items));
                    sqlThread.addDelete(SQLThread.prepareDelete("Titles", "Title", items));
                    
                    sqlThread.wakeQueue();
                    titlesTable.getSelectionModel().clearSelection();
            
            
        }else if (tabArtists.isSelected()){
            
                    List items =  new ArrayList (artistsTable.getSelectionModel().getSelectedItems());
                    artistsTable.getItems().removeAll(items);
                    sqlThread.addDelete(SQLThread.prepareDelete("Actuo", "Artist", items));
                    sqlThread.addDelete(SQLThread.prepareDelete("Artists", "Artist", items));
                    sqlThread.wakeQueue();
                    artistsTable.getSelectionModel().clearSelection();
                    
            
        }else if (tabDirectors.isSelected()){
            
            
                    List items =  new ArrayList (directorsTable.getSelectionModel().getSelectedItems());  
                    directorsTable.getItems().removeAll(items);
                    
                    sqlThread.addDelete(SQLThread.prepareDelete("Dirigio", "Director", items));
                    sqlThread.addDelete(SQLThread.prepareDelete("Directors", "Director", items));
                    sqlThread.wakeQueue();
                    directorsTable.getSelectionModel().clearSelection();
        }
        
    }
    
//    SearchDirectorsTask
    private void performSearch(){
        
//        
//        artistsTable.setDisable(true);
//         tabPane.setDisable(true);
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
                 
                
                sqlThread.selectArtists(searchBox.getText());
                sqlThread.selectDirectors(searchBox.getText());
                sqlThread.selectTitles(searchBox.getText());
                
                break;
            case "Titles":
                if (!tabs.contains(tabTitles))
                    tabPane.getTabs().add(tabTitles);
                tabPane.getTabs().remove(tabArtists);
                tabPane.getTabs().remove(tabDirectors);
                
                
                sqlThread.selectTitles(searchBox.getText());
                
                
                break;
            case "Directors":
                
                if (!tabs.contains(tabDirectors))
                    tabPane.getTabs().add(tabDirectors);
                tabPane.getTabs().remove(tabTitles);
                tabPane.getTabs().remove(tabArtists);
                
                sqlThread.selectDirectors(searchBox.getText());
                
                break;
            case "Artists":
                
                if (!tabs.contains(tabArtists))
                    tabPane.getTabs().add(tabArtists);
                tabPane.getTabs().remove(tabTitles);
                tabPane.getTabs().remove(tabDirectors);
                sqlThread.selectArtists(searchBox.getText());

                
                break;
        }
    }
    public void performSimpleSearch(){
//        synchronized(sqlThread){
            this.performSearch();
//        }
        
    }
    
   
    
    private void setCellFactoryForColumns(){
        
        
        //+++++++++ARTTISTS
        
    a_firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    a_lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
    a_birthDateCol.setCellValueFactory(new PropertyValueFactory("birthDate"));
    a_bioCol.setCellValueFactory(new PropertyValueFactory("biography"));
    
 
//        tableArtists.getColumns().setAll(firstNameCol, lastNameCol,dateCol);
        
//        ObservableList<Person> obs = FXCollections.observableArrayList();
//        Person r = new Person("Sam","Wrothington");
               
                
//        obs.add(r);
//        tableArtists.setItems(obs);
//        tabArtists.setContent(tableArtists);
        
        artistsTable.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                
                try {
                    this.editPersonRegister((Person)row.getItem(),false);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          });
        
         artistsTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
        
        
        //+++++++++DIRECTORS
        
//        tableDirectors= new TableView<>();
          
   
    d_firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    d_lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
    d_bioCol.setCellValueFactory(new PropertyValueFactory("biography"));
    d_birthDateCol.setCellValueFactory(new PropertyValueFactory("birthDate"));
   
        directorsTable.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                try {
                    editPersonRegister((Person)row.getItem(),true);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          });
        directorsTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
//        ObservableList<Person> obs = FXCollections.observableArrayList();
//        Person r = new Person("Sam","Wrothington");
//               
                
//        obs.add(r);
//        tableDirectors.setItems(obs);
//        tabDirectors.setContent(tableDirectors);
        
        
        //+++++++++TTLES
//        movieColumnTitle = new TableColumn<>("Name");
        
    t_nameCol.setCellValueFactory(new PropertyValueFactory("name"));
    t_relDateCol.setCellValueFactory(new PropertyValueFactory("releaseDate"));
    t_genereCol.setCellValueFactory(new PropertyValueFactory("genere"));
    t_summaryCol.setCellValueFactory(new PropertyValueFactory("summary"));
    
    titlesTable.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                try {
                    editTitleRegister((Title)row.getItem());
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          });
        titlesTable.getSelectionModel().setSelectionMode(
                 SelectionMode.MULTIPLE
        );
    
    
        
        
      
        
        
    }

        
      private void newTitle() throws IOException {
        
        this.editTitleRegister(null);
        
    }
    private void editTitleRegister(Title t) throws IOException {
        

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewTitle.fxml"));     

        AnchorPane n = (AnchorPane)fxmlLoader.load(); 
        NewTitleController controllerT = fxmlLoader.<NewTitleController>getController();
        controllerT.setRegisterDelegate(this);
        controllerT.setSQLThread(sqlThread);
        controllerT.setTitle(t);
        

        lastTemporaryNode = n;
        mainPane.getChildren().remove(searchBorderPane);
        mainPane.getChildren().add(n);
    
    }
    private void newPerson(boolean isDirector) throws IOException {
        this.editPersonRegister(null, isDirector);
        
    }
    private void editPersonRegister(Person p , boolean isDirector) throws IOException {
        

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewPerson.fxml"));     

        AnchorPane n = (AnchorPane)fxmlLoader.load(); 
        NewPersonController controllerP = fxmlLoader.<NewPersonController>getController();
        controllerP.setDelegate(this);
        controllerP.setSQLThread(sqlThread);        
        controllerP.setIsDirector(isDirector);
        controllerP.setPerson(p);
        

        lastTemporaryNode = n;
        mainPane.getChildren().remove(searchBorderPane);
        mainPane.getChildren().add(n);
    
    }
    
    public void newDirectorRegister(){
        try {
            newPerson(true);
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void newArtistRegister(){
        try {
            newPerson(false);
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    public void newTitleRegister(){
        try {
            newTitle();
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private AnchorPane searchView;
    private  AdvancedSearchViewController controller;
    public void advancedSearch() throws IOException{
        
        
        //The following code is a quick fix.. but very bad indeed.
//                sqlThread.selectArtists("!");
                sqlThread.selectDirectors("!");
//                sqlThread.selectTitles("!");
                
        if(searchView ==null){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdvancedSearchView.fxml"));     

        searchView = (AnchorPane)fxmlLoader.load(); 
//        
        controller = fxmlLoader.<AdvancedSearchViewController>getController();
        } 
        controller.setDelegate(this);
        controller.setSQLThread(sqlThread);   
        
        lastTemporaryNode = searchView;
        mainPane.getChildren().remove(searchBorderPane);
        mainPane.getChildren().add(searchView);
        
    }
    
  

    
    //Methods from newController
    
//    @Override
    @Override
    public void newDirector(Register r) {
        directorsTable.getItems().add((Person)r);
    }
    @Override
    public void newArtist(Register r) {
        
        artistsTable.getItems().add((Person)r);
    }
    @Override
    public void newMovie(Register r) {
        
        titlesTable.getItems().add((Title)r);
    }
    @Override
    public void editedDirector(Register r) {
        ObservableList<Person> list = directorsTable.getItems();
        int i = list.indexOf((Person)r);
        list.set(i, (Person)r);
         directorsTable.getColumns().get(0).setVisible(false);
        directorsTable.getColumns().get(0).setVisible(true);
        
        
        
        
    }
    @Override
    public void editedArtist(Register r) {
        
        int i = artistsTable.getItems().indexOf((Person)r);
        
        artistsTable.getItems().set(i, (Person)r);
        
         artistsTable.getColumns().get(0).setVisible(false);
        artistsTable.getColumns().get(0).setVisible(true);
    }
    
    @Override
    public void editedMovie(Register r) {
        
        int i = titlesTable.getItems().indexOf((Title)r);
        
        titlesTable.getItems().set(i, (Title)r);
        titlesTable.getColumns().get(0).setVisible(false);
        titlesTable.getColumns().get(0).setVisible(true);
        
    }
    
    @Override
    public void close() {
        
        if(lastTemporaryNode!=null)
        {
            mainPane.getChildren().remove(lastTemporaryNode);
           
        }
        mainPane.getChildren().add(searchBorderPane);
        
        
        sqlThread.setArtistsTable(artistsTable);
        sqlThread.setDirectorsTable(directorsTable);
        sqlThread.setTitlesTable(titlesTable);
        
//        synchronized(sqlThread){
//            sqlThread.reload();
//        }

    }
    @Override 
    public void closeAdvancedSearch(){
        
        
        if(lastTemporaryNode!=null)
        {
            mainPane.getChildren().remove(lastTemporaryNode);
           
        }
        mainPane.getChildren().add(searchBorderPane);
        
        
        
    }

    @Override
    public void artistSearch() {
        
        choiceBox.getSelectionModel().select("Artists");
        ObservableList<Tab> tabs =  tabPane.getTabs();
                if (!tabs.contains(tabArtists))
                    tabPane.getTabs().add(tabArtists);
                tabPane.getTabs().remove(tabTitles);
                tabPane.getTabs().remove(tabDirectors);
                
     }
    
    
}
