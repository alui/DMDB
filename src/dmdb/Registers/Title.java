/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Registers;

//import java.sql.Date;

import dmdb.Registers.Register;
import java.awt.Image;
import java.sql.Date;



/**
 *
 * @author Alfonso
 */
public class Title extends Register {
    
    private Integer titleID;
    //Relase Dare
    private Date releaseDate;
    
    private String name;
    
    private Image coverImage;
    
    
    public Title(){
        titleID=null;
        name = "";
        releaseDate = null;
        coverImage = null;
        
             
        
    }
    
    public Title(Integer id,String f,Date d ){
        
        titleID=id;
        name = f;
        releaseDate = d;
        coverImage = null;
               
        
    }
    
    public String getName(){return name;};
    public void setName(String f){ name = f;};
    
    
    
    public Date getReleaseDate(){return releaseDate;};
    public void setReleaseDate(Date f){ releaseDate = f;};
    
    
    public Integer getTitleID(){return titleID;};
    public void setTitleID(Integer f){ titleID = f;};
    
    
    
    
}
