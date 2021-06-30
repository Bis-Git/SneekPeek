/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author bisiv
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx";
    private static final String REQUEST_METHOD = "GET";
    private static final int TIMEOUT = 10000;
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final Random RANDOM = new Random();

    public static List<Movie> parse() throws IOException, XMLStreamException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);
        XMLEventReader reader = ParserFactory.createStaxParser(con.getInputStream());

        Optional<TagType> tagType = Optional.empty();
        Movie movie = null;

        StartElement startElement;

        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        break;
                    
                    case XMLStreamConstants.CHARACTERS:
                        if (tagType.isPresent()) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            if (tagType.get() == TagType.ITEM) {
                                System.out.println("item");
                            }
                            switch (tagType.get()) {
                                case ITEM:
                                    movie = new Movie();
                                    movies.add(movie);
                                    break;
                                case TITLE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                    break;
                                case PUBLISHED_DATE:
                                    if (movie != null && !data.isEmpty()) {
                                        Optional<LocalDateTime> publishedDate = Optional.of(LocalDateTime.parse(data, DATE_FORMATTER));
                                        movie.setPublishedDate(publishedDate);
                                    } else {
                                        movie.setPublishedDate(Optional.empty());
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (movie != null && !data.isEmpty()) {
                                        String subHelper;
                                        data = data.substring(data.lastIndexOf("\">") + 2, data.lastIndexOf("<"));
                                        if (data.contains("</div>")) {
                                            data = data.substring(0, data.lastIndexOf(".") + 1);
                                            if (data.contains("<br />")) {
                                                subHelper = data.substring(0, data.indexOf("<"));
                                                data = data.substring(data.indexOf(">") + 1);
                                                data = subHelper.concat(data);
                                            }
                                        }
                                        movie.setDescription(data);
                                    }
                                    break;
                                case DIRECTOR:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDirectors(parsePerson(data, movie));
                                    }
                                    break;
                                case ACTORS:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setActors(parsePerson(data, movie));
                                    }
                                    break;
                                case GENRE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setGenre(data.substring(0, 1).toLowerCase() + data.substring(1));
                                    }
                                    break;
                                case PICTURE:
                                    if (movie != null && !data.isEmpty()) {
                                        
                                        handlePicture(movie, data.trim());
                                        
                                    }
                                    break;
                                case ORIGNAME:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setOrigName(data);
                                    }
                            }
                        }
                        break;
                }
            }
        } catch (XMLStreamException | IOException ex) {
            ex.printStackTrace();
        }
        return movies;
    }

    private static List<Person> parsePerson(String data, Movie movie) {
        String[] names = data.split(", ");
        List<Person> people = new ArrayList<>();
        if (names != null) {
            for (String name : names) {
                String[] n = name.split(" ");
                
                switch (n.length) {
                    case 2:
                        people.add(new Person(n[0], n[1]));
                        break;
                    case 3:
                        people.add(new Person(n[0] + " " + n[1], n[2]));
                        break;
                    default:
                        people.add(new Person(n[0], ""));
                        break;
                }
            }
        }
        return people;
    }

    private static void handlePicture(Movie movie, String pictureUrl) throws IOException {

        pictureUrl = pictureUrl.replaceAll("http", "https");
        String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String pictureName = Math.abs(RANDOM.nextInt()) + ext;
        String localPicturePath = DIR + File.separator + pictureName;

        FileUtils.copyFromUrl(pictureUrl, localPicturePath);
        movie.setPicturePath(localPicturePath);
    }

    private enum TagType {
        ITEM("item"),
        TITLE("title"),
        PUBLISHED_DATE("pubDate"),
        DESCRIPTION("description"),
        DIRECTOR("redatelj"),
        ACTORS("glumci"),
        GENRE("zanr"),
        PICTURE("plakat"),
        ORIGNAME("orignaziv");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
}
