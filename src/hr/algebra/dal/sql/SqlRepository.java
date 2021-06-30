/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Person;
import hr.algebra.model.Movie;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author bisiv
 */
public class SqlRepository implements Repository {

    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PublishDate";
    private static final String DESCRIPTION = "Description";
    private static final String GENRE = "Genre";
    private static final String PICTURE_PATH = "PicturePath";
    private static final String ORIGNAME = "OrigName";

    private static final String ID_DIRECTOR = "IDDirector";
    private static final String ID_ACTOR = "IDActor";

    private static final String IS_ADMIN = "IsAdmin";
    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";
    private static final String EMAIL = "Email";
    private static final String LAST_NAME = "LastName";
    private static final String FIRST_NAME = "FirstName";

    private static final String FIND_USER = "{ CALL spFindUser (?,?) }";
    private static final String UPDATE_USER = "{ CALL spUpdateUser (?, ?, ?, ?, ?, ?, ?) }";
    private static final String CREATE_USER = "{ CALL spCreateUser (?, ?, ?, ?, ?, ?, ?) }";
    private static final String FIND_USER_BY_EMAIL = " { CALL spFindUserByEmail (?) }";
    private static final String FIND_USER_BY_USERNAME = " { CALL spFindUserByUsername (?) }";

    private static final String CREATE_MOVIE = " { CALL spCreateMovie (?, ?, ?, ?, ?, ?, ?, ? ) }";
    private static final String SELECT_MOVIES = " { CALL spSelectAllMovies }";
    private static final String SELECT_MOVIE = " { CALL spSelectMovie (?)}";
    private static final String DELETE_MOVIE = " { CALL spDeleteMovie (?)}";
    private static final String UPDATE_MOVIE = " { CALL spUpdateMovie (?, ?, ?, ?, ?, ?, ?) }";

    private static final String CREATE_ACTOR = " { CALL spCreateActor (?, ?, ?, ?)}";
    private static final String UPDATE_ACTOR = " { CALL spUpdateActor (?, ?, ?, ?)}";
    private static final String SELECT_ACTORS = " { CALL spSelectActors }";
    private static final String SELECT_ACTOR = " { CALL spSelectActor (?)}";
    private static final String DELETE_ACTOR = " { CALL spDeleteActor (?)}";
    private static final String FIND_ACTOR = " { CALL spFindActor (?)}";
    private static final String LINK_MOVIE_ACTORS = " { CALL spLinkActorsToMovies (?, ?)}";
    private static final String DELETE_MOVIE_ACTORS = " { CALL spDeleteActorsMovie (?)} ";

    private static final String CREATE_DIRECTOR = " { CALL spCreateDirector (?, ?, ?, ?)}";
    private static final String UPDATE_DIRECTOR = " { CALL spUpdateDirector (?, ?, ?, ?)}";
    private static final String SELECT_DIRECTORS = " { CALL spSelectDirectors }";
    private static final String SELECT_DIRECTOR = " { CALL spSelectDirector (?) }";
    private static final String DELETE_DIRECTOR = " { CALL spDeleteDirector (?) }";
    private static final String FIND_DIRECTOR = " { CALL spFindDIrector (?)}";
    private static final String LINK_MOVIE_DIRECTORS = " { CALL spLinkDirectorsToMovies (?, ?)}";
    private static final String DELETE_MOVIE_DIRECTORS = " { CALL spDeleteDirectorsMovie (?)} ";

    private static final String CLEAR_DATABASE = " { CALL spClearData }";

    @Override
    public User findUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(FIND_USER)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(EMAIL),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getBoolean(IS_ADMIN)
                    );
                }
            }

            return null;
        }

    }

    @Override
    public void updateUser(User user, String oldUsername, String oldPassword) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_USER)) {
            stmt.setString(1, oldUsername);
            stmt.setString(2, oldPassword);
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getUsername());
            stmt.setString(7, user.getPassword());

            stmt.executeUpdate();
        }
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(FIND_USER_BY_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(EMAIL),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getBoolean(IS_ADMIN)
                    );
                }
            }

            return null;
        }

    }

    @Override
    public User finUserByUsername(String username) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(FIND_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(EMAIL),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getBoolean(IS_ADMIN)
                    );
                }
            }

            return null;
        }
    }

    @Override
    public int createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setBoolean(6, user.getAdmin());
            stmt.registerOutParameter(7, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(7);
        }
    }

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getPublishedDate().get().format(Movie.DATE_FORMATTER));
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getGenre());
            stmt.setString(5, movie.getPicturePath());
            stmt.setString(6, movie.getOrigName());

            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.registerOutParameter(8, Types.BOOLEAN);

            stmt.executeUpdate();

            int id = stmt.getInt(7);
            List<Integer> actorIDs = new ArrayList<>();
            List<Integer> directorIDs = new ArrayList<>();

            movie.getActors().forEach(actor -> actorIDs.add(actor.getId()));
            movie.getDirectors().forEach(director -> directorIDs.add(director.getId()));

            linkEntitiesInTable(con, LINK_MOVIE_ACTORS, id, actorIDs);
            linkEntitiesInTable(con, LINK_MOVIE_DIRECTORS, id, directorIDs);

            return id;
        }
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmtMovie = con.prepareCall(CREATE_MOVIE)) {
            for (Movie movie : movies) {

                String origName = movie.getOrigName();
                String[] tags = {"3D", "IMAX", "4DX"};

                for (String tag : tags) {
                    if (origName.contains(tag)) {
                        origName = origName.substring(0, origName.indexOf(tag) - 1);
                    }
                }

                String formattedDate;
                if (movie.getPublishedDate().isPresent()) {
                    formattedDate = movie.getPublishedDate().get().format(Movie.DATE_FORMATTER);
                } else {
                    formattedDate = "";
                }

                stmtMovie.setString(1, movie.getTitle());
                stmtMovie.setString(2, formattedDate);
                stmtMovie.setString(3, movie.getDescription());
                stmtMovie.setString(4, movie.getGenre());
                stmtMovie.setString(5, movie.getPicturePath());
                stmtMovie.setString(6, origName);
                stmtMovie.registerOutParameter(7, Types.INTEGER);
                stmtMovie.registerOutParameter(8, Types.BOOLEAN);

                stmtMovie.executeUpdate();

                int MovieId = stmtMovie.getInt(7);
                boolean newMovie = stmtMovie.getBoolean(8);
                List<Integer> actorIDs;
                List<Integer> directorIDs;

                if (newMovie) {

                    List<Person> actors = movie.getActors();
                    List<Person> directors = movie.getDirectors();

                    actorIDs = createPeople(con, CREATE_ACTOR, actors);
                    linkEntitiesInTable(con, LINK_MOVIE_ACTORS, MovieId, actorIDs);

                    directorIDs = createPeople(con, CREATE_DIRECTOR, directors);
                    linkEntitiesInTable(con, LINK_MOVIE_DIRECTORS, MovieId, directorIDs);

                }

            }
        }
    }

    @Override
    public void updateMovie(int id, Movie data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {
            stmt.setInt(1, id);
            stmt.setString(2, data.getTitle());
            stmt.setString(3, data.getPublishedDate().get().toString());
            stmt.setString(4, data.getDescription());
            stmt.setString(5, data.getGenre());
            stmt.setString(6, data.getPicturePath());
            stmt.setString(7, data.getOrigName());

            stmt.executeUpdate();

            List<Integer> actorIDs = new ArrayList<>();
            List<Integer> directorIDs = new ArrayList<>();

            data.getActors().forEach((actor) -> {
                actorIDs.add(actor.getId());
            });

            data.getDirectors().forEach((director) -> {
                directorIDs.add(director.getId());
            });

            try (CallableStatement stmtDel = con.prepareCall(DELETE_MOVIE_ACTORS)) {
                stmtDel.setInt(1, id);

                stmtDel.executeUpdate();
            }
            try (CallableStatement stmtDel = con.prepareCall(DELETE_MOVIE_DIRECTORS)) {
                stmtDel.setInt(1, id);

                stmtDel.executeUpdate();
            }

            linkEntitiesInTable(con, LINK_MOVIE_ACTORS, id, actorIDs);
            linkEntitiesInTable(con, LINK_MOVIE_DIRECTORS, id, directorIDs);

        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        List<Person> actors = new ArrayList<>();
        List<Person> directors = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
            stmt.setInt(1, id);
            try (CallableStatement stmtActor = con.prepareCall(FIND_ACTOR)) {
                stmtActor.setInt(1, id);

                try (ResultSet rs = stmtActor.executeQuery()) {
                    while (rs.next()) {
                        actors.add(new Person(
                                rs.getInt(ID_ACTOR),
                                rs.getString(FIRST_NAME),
                                rs.getString(LAST_NAME),
                                rs.getString(PICTURE_PATH)
                        ));
                    }

                }
            }

            try (CallableStatement stmtDirector = con.prepareCall(FIND_DIRECTOR)) {
                stmtDirector.setInt(1, id);

                try (ResultSet rs = stmtDirector.executeQuery()) {
                    while (rs.next()) {
                        directors.add(new Person(
                                rs.getInt(ID_DIRECTOR),
                                rs.getString(FIRST_NAME),
                                rs.getString(LAST_NAME),
                                rs.getString(PICTURE_PATH)
                        ));
                    }

                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Optional<LocalDateTime> localDateTime;
                    
                    try{
                        localDateTime = Optional.of(LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER));
                    }catch(DateTimeParseException e){
                        localDateTime = Optional.empty();
                    }

                    return Optional.of(new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            localDateTime,
                            rs.getString(DESCRIPTION),
                            directors,
                            actors,
                            rs.getString(GENRE),
                            rs.getString(PICTURE_PATH),
                            rs.getString(ORIGNAME)
                    ));

                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                List<Person> actors = new ArrayList<>();
                List<Person> directors = new ArrayList<>();
                int movieID = rs.getInt(ID_MOVIE);

                try (CallableStatement stmtActor = con.prepareCall(FIND_ACTOR)) {
                    stmtActor.setInt(1, movieID);
                    try (ResultSet rsActor = stmtActor.executeQuery()) {
                        while (rsActor.next()) {
                            actors.add(new Person(
                                    rsActor.getInt(ID_ACTOR),
                                    rsActor.getString(FIRST_NAME),
                                    rsActor.getString(LAST_NAME),
                                    rsActor.getString(PICTURE_PATH)
                            ));
                        }
                    }

                }
                //iste stvari, refaktor
                try (CallableStatement stmtDirector = con.prepareCall(FIND_DIRECTOR)) {
                    stmtDirector.setInt(1, movieID);
                    try (ResultSet rsDirector = stmtDirector.executeQuery()) {
                        while (rsDirector.next()) {
                            directors.add(new Person(
                                    rsDirector.getInt(ID_DIRECTOR),
                                    rsDirector.getString(FIRST_NAME),
                                    rsDirector.getString(LAST_NAME),
                                    rsDirector.getString(PICTURE_PATH)
                            ));
                        }
                    }

                }
                Optional<LocalDateTime> date;
                try {
                    date = Optional.of(LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER));
                } catch (DateTimeParseException e) {
                    date = Optional.empty();
                }

                movies.add(new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        date,
                        rs.getString(DESCRIPTION),
                        directors,
                        actors,
                        rs.getString(GENRE),
                        rs.getString(PICTURE_PATH),
                        rs.getString(ORIGNAME)
                ));

            }

        }
        return movies;
    }

    @Override
    public void clearDatabase() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CLEAR_DATABASE)) {
            stmt.executeUpdate();
        }

    }

    @Override
    public int createActor(Person actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {
            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setString(3, actor.getPicturePath());
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();

            return stmt.getInt(4);
        }
    }

    @Override
    public void updateActor(int id, Person data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ACTOR)) {

            stmt.setInt(1, id);
            stmt.setString(2, data.getFirstName());
            stmt.setString(3, data.getLastName());
            stmt.setString(4, data.getPicturePath());

            stmt.executeUpdate();

        }
    }

    @Override
    public void deleteActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTOR)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(ID_ACTOR),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(PICTURE_PATH)
                    ));
                }

            }
            return Optional.empty();
        }
    }

    @Override
    public List<Person> selectActors() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        List<Person> actors = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                actors.add(new Person(
                        rs.getInt(ID_ACTOR),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME),
                        rs.getString(PICTURE_PATH)));
            }
        }

        return actors;
    }

    @Override
    public int createDirector(Person director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)) {
            stmt.setString(1, director.getFirstName());
            stmt.setString(2, director.getLastName());
            stmt.setString(3, director.getPicturePath());
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();

            return stmt.getInt(4);
        }
    }

    @Override
    public void updateDirector(int id, Person data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_DIRECTOR)) {

            stmt.setInt(1, id);
            stmt.setString(2, data.getFirstName());
            stmt.setString(3, data.getLastName());
            stmt.setString(4, data.getPicturePath());

            stmt.executeUpdate();

        }
    }

    @Override
    public void deleteDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_DIRECTOR)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTOR)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(ID_DIRECTOR),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(PICTURE_PATH)
                    ));
                }

            }
            return Optional.empty();
        }
    }

    @Override
    public List<Person> selectDirectors() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        List<Person> directors = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                directors.add(new Person(
                        rs.getInt(ID_DIRECTOR),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME),
                        rs.getString(PICTURE_PATH)));
            }
        }

        return directors;
    }

    private List<Integer> createPeople(Connection con, String call, List<Person> people) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(call)) {
            if (people != null) {

                for (Person person : people) {
                    stmt.setString(1, person.getFirstName());
                    stmt.setString(2, person.getLastName());
                    stmt.setString(3, null);
                    stmt.registerOutParameter(4, Types.INTEGER);
                    stmt.executeUpdate();
                    int n = stmt.getInt(4);

                    IDs.add(n);

                }
            }
        }
        return IDs;
    }

    private void linkEntitiesInTable(Connection con, String call, int id, List<Integer> ids) throws SQLException {
        try (CallableStatement stmt = con.prepareCall(call)) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setInt(1, id);
                stmt.setInt(2, ids.get(i));

                stmt.executeUpdate();
            }

        }
    }
}
