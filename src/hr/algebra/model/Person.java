/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author bisiv
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "firstName", "lastName", "picturePath", "isDirector"})
public class Person implements Comparable<Person>{
    private int id;
    private String firstName;
    private String lastName;
    private String picturePath;
    private boolean isDirector;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, String picturePath, boolean isDirector) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.picturePath = picturePath;
        this.isDirector = isDirector;
    }
    
    
    
    
    public Person(int id, String firstName, String lastName, String picturePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picturePath = picturePath;
    }

    public Person(String firstName, String lastName, String picturePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.picturePath = picturePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean isDirector() {
        return isDirector;
    }

    public void setIsDirector(boolean isDirector) {
        this.isDirector = isDirector;
    }
    

    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Person o) {
        return String.valueOf(id).compareTo(String.valueOf(o.id));
    }

    
    
    
}
