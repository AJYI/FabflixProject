import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.FileWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CastParser extends DefaultHandler {

    private String tempVal;
    private MoviesCast tempMovieCast;
    List<MoviesCast> moviesList;
    List<String> listOfInconsistencies;
    FileWriter fw;
    FileWriter castErrors;

    public CastParser() {
        moviesList = new ArrayList<MoviesCast>();
        listOfInconsistencies = new ArrayList<String>();
    }

    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();

        try {
            // Set up file to write into
            fw = new FileWriter("CastResults.txt");
            castErrors = new FileWriter("CastInconsistencies.txt");

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData() {

        System.out.println("Number of Movies Inserted: '" + moviesList.size() + "'.");

        Iterator<MoviesCast> it = moviesList.iterator();

        while (it.hasNext()) {
            MoviesCast temp = it.next();
            List<String> actors = temp.getActors();

            for(int i = 0; i < actors.size(); i++){
                System.out.println("Title: " + temp.getTitle() + " | Actors: " + actors.get(i));
            }
            //System.out.println(it.next().toString());

        }

//        System.out.println("Number of inconsistencies found in casts124.xml: '" + listOfInconsistencies.size() + "'.");
//        System.out.println("All inconsistencies found in casts124.xml: ");

//        while (incons.hasNext()) {
//            System.out.println(incons.next().toString());
//        }
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("filmc")) {
            tempMovieCast = new MoviesCast();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        Pattern pattern = Pattern.compile("[^a-z0-9,:'+.!/]-", Pattern.CASE_INSENSITIVE);
        Pattern namePattern = Pattern.compile("[^a-z]-", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        Matcher nameMatcher;
        boolean foundSpec;

        if(qName.equalsIgnoreCase("filmc")) {
            try {
                moviesList.add(tempMovieCast);
            } catch (Exception e) {
                System.out.println("Failure to add movieCast object into moviesList.");
            }
        } else if (qName.equalsIgnoreCase("f")) {
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempMovieCast.setId(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie ID (<f>): " + tempVal);
                tempMovieCast.setId(null);
                System.out.println("Error in adding Id to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("t")) {
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempMovieCast.setTitle(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Title(<t>): " + tempVal);
                tempMovieCast.setTitle(null);
                System.out.println("Error in adding Title to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("a")) {
            try {
                tempMovieCast.addActor(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Actor (<a>): " + tempVal);
                System.out.println("Failure in adding Actor to tempMoviesCast - tempVal: " + tempVal);
            }
        }

    }

    public void addStarsToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            String url = "jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true";
            String user = "mytestuser";
            String password = "My6$Password";
//
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String listQuery = "select m.title, m.id from movies m;";
                PreparedStatement statement = conn.prepareStatement(listQuery);
                ResultSet rs = statement.executeQuery();

                // The mapped data of movie title and id
                Map<String, String> movieMap = new HashMap<String, String>();
                while(rs.next()){
                    String title = rs.getString("title");
                    String id = rs.getString("id");
                    movieMap.put(title, id);
                }

                String ActorMapQuery = "select distinct s.name, s.id from stars s";
                statement = conn.prepareStatement(ActorMapQuery);
                rs = statement.executeQuery();

                Map<String, String> actorMap = new HashMap<String, String>();
                while(rs.next()){
                    String name = rs.getString("name");
                    String id = rs.getString("id");
                    actorMap.put(name, id);
                }

                statement.close();
                rs.close();


                // Preparing to put into the database
                conn.setAutoCommit(false);
                // INSERT INTO stars_in_movies VALUES('nm0149223','tt0293280');
                String query1 = "INSERT INTO stars_in_movies VALUES(?,?)";
                PreparedStatement statement1 = conn.prepareStatement(query1);




                Iterator<MoviesCast> it = moviesList.iterator();

                while (it.hasNext()) {
                    MoviesCast temp = it.next();
                    List<String> actors = temp.getActors();

                    for(int i = 0; i < actors.size(); i++){
                        System.out.println("Title: " + temp.getTitle() + " " + movieMap.get(temp.getTitle()) + " | Actors: " + actors.get(i) + " " +actorMap.get(actors.get(i)));

                        if(actorMap.get(actors.get(i)) == null){
                            continue;
                        }
                        if(movieMap.get(temp.getTitle()) == null){
                            continue;
                        }


                        // For query 1
                        statement1.setString(2, movieMap.get(temp.getTitle()));
                        statement1.setString(1, actorMap.get(actors.get(i)));
                        // Adding the batches
                        statement1.addBatch();
                    }
                }

                System.out.println(statement1);












                System.out.println("Entering into the database");
                statement1.executeBatch();
                conn.commit();
                statement1.close();


//
//                HashSet<MovieHashData> hashTable = new HashSet<MovieHashData>();
//
//                while(rs.next()){
//                    String title = rs.getString("title");
//                    String director = rs.getString("director");
//                    Integer year = rs.getInt("year");
//                    if(year == 0){
//                        year = null;
//                    }
//
//                    MovieHashData temp = new MovieHashData();
//                    temp.setDirectorName(director);
//                    temp.setTitle(title);
//                    temp.setYear(year);
//                    hashTable.add(temp);
//                }
//
//                for (MovieHashData a: hashTable){
//                    System.out.println("HashTable Entry " + a.getTitle() + " " + a.getDirectorName() + " " + a.getYear());
//                }
//
//                // To get the max starID
//                String maxQuery = "select m.id from movies m order by m.id desc limit 1;";
//                statement = conn.prepareStatement(maxQuery);
//                rs = statement.executeQuery();
//                rs.next();
//                String currentMax = rs.getString("id");
//                int currentId = Integer.parseInt(currentMax.substring(2));
//
//                //Format = tt0499469
//
//                //Using a convertor class
//                HashSet<MovieHashData> movieSet = new HashSet<MovieHashData>();
//                Iterator<DirectorFilms> it = directorFilms.iterator();
//                while(it.hasNext()){
//                    DirectorFilms temp = it.next();
//                    List<Movies> movieTemp = temp.getMovies();
//
//                    for (int i = 0; i < movieTemp.size(); i++){
//                        MovieHashData temp2 = new MovieHashData();
//                        temp2.setDirectorName(temp.getDirectorName());
//                        temp2.setTitle(movieTemp.get(i).getTitle());
//                        temp2.setYear(movieTemp.get(i).getYear());
//                        movieSet.add(temp2);
//                    }
//                }
//
//                conn.setAutoCommit(false);
//                // INSERT INTO movies VALUES('tt0351795','Ripoux 3',2003,'Claude Zidi');
//                String query1 = "INSERT INTO movies VALUES(?,?,?,?)";
//                String query2 = "INSERT INTO genres_in_movies VALUES(1,?)";
//                PreparedStatement statement1 = conn.prepareStatement(query1);
//                PreparedStatement statement2 = conn.prepareStatement(query2);
//
//                // Gotta process the movie now
//                // some for loop in here
//                for (MovieHashData a : movieSet){
//                    // If the hash table already contains a
//                    String id;
//                    if (hashTable.contains(a)){
//                        // Means we dont have to do anything because it already exists
//                        System.out.println("Found a match -------------> " +a.getDirectorName());
//                        continue;
//                    }
//                    // means that actor doesnt exist
//                    else{
//                        currentId++;
//                        id = "tt" + currentId;
//                    }
//                    System.out.println("Adding movies: " + id + " |DirectorName " +a.getDirectorName() + " |MovieTitle: " + a.getTitle() + " |MovieYear: " + a.getYear());
//
//                    if(a.getYear() == null){
//                        continue;
//                    }
//                    if(a.getTitle() == null){
//                        continue;
//                    }
//                    if(a.getDirectorName() == null){
//                        continue;
//                    }
//
//                    // For query 2
//                    statement2.setString(1, id);
//                    // For query 1
//                    statement1.setString(1, id);
//                    statement1.setString(2, a.getTitle());
//                    statement1.setInt(3, a.getYear());
//                    statement1.setString(4, a.getDirectorName());
//
//                    // Adding the batches
//                    statement1.addBatch();
//                    statement2.addBatch();
//                }
//
//
//                System.out.println("Entering into the database");
//                System.out.println("Inserting " + movieSet.size() + " entries");
//                statement1.executeBatch();
//                statement2.executeBatch();
//                conn.commit();
//
//                statement.close();
//                statement1.close();
//                statement2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CastParser castParser = new CastParser();
        castParser.runExample();
        castParser.addStarsToDatabase();
    }

}
