/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author bisiv
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "title", "origName", "publishedDate", "genre", "description", 
    "picturePath", "actors", "directors"})
public class Movie {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private int id;
    private String title;
    @XmlJavaTypeAdapter(PublishedDateAdapter.class)
    private Optional<LocalDateTime> publishedDate;
    private String description;
    
    @XmlElementWrapper
    @XmlElement(name = "diretor")
    private List<Person> directors;
    
    @XmlElementWrapper
    @XmlElement(name = "actor")
    private List<Person> actors;
    
    private String genre;
    private String picturePath;
    private String origName;

    public Movie() {
    }

    public Movie(String title, Optional<LocalDateTime> publishedDate, String description, List<Person> directors, List<Person> actors, String genre, String picturePath, String origName) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
        this.directors = directors;
        this.actors = actors;
        this.genre = genre;
        this.picturePath = picturePath;
        this.origName = origName;
    }
    

    public Movie(int id, String title, Optional<LocalDateTime> publishDate, String description, List<Person> directors, List<Person> actors, String genre, String picturePath, String origName) {
        this.id = id;
        this.title = title;
        this.publishedDate = publishDate;
        this.description = description;
        this.directors = directors;
        this.actors = actors;
        this.genre = genre;
        this.picturePath = picturePath;
        this.origName = origName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<LocalDateTime> getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Optional<LocalDateTime> publishDate) {
        this.publishedDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    
    
    @Override
    public String toString() {
        return title + " - " + actors.toString() + directors + " | " + origName + " | " + genre + " | " + picturePath;
    }
    
    
}
