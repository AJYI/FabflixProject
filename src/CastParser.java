import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.sql.DataSource;
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

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
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

        }

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
            try (Connection conn = dataSource.getConnection()) {
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
