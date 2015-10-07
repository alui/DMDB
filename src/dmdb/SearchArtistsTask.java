/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.*;


/**
 *
 * @author Alfonso
 */
public class SearchArtistsTask extends Task<Void> {
    
    ObservableList<RegisterArtist> obs;
    String  search;
    SearchArtistsTask(ObservableList<RegisterArtist> r , String text, Connection c)
    {
        super();
        obs = r;
        search=text;
        
    }
    
    @Override
    protected Void call() throws Exception {
                
        obs.removeAll(obs);
                 ObservableList<RegisterArtist> newObs = FXCollections.observableArrayList();
        RegisterArtist r;
        r = new RegisterArtist("Jules","Wrothington");
        obs.add(r);
        r  = new RegisterArtist("Opeal","Seuma");
        obs.add(r);       
                
                
                //Perfomr sql statement with text.
                
                //In run later.
                //Remove all children from current obs, and then add all new items.
                
                
            
            //            stmt.executeUpdate(sql);
                
                return null;
       }
        
    
    
    
    
}
