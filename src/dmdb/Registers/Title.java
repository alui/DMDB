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
import java.util.Objects;



/**
 *
 * @author Alfonso
 */
public class Title extends Register {
    
    private Integer titleID;
    //Relase Dare
    private Date releaseDate;
    
    private String name;
    private String genere;
    
    private String summary;
    private Image coverImage;
    
    
    public Title(){
        titleID=null;
        name = "";
        releaseDate = null;
        coverImage = null;
        
             
        
    }
    
    public Title(Integer id,String f,String sum,Date d ,String genere){
        
        titleID=id;
        name = f;
        releaseDate = d;
        summary  = sum;
        coverImage = null;
        this.genere = genere;
               
        
    }
    
    public String getName(){return name;};
    public void setName(String f){ name = f;};
    
    
    
    public String getGenere(){return genere;};
    public void setGenere(String f){ genere = f;};
    
    public String getSummary(){return summary;};
    public void setSummary(String f){ summary = f;};
    
    
    public Date getReleaseDate(){return releaseDate;};
    public void setReleaseDate(Date f){ releaseDate = f;};
    
    
    public Integer getTitleID(){return titleID;};
    public void setTitleID(Integer f){ titleID = f;};

    @Override
    public int getID() {
        return this.getTitleID();
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o ){
        boolean isEqual = false;
        if(o instanceof Title){
            Title t = (Title)o;
            if(Objects.equals(t.titleID, this.titleID))
                isEqual=true;
        }
        return isEqual;
    }
    
    
}
