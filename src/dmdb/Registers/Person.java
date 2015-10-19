/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmdb.Registers;

//import dmdb.Register;

import java.sql.Date;




//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;

/**
 *
 * @author Alfonso
 */
public class Person extends Register {
//    
        private Integer personID;
        
    private String firstName;
    private String lastName;
    
    
    private String biography;
    
    private Date birthDate;
    

    
    
//    
    
//    
//     private StringProperty firstName;
//     public void setFirstName(String value) { firstNameProperty().set(value); }
//     public String getFirstName() { return firstNameProperty().get(); }
//     public StringProperty firstNameProperty() { 
//         if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
//         return firstName; 
//     }
// 
//     private StringProperty lastName;
//     public void setLastName(String value) { lastNameProperty().set(value); }
//     public String getLastName() { return lastNameProperty().get(); }
//     public StringProperty lastNameProperty() { 
//         if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
//         return lastName; 
//     }
     
    public Person(){
        personID = null;
        firstName = "";
        lastName="";   
        biography = "";
        birthDate = null;
        
        
    }
    
    public Person(Integer id, String f, String l, String b, Date d){
        
        personID = id;
        firstName = f;
        lastName=l;   
        biography = b;
        birthDate = d;     
        
    }
    
    
    public String getBiography(){return biography;};
    public void setBiography(String f){ biography = f;};
    
    public String getFirstName(){return firstName;};
    public void setFirstName(String f){ firstName = f;};
    
    
    public String getLastName(){return lastName;};
    public void setLastName(String f){ lastName = f;};

    
    public Integer getPersonID(){return personID;};
    public void setPersonID(Integer f){ personID = f;};
    
    
    public Date getBirthDate(){return birthDate;};
    public void getBirthDate(Date f){ birthDate = f;};

    @Override
    public int getID() {
        return this.getPersonID();
    }

   
    
    @Override
    public String toString(){
        return firstName + " "+lastName;
    }
}
