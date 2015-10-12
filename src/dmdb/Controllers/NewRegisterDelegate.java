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
public interface NewRegisterDelegate {
    
    //When the New Register Window should close.
    public void close();
    
    
    //
    public void doneEditing(Register r); 
}
