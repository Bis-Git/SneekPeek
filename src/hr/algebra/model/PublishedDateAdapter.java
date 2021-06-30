/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author bisiv
 */
public class PublishedDateAdapter extends XmlAdapter<String, Optional<LocalDateTime>>{

    @Override
    public Optional<LocalDateTime> unmarshal(String text) throws Exception {
        return Optional.of(LocalDateTime.parse(text, Movie.DATE_FORMATTER));
    }

    @Override
    public String marshal(Optional<LocalDateTime> date) throws Exception {
        return date.get().format(Movie.DATE_FORMATTER);
    }
    
}
