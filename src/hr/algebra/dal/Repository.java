/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author bisiv
 */
public interface Repository {
        
    User findUser(String username, String password) throws Exception;
    int createUser(User user) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User finUserByUsername(String username) throws Exception;
    void updateUser(User user, String oldUsername, String oldPassword) throws Exception;
    
    int createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;
    void updateMovie(int id, Movie data) throws Exception;
    void deleteMovie(int id) throws Exception;
    Optional<Movie> selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    
    int createActor(Person actor) throws Exception;
    void updateActor(int id, Person data) throws Exception;
    void deleteActor(int id) throws Exception;
    Optional<Person> selectActor(int id) throws Exception;
    List<Person> selectActors() throws Exception;
    
    int createDirector(Person director) throws Exception;
    void updateDirector(int id, Person data) throws Exception;
    void deleteDirector(int id) throws Exception;
    Optional<Person> selectDirector(int id) throws Exception;
    List<Person> selectDirectors() throws Exception;
    
    public void clearDatabase() throws Exception;
}
