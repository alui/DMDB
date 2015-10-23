/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Controllers;

import dmdb.Registers.Register;

/**
 *
 * @author Alfonso
 */
public interface RegisterDelegate {
    
    //When the New Register Window should close.
    public void close();
    
    public void newDirector(Register r);
    public void newArtist(Register r);
    
    public void newMovie(Register r);
    
    public void editedDirector(Register r);
    
    public void editedArtist(Register r);
    
    public void editedMovie(Register r) ;
   
//    
//    public void doneEditing(Register r); 
//    
//    public void createNewRegister(Register r);
}
